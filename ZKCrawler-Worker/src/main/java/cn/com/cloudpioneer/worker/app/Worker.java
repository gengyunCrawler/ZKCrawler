package cn.com.cloudpioneer.worker.app;

import cn.com.cloudpioneer.worker.listener.MyTaskCacheListener;
import cn.com.cloudpioneer.worker.model.TaskModel;
import cn.com.cloudpioneer.worker.tasks.MyTasks;
import cn.com.cloudpioneer.worker.utils.RandomUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/1.
 */

/**
 * Worker 是一个单例，任何时候的任何有关worker操作的请求，
 * 只由此一个work处理。
 */
public class Worker implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);

    private volatile static Worker thisWorker;

    private static final String MY_STATUS = "alive";
    public static final String TASKS_ROOT_PATH = "/tasks";
    public static final String WORKERS_ROOT_PATH = "/workers";
    public static final String LOCK_ROOT_PATH = "/lock-4-workers";

    private Map<String, String> taskLockMap;

    private MyTasks myTasks;

    private final TreeCache myTasksCache;
    private String workerId;
    private CuratorFramework client;


    private TreeCacheListener myTaskCacheListener;

    private String getMyId() {

        return RandomUtils.getRandomString(10);
    }


    private void register() throws Exception {

        // 注册锁根节点,永久类型
        if (client.checkExists().forPath(LOCK_ROOT_PATH) == null) {
            client.create().withMode(CreateMode.PERSISTENT).forPath(LOCK_ROOT_PATH);
            LOGGER.info("register znode: " + LOCK_ROOT_PATH + " finished.");
        }

        // 去 workers 节点下注册 worker 节点，持久类型（ephemeral）
        client.create().withMode(CreateMode.PERSISTENT).forPath(WORKERS_ROOT_PATH + "/" + workerId, workerId.getBytes());
        LOGGER.info("register zonode: " + WORKERS_ROOT_PATH + "/" + workerId + " finished.");

        // 去 workers 节点下之间的节点woker 注册状态节点status，短暂类型（persistent）
        client.create().withMode(CreateMode.EPHEMERAL).forPath(WORKERS_ROOT_PATH + "/" + workerId + "/status", MY_STATUS.getBytes());
        LOGGER.info("register zonode: " + WORKERS_ROOT_PATH + "/" + workerId + "/status" + " finished.");
    }


    private synchronized boolean isGetTaskLock(String taskId) {

        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(LOCK_ROOT_PATH + "/" + taskId);
        } catch (Exception e) {
            LOGGER.warn("create lock: " + taskId + " Exception.");
            return false;
            //e.printStackTrace();
        }

        taskLockMap.put(taskId, LOCK_ROOT_PATH + "/" + taskId);
        LOGGER.info("create lock: " + taskId + " OK.");
        return true;
    }


    private synchronized void releaseTaskLock(String taskId) {

        String lockPath = taskLockMap.remove(taskId);
        if (lockPath != null) {
            try {
                client.delete().forPath(lockPath);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        LOGGER.info("release lock: " + lockPath);
    }


    public String getWorkerId(){

        return this.workerId;
    }

    public void myTaskWirteBack(String taskId) {

        TaskModel task = myTasks.removeTask(taskId);
        int costTime = (int) (System.currentTimeMillis() - task.getStartTime());
        task.getEntity().setTimeStop(new Date());
        task.getEntity().setCostLastCrawl(costTime);
        task.getEntity().setCompleteTimes(task.getEntity().getCompleteTimes() + 1);

        final TaskModel backTask = task;

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {

                    if (!isGetTaskLock(backTask.getEntity().getId())) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    break;
                }
                int count;
                try {
                    count = Integer.parseInt(new String(client.getData().forPath(backTask.getTaskPath() + "/status"), "UTF-8"));
                } catch (Exception e) {
                    releaseTaskLock(backTask.getEntity().getId());
                    e.printStackTrace();
                    return;
                }
                count--;
                if (count == 0) {

                    try {
                        client.setData().forPath(backTask.getTaskPath(), backTask.getEntity().toString().getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        client.setData().forPath(backTask.getTaskPath() + "/status", ("" + count).getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                releaseTaskLock(backTask.getEntity().getId());
            }
        }).start();
    }


    private Worker(String hostPort, RetryPolicy retryPolicy) {

        taskLockMap = new HashMap<>();
        myTasks = new MyTasks();
        workerId = "worker-" + getMyId();
        LOGGER.info("Worker constructing, hostPort:" + hostPort);
        LOGGER.info("my work id: " + workerId);
        this.client = CuratorFrameworkFactory.newClient(hostPort, retryPolicy);
        this.myTasksCache = new TreeCache(this.client, WORKERS_ROOT_PATH + "/" + workerId);
        this.myTaskCacheListener = new MyTaskCacheListener();

    }

    public static Worker initializeWorker(String hostPort, RetryPolicy retryPolicy) {

        if (thisWorker == null)
            thisWorker = new Worker(hostPort, retryPolicy);
        return thisWorker;
    }

    public void addTaskToRunning(TaskModel task) {

        this.myTasks.addTask(task);
    }

    public byte[] getZnodeData(String znode) {

        try {
            return client.getData().forPath(znode);
        } catch (Exception e) {
            LOGGER.warn("get znode data error.");
            //e.printStackTrace();
        }
        return null;
    }


    public List<TaskModel> getMyTasks() {

        return myTasks.getTasks();

    }


    private void startZK() {

        LOGGER.info("starting zookeeper client.");
        client.start();
        LOGGER.info("zookeeper client started.");
    }

    private void bootsrap() {

        try {
            register();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("register znode Exception");
        }

    }

    private void runForWorker() {

        myTasksCache.getListenable().addListener(myTaskCacheListener);
        try {
            myTasksCache.start();
        } catch (Exception e) {
            LOGGER.warn("myTasksCache starting Exception");
            e.printStackTrace();

        }

    }


    @Override
    public void close() throws IOException {

        LOGGER.info("closing worker");
        myTasksCache.close();
        client.close();
        LOGGER.info("worker closed.");

    }

    /**
     *
     */
    public void workerStart() {

        try {
            startZK();
            bootsrap();
            runForWorker();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized Worker getThisWorker() {

        return Worker.thisWorker;
    }
}
