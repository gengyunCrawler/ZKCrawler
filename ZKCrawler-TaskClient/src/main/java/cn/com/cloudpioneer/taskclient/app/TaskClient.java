package cn.com.cloudpioneer.taskclient.app;

import cn.com.cloudpioneer.taskclient.chooser.TaskChooser;
import cn.com.cloudpioneer.taskclient.chooser.chooserImpl.LongTimeFirstTaskChooser;
import cn.com.cloudpioneer.taskclient.dao.TaskDao;
import cn.com.cloudpioneer.taskclient.listener.TasksCacheListener;
import cn.com.cloudpioneer.taskclient.listener.WorkersCacheListener;
import cn.com.cloudpioneer.taskclient.model.ConfigItem;
import cn.com.cloudpioneer.taskclient.model.TaskEntity;
import cn.com.cloudpioneer.taskclient.model.TaskModel;
import cn.com.cloudpioneer.taskclient.model.TaskStatusItem;
import cn.com.cloudpioneer.taskclient.scheduler.SchedulePolicy;
import cn.com.cloudpioneer.taskclient.utils.RandomUtils;
import com.alibaba.fastjson.JSON;
import com.sun.istack.internal.Nullable;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Tianjinjin on 2016/9/1.
 */
public class TaskClient implements Closeable, LeaderSelectorListener {

    private static Logger LOGGER = LoggerFactory.getLogger(TaskClient.class);

    private static volatile TaskClient thisTaskClient;

    private Timer tasksTimer;

    //TaskClient的配置
    private Map<String, Object> configs;

    //TaskClient的Id
    private String myId;

    private String hostPort;

    //ZK的客户端对象
    private CuratorFramework client;

    //主节点选择器
    private LeaderSelector leaderSelector;

    //任务的缓存器
    private TreeCache tasksCache;


    private TreeCache workersCache;


    //我的任务实体集合
    private Map<String, TaskModel> myTasks;


    private Lock lock4MyTasks;

    //任务选择器成员
    private TaskChooser taskChooser;

    //任务调度器
    private Scheduler scheduler;


    private ExecutorService executorService;

    //领导锁
    private CountDownLatch leaderLatch = new CountDownLatch(1);
    //关闭锁
    private CountDownLatch closeLatch = new CountDownLatch(1);


    private static int tasksScannerPeriod = 2;

    /**
     * thisTaskClient 自己拥有的所有锁的集合。taskLockMap 存储 worker 当前自己拥有的锁。
     */
    private Map<String, String> taskLockMap;

    /**
     * LOCK_ROOT_PATH 是任务锁节点的根节点，节点是永久类型（persistent）。
     */
    public static final String LOCK_ROOT_PATH = "/lock-4-tasks";

    public static final String TASKS_ROOT_PATH = "/tasks";
    public static final String WORKERS_ROOT_PATH = "/workers";
    public static final String CLIENT_ROOT_PATH = "/TASK_CLIENT";


    static {
        try {
            tasksScannerPeriod = Integer.parseInt(ResourceBundle.getBundle("config").getString("TASKS_SCANNER_PERIOD"));
        } catch (NumberFormatException e) {
            tasksScannerPeriod = 2;
        }
    }

    /**
     * thisTaskClient 在工作时，是围绕任务来进行的，因而锁也是围绕任务来建立的。
     * 此方法是获取某个任务的锁。在 worker 中所说的锁都是独享锁。
     *
     * @param taskId 任务ID，以获取此任务的锁。
     * @return 当锁获得时返回 true，锁不能获得时返回 false。
     */
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


    /**
     * 释放 thisTaskClient 获得的锁。
     *
     * @param taskId 任务ID，也是该任务的锁id。
     */
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


    public static TaskClient initializeTaskClient(String hostPort, RetryPolicy retryPolicy, @Nullable String myId, @Nullable TaskChooser taskChooser, @Nullable String configFileName) {

        if (thisTaskClient == null)
            thisTaskClient = new TaskClient(hostPort, retryPolicy, myId, taskChooser, configFileName);
        return thisTaskClient;

    }

    public static TaskClient getThisTaskClient() {
        return thisTaskClient;
    }


    private TaskClient(String hostPort, RetryPolicy retryPolicy, @Nullable String myId, @Nullable TaskChooser taskChooser, @Nullable String configFileName) {

        this.tasksTimer = new Timer();
        this.myTasks = new HashMap<>();
        this.taskLockMap = new HashMap<>();
        this.executorService = Executors.newFixedThreadPool(10);
        this.lock4MyTasks = new ReentrantLock();
        this.configs = initConfigs(configFileName);

        if (myId != null)
            this.myId = myId;
        else
            this.myId = "TaskClient-" + RandomUtils.getRandomString(10) + "-" + System.currentTimeMillis();

        if (taskChooser == null)
            this.taskChooser = new LongTimeFirstTaskChooser();
        else
            this.taskChooser = taskChooser;

        this.scheduler = new Scheduler();

        this.hostPort = hostPort;
        this.client = CuratorFrameworkFactory.newClient(this.hostPort, retryPolicy);
        this.leaderSelector = new LeaderSelector(this.client, CLIENT_ROOT_PATH, this);
        this.workersCache = new TreeCache(this.client, WORKERS_ROOT_PATH);
        this.tasksCache = new TreeCache(this.client, TASKS_ROOT_PATH);

        tasksTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    LOGGER.info("+++-> tasks scanning ......  period: " + tasksScannerPeriod + " minutes");
                    tasksScanner();

                } catch (Exception e) {

                    LOGGER.warn("+++-> tasks scanning Exception!!!!");
                }
            }
        }, 120 * 1000, tasksScannerPeriod * 60 * 1000);
    }

    public void myExecutor(Runnable task) {

        if (!executorService.isShutdown())
            executorService.execute(task);
    }

    private void startZK() throws Exception {

        client.start();
    }


    private TaskModel removeMyTasks(String taskId) {

        try {
            lock4MyTasks.lock();
            return myTasks.remove(taskId);
        } finally {
            lock4MyTasks.unlock();
        }
    }


    private void addToMyTasks(String taskId, TaskModel taskModel) {


        try {
            lock4MyTasks.lock();
            myTasks.put(taskId, taskModel);
        } finally {
            lock4MyTasks.unlock();
        }
    }


    public int getMyTasksSize() {

        try {
            lock4MyTasks.lock();
            return myTasks.size();
        } finally {
            lock4MyTasks.unlock();
        }
    }


    private void updateMyTasks() {

        try {
            List<String> tasks = client.getChildren().forPath(TASKS_ROOT_PATH);
            for (String item : tasks) {
                List<String> workers = client.getChildren().forPath(TASKS_ROOT_PATH + "/" + item);
                for (int i = 0; i < workers.size(); i++) {
                    String s = workers.get(i);
                    if (workers.contains(s)) {
                        workers.remove(s);
                        i--;
                    }
                }
                TaskEntity entity = JSON.parseObject(new String(client.getData().forPath(TASKS_ROOT_PATH + "/" + item), "UTF-8"), TaskEntity.class);
                String statusData = new String(client.getData().forPath(TASKS_ROOT_PATH + "/" + item + "/status"), "UTF-8");
                addToMyTasks(entity.getId(), new TaskModel(TASKS_ROOT_PATH + "/" + item, entity, statusData, workers));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 为workersCache 添加监视器
     *
     * @param workersListener
     */
    private void addWorkersListener(TreeCacheListener workersListener) {
        this.workersCache.getListenable().addListener(workersListener);
    }

    /**
     * 为tasksCache 添加监视器
     *
     * @param tasksListener
     */
    private void addTasksListener(TreeCacheListener tasksListener) {
        this.tasksCache.getListenable().addListener(tasksListener);
    }

    /**
     * 等待选举结束
     *
     * @throws InterruptedException
     */
    private void awaitLeadership() throws InterruptedException {
        leaderLatch.await();
    }


    private Map<String, Object> initConfigs(@Nullable String configFileName) {
        Map<String, Object> configs = new HashMap<>();
        if (configFileName == null) {
            configs.put(ConfigItem.MAX_RUNNING_TASKS_SIZE, Integer.parseInt(ResourceBundle.getBundle("config").getString("MAX_RUNNING_TASKS_SIZE")));
            configs.put(ConfigItem.DEFAULT_TASK_CHOOSER, ResourceBundle.getBundle("config").getString("DEFAULT_TASK_CHOOSER"));
        }
        return configs;
    }

    public void setConfig(String configFileName) {

        configs = initConfigs(configFileName);
    }


    public Object getConfig(String key) {
        return configs.get(key);
    }


    public String getMyId() {
        return myId;
    }


    public void setMyId(String myId) {
        this.myId = myId;
    }


    private void bootsrap() {

        try {
            if (client.checkExists().forPath(LOCK_ROOT_PATH) == null) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(LOCK_ROOT_PATH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private TaskEntity getTaskData(String taskPath) {

        String taskId = taskPath.split("-")[1];
        while (!isGetTaskLock(taskId)) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            return JSON.parseObject(new String(client.getData().forPath(taskPath), "UTF-8"), TaskEntity.class);
        } catch (Exception e) {
            return null;
        } finally {

            releaseTaskLock(taskId);
        }
    }


    private int getTaskStatusData(String statusPath) {

        String taskId = statusPath.split("-")[1].split("/")[0];
        while (!isGetTaskLock(taskId)) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            return Integer.parseInt(new String(client.getData().forPath(statusPath), "UTF-8"));
        } catch (Exception e) {
            return -1;
        } finally {

            releaseTaskLock(taskId);
        }
    }

    public boolean taskUpdateProcess(String taskStatusPath) {

        String taskPath = TASKS_ROOT_PATH + "/" + taskStatusPath.split("/")[2];
        if (getTaskStatusData(taskStatusPath) == 0) {
            TaskEntity entity = getTaskData(taskPath);
            if (entity == null) {
                return false;
            }
            LOGGER.info("===> taskUpdateProcess get task data:\n\t" + entity.toString());
            //entity.setStatus(TaskStatusItem.TASK_STATUS_COMPLETED);
            removeMyTasks(entity.getId());
            taskWriteBack(entity);
            taskDelete(taskPath);

            return true;
        }

        return false;

    }


    private List<TaskEntity> tasksGeter(int size) {
        if (size <= 0)
            return new ArrayList<>();
        return this.taskChooser.chooser(size);
    }


    public void setSchedulerPolicy(SchedulePolicy policy) {

        this.scheduler.setPolicy(policy);
    }

    public synchronized void tasksCreator() throws Exception {

        int size = (int) configs.get(ConfigItem.MAX_RUNNING_TASKS_SIZE) - getMyTasksSize();
        LOGGER.info("will get tasks, size: " + size);
        List<TaskEntity> taskEntities = tasksGeter(size);
        List<String> workers = getWorks();
        if (taskEntities == null || taskEntities.size() == 0 || workers == null || workers.size() == 0) {
            LOGGER.info("tasks null or tasks size = 0 or workers is null or workers size = 0, return.");
            return;
        }

        LOGGER.info("get tasks size: " + taskEntities.size() + ", get workers size: " + workers.size());

        final Map<TaskEntity, List<String>> twMap = scheduler.scheduleProcess(taskEntities, workers);

        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry item : twMap.entrySet()) {
                    while (!isGetTaskLock(((TaskEntity) item.getKey()).getId())) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {

                        String taskId = ((TaskEntity) item.getKey()).getId();
                        byte[] taskData = (item.getKey()).toString().getBytes();
                        List<String> ws = (List<String>) item.getValue();

                        TaskModel taskModel = new TaskModel(TASKS_ROOT_PATH + "/task-" + taskId, ((TaskEntity) item.getKey()), "" + ws.size(), ws);
                        taskModel.setTaskStatus(TaskStatusItem.TASK_STATUS_RUNNING);

                        client.create().withMode(CreateMode.PERSISTENT).forPath(TASKS_ROOT_PATH + "/task-" + taskId, taskModel.getEntity().toString().getBytes());
                        for (String w : ws) {
                            client.create().withMode(CreateMode.PERSISTENT).forPath(TASKS_ROOT_PATH + "/task-" + taskId + "/" + w, "".getBytes());
                        }
                        client.create().withMode(CreateMode.PERSISTENT).forPath(TASKS_ROOT_PATH + "/task-" + taskId + "/status", ("" + ws.size()).getBytes());

                        addToMyTasks(taskId, taskModel);
                        taskWriteBack(taskModel.getEntity());

                    } catch (Exception ex) {

                    } finally {
                        releaseTaskLock(((TaskEntity) item.getKey()).getId());
                    }
                }
            }
        });

    }


    private void tasksScanner() throws Exception {
        if (!isLeader()) {
            LOGGER.info("I am not the leader, don't need to create tasks, my id is: " + myId);
            return;
        }
        LOGGER.info("I am the leader, my id is: " + myId);
        tasksCreator();
    }

    private int taskWriteBack(TaskEntity taskEntity) {

        return new TaskDao().updateTaskEntityById(taskEntity);
    }


    /**
     * 删除tasks下已完成的任务节点
     *
     * @param taskNode
     * @return
     */
    private boolean taskDelete(String taskNode) {

        String taskId = taskNode.split("-")[1];
        while (!isGetTaskLock(taskId)) {

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            List<String> children = client.getChildren().forPath(taskNode);
            for (String item : children)
                client.delete().forPath(taskNode + "/" + item);
            client.delete().forPath(taskNode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseTaskLock(taskId);
        }
        return false;
    }


    private void runForTaskClient() {

        leaderSelector.setId(myId);
        LOGGER.info("Starting thisTaskClient selection: " + myId);
        leaderSelector.start();
    }


    public boolean isLeader() {

        return leaderSelector.hasLeadership();
    }


    //获得领导权限后执行
    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        //listenTaskNode();
        LOGGER.info("Mastership participants for thisTaskClient: " + myId + ", " + leaderSelector.getParticipants());

        addTasksListener(new TasksCacheListener());
        addWorkersListener(new WorkersCacheListener());
        workersCache.start();
        tasksCache.start();
        leaderLatch.countDown();
        updateMyTasks();
        tasksCreator();
        LOGGER.info(this.getMyId() + " is leader");
        keepAsLeader();
    }


    private void keepAsLeader() {
        LOGGER.info("now, keep as a leader.");
        try {
            closeLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //对不同的网络连接状态做处理
    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        switch (newState) {
            case CONNECTED:
                LOGGER.info("========= stateChanged: CONNECTED");
                System.out.println("连接成功！");
                LOGGER.info("连接成功！");
                break;
            case RECONNECTED:
                LOGGER.info("========= stateChanged: RECONNECTED");
                System.out.println("正在连接......");
                LOGGER.info("正在连接......");
                break;
            case SUSPENDED:
                LOGGER.info("========= stateChanged: SUSPENDED");
                closeLatch.countDown();
                runForTaskClient();

                break;
            case LOST:
                LOGGER.info("========= stateChanged: LOST");
                try {

                    close();
                    startZK();
                    runForTaskClient();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case READ_ONLY:
                LOGGER.info("========= stateChanged: READ_ONLY");
                LOGGER.info("正在读取内容......");
                break;
        }
    }


    public void startTaskClient() throws Exception {

        this.startZK();
        this.bootsrap();
        this.runForTaskClient();
        this.awaitLeadership();
    }


    @Override
    public void close() throws IOException {

        closeLatch.countDown();
        CloseableUtils.closeQuietly(workersCache);
        CloseableUtils.closeQuietly(tasksCache);
        CloseableUtils.closeQuietly(leaderSelector);
        CloseableUtils.closeQuietly(client);
    }

    /**
     * 获取worsk节点下的所有work节点
     *
     * @return
     * @throws Exception
     */
    public List<String> getWorks() throws Exception {

        List<String> works = client.getChildren().forPath(WORKERS_ROOT_PATH);

        return works;
    }


}
