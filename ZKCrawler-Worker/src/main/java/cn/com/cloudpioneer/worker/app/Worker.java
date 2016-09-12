package cn.com.cloudpioneer.worker.app;

import cn.com.cloudpioneer.worker.listener.MyTaskCacheListener;
import cn.com.cloudpioneer.worker.model.TaskModel;
import cn.com.cloudpioneer.worker.tasks.MyTasks;
import cn.com.cloudpioneer.worker.utils.HttpClientUtils;
import cn.com.cloudpioneer.worker.utils.RandomUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Administrator on 2016/9/1.
 */

/**
 * Worker 是一个单例，任何时候的任何有关worker操作的请求，
 * 只由此一个work处理。
 */
public class Worker implements Closeable, ConnectionStateListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);

    private volatile static Worker thisWorker;

    private static final String MY_STATUS = "alive";
    public static final String TASKS_ROOT_PATH = "/tasks";
    public static final String WORKERS_ROOT_PATH = "/workers";
    public static final String LOCK_ROOT_PATH = "/lock-4-workers";

    public static final String API_CRAWLER_TASK_STARTER = ResourceBundle.getBundle("config").getString("API_CRAWLER_TASK_STARTER");
    public static final String API_CRAWLER_TASK_STOPPER = ResourceBundle.getBundle("config").getString("API_CRAWLER_TASK_STOPPER");

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
            LOGGER.warn("get lock: " + taskId + " Failed. return false.");
            return false;
        }

        taskLockMap.put(taskId, LOCK_ROOT_PATH + "/" + taskId);
        LOGGER.info("get lock: " + taskId + " Success. return true.");
        return true;
    }


    private synchronized void releaseTaskLock(String taskId) {

        int retry = 5;
        String lockPath = taskLockMap.remove(taskId);
        if (lockPath != null) {
            while (retry > 0) {

                try {
                    client.delete().forPath(lockPath);
                } catch (Exception e) {
                    LOGGER.warn("release lock: " + lockPath + " Exception. retry ......");
                    retry--;
                    continue;
                }

                LOGGER.info("release lock: " + lockPath + " ok.");
                break;
            }
        } else
            LOGGER.warn("try to release a not exists lock, ignore it ......");

    }


    public String getWorkerId() {

        return this.workerId;
    }


    public void myTaskWirteBack(String taskId) {

        TaskModel task = myTasks.removeTask(taskId);
        if (task == null) {
            LOGGER.warn("try to write back a null task to the TaskClient, taskId = " + taskId + ", ignore it.");
            return;
        }
        int costTime = (int) (System.currentTimeMillis() - task.getStartTime());
        task.getEntity().setTimeStop(new Date());
        task.getEntity().setCostLastCrawl(costTime);
        task.getEntity().setCompleteTimes(task.getEntity().getCompleteTimes() + 1);

        final TaskModel backTask = task;

        new Thread(new Runnable() {
            @Override
            public void run() {

                int retry = 5;
                int count = Integer.MAX_VALUE;
                byte[] data = null;
                boolean isException = false;

                while (true) {

                    if (!isGetTaskLock(backTask.getEntity().getId())) {
                        LOGGER.info("can't get the lock, retry ......");
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {

                        }
                        continue;
                    }
                    LOGGER.info("get lock ok.");
                    break;
                }


                while (retry > 0) {

                    try {
                        data = client.getData().forPath(backTask.getTaskPath() + "/status");

                    } catch (Exception e) {
                        LOGGER.warn("get the znode: " + backTask.getTaskPath() + "/status  data error. retry ......");
                        retry--;
                        isException = true;
                        continue;
                    }
                    isException = false;
                    LOGGER.info("get the znode: " + backTask.getTaskPath() + "/status  data success.");
                    break;
                }

                if (isException) {
                    LOGGER.error("task write back error. release the lock and return.");
                    releaseTaskLock(backTask.getEntity().getId());
                    return;
                }

                try {
                    count = Integer.parseInt(new String(data, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                count--;
                if (count == 0) {

                    retry = 5;
                    while (retry > 0) {

                        try {
                            client.setData().forPath(backTask.getTaskPath(), backTask.getEntity().toString().getBytes());
                        } catch (Exception e) {
                            LOGGER.warn("write task data back to znode: " + backTask.getTaskPath() + " Exception. retry ......");
                            retry--;
                            isException = true;
                            continue;
                        }
                        isException = false;
                        LOGGER.info("write task data back to znode: " + backTask.getTaskPath() + " Success.");
                        break;
                    }

                    if (isException) {
                        LOGGER.error("task write back error. release the lock and return.");
                        releaseTaskLock(backTask.getEntity().getId());
                        return;
                    }
                    retry = 5;
                    while (retry > 0) {

                        try {
                            client.setData().forPath(backTask.getTaskPath() + "/status", "0".getBytes());
                        } catch (Exception e) {
                            LOGGER.warn("write task data back to znode: " + backTask.getTaskPath() + "/status Exception. retry ......");
                            retry--;
                            isException = true;
                            continue;
                        }
                        isException = false;
                        break;
                    }

                    if (isException) {
                        LOGGER.error("task write back error. release the lock and return.");
                        releaseTaskLock(backTask.getEntity().getId());
                        return;
                    }

                    LOGGER.info("write task data back to TaskClient Success. release the lock and return this thread.");
                    releaseTaskLock(backTask.getEntity().getId());

                } else {
                    LOGGER.info("I am not the last worker, count for me is: " + count);
                    retry = 5;

                    while (retry > 0) {
                        try {
                            //LOGGER.info("count: " + Integer.valueOf(count).toString().getBytes());
                            client.setData().forPath(backTask.getTaskPath() + "/status", ("" + count).getBytes());
                        } catch (Exception e) {
                            LOGGER.warn("write count back to znode: " + backTask.getTaskPath() + "/status Exception. retry ......");
                            retry--;
                            isException = true;
                            continue;
                        }
                        isException = false;
                        break;
                    }
                }
                if (isException) {
                    LOGGER.error("write count data back error. release the lock and return.");
                    releaseTaskLock(backTask.getEntity().getId());
                    return;
                }
                LOGGER.info("write count data back  Success. release the lock and return this thread.");
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

    public void addTaskToRunning(final TaskModel task) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpClientUtils.jsonPostRequest(API_CRAWLER_TASK_STARTER, task.getEntityString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                myTasks.addTask(task);
            }
        }).start();

    }



    public void removeTaskInRunning(final TaskModel task) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpClientUtils.jsonPostRequest(API_CRAWLER_TASK_STOPPER, task.getEntity().getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public byte[] getZnodeData(String znode) {

        try {
            return client.getData().forPath(znode);
        } catch (Exception e) {
            LOGGER.warn("get znode data error.");
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

        LOGGER.info("closing worker ......");
        CloseableUtils.closeQuietly(myTasksCache);
        CloseableUtils.closeQuietly(client);
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


    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {

        switch (connectionState) {

            case CONNECTED:
                LOGGER.info("worker connected.");
                break;
            case SUSPENDED:
                LOGGER.info("worker suspended.");
                break;
            case RECONNECTED:
                LOGGER.info("worker reconnected.");
                workerStart();
                break;
            case LOST:
                LOGGER.info("worker lost.");
                try {
                    close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case READ_ONLY:
                LOGGER.info("worker read only event.");
                break;
        }
    }

}
