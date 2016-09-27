package cn.com.cloudpioneer.taskclient.app;

import cn.com.cloudpioneer.taskclient.chooser.ChooserPolicy;
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


    /**
     * 日志记录成员。
     */
    private static Logger LOGGER = LoggerFactory.getLogger(TaskClient.class);

    /**
     * TaskClient 是单例模式，此静态变量提供给外部对 TaskClient 的引用。
     */
    private static volatile TaskClient thisTaskClient;


    /**
     * 任务定时器，用于定时的从数据库中扫描任务。
     */
    private Timer tasksTimer;


    /**
     * TaskClient 的相关配置。
     */
    private Map<String, Object> configs;


    /**
     * TaskClient 的 ID，随机生成。
     */
    private String myId;


    /**
     * zookeeper 链接字符串。
     */
    private String hostPort;

    /**
     * zookeeper 客户端对象。
     */
    private CuratorFramework client;

    /**
     * 主节点选择器。
     */
    private LeaderSelector leaderSelector;

    /**
     * 任务缓存成员。
     */
    private TreeCache tasksCache;

    /**
     * 工作者缓存成员。
     */
    private TreeCache workersCache;


    /**
     * TaskClient 当前的所有任务集合。
     */
    private Map<String, TaskModel> myTasks;


    /**
     * 访问当前任务集合 myTasks 的独占锁。
     */
    private Lock lock4MyTasks;


    /**
     * 任务选择器成员，提供数据库任务选择的策略。
     */
    private Chooser tasksChooser;


    /**
     * 执行爬取任务的工作节点调度器。
     */
    private Scheduler workersScheduler;


    /**
     * 线程执行服务成员。
     */
    private ExecutorService executorService;

    /**
     * 主节点锁和等待关闭锁。
     * 当节点选举成为主节点后，由等待关闭锁来保持主节点的权利。
     */
    private CountDownLatch leaderLatch = new CountDownLatch(1);
    private CountDownLatch closeLatch = new CountDownLatch(1);


    /**
     * 数据库任务扫描定时器的扫描间隔，单位为秒。
     */
    private static int tasksScannerPeriod = 2;


    /**
     * thisTaskClient 自己拥有的所有锁的集合。taskLockMap 存储 worker 当前自己拥有的锁。
     */
    private Map<String, String> taskLockMap;


    /**
     * 以下常量为 zookeeper 里的相关根节点。
     */
    public static final String ROOT_PATH_LOCK = "/lock-4-tasks";
    public static final String ROOT_PATH_TASKS = "/tasks";
    public static final String ROOT_PATH_WORKERS = "/workers";
    public static final String ROOT_PATH_CLIENT = "/TASK_CLIENT";


    /**
     * 初始化扫描器间隔变量，从配置文件中取得，若取值出错则初始化默认值。
     */
    static {
        try {
            tasksScannerPeriod = Integer.parseInt(ResourceBundle.getBundle("config").getString("TASKS_SCANNER_PERIOD"));
        } catch (NumberFormatException e) {
            tasksScannerPeriod = 2;
            LOGGER.warn("get tasksScannerPeriod Exception, use the default value: " + tasksScannerPeriod + " minutes.");
        }
    }

    /**
     * 设置任务扫描器扫描间隔时间。
     *
     * @param tasksScannerPeriod
     */
    public static void setTasksScannerPeriod(int tasksScannerPeriod) {

        TaskClient.tasksScannerPeriod = tasksScannerPeriod;
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
            client.create().withMode(CreateMode.EPHEMERAL).forPath(ROOT_PATH_LOCK + "/" + taskId);
        } catch (Exception e) {
            LOGGER.warn("get lock: " + taskId + " Failed. return false.");
            return false;
        }

        taskLockMap.put(taskId, ROOT_PATH_LOCK + "/" + taskId);
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
        } else {
            LOGGER.warn("try to release a not exists lock, ignore it ......");
        }

    }


    /**
     * 初始化 TaskClient 实例，由于是单例模式，需由此静态方法初始化取得实例。
     *
     * @param hostPort       zookeeper 的链接字符串。
     * @param retryPolicy    zookeeper 链接失败时的重试策略。
     * @param myId           TaskClient 的ID， 可以为 null。
     * @param chooserPolicy  任务选择器的选择策略，可以为 null，为 null 时将使用默认值。
     * @param configFileName TaskClient 的配置文件，可以为 null，为 null 时将使用默认值。
     * @return 返回 TaskClient 实例。
     */
    public static TaskClient initializeTaskClient(String hostPort, RetryPolicy retryPolicy, @Nullable String myId, @Nullable SchedulePolicy schedulePolicy, @Nullable ChooserPolicy chooserPolicy, @Nullable String configFileName) {

        if (thisTaskClient == null)
            thisTaskClient = new TaskClient(hostPort, retryPolicy, myId, schedulePolicy, chooserPolicy, configFileName);
        return thisTaskClient;

    }


    /**
     * 获取 TaskClient 的实例，此方法需在初始化方法执行后方可调用，否则将得到 null。
     *
     * @return 返回 TaskClient 实例。
     */
    public static TaskClient getThisTaskClient() {
        return thisTaskClient;
    }


    /**
     * TaskClient 的私有构造方法。
     *
     * @param hostPort       zookeeper 的链接字符串。
     * @param retryPolicy    zookeeper 链接失败时的重试策略。
     * @param myId           TaskClient 的ID， 可以为 null。
     * @param chooserPolicy  任务选择器的选择策略，可以为 null，为 null 时将使用默认值。
     * @param configFileName TaskClient 的配置文件，可以为 null，为 null 时将使用默认值。
     */
    private TaskClient(String hostPort, RetryPolicy retryPolicy, @Nullable String myId, @Nullable SchedulePolicy schedulePolicy, @Nullable ChooserPolicy chooserPolicy, @Nullable String configFileName) {

        this.tasksTimer = new Timer();
        this.myTasks = new HashMap<>();
        this.taskLockMap = new HashMap<>();
        this.executorService = Executors.newFixedThreadPool(10);
        this.lock4MyTasks = new ReentrantLock();
        this.configs = initConfigs(configFileName);
        this.tasksChooser = new Chooser();

        if (myId != null)
            this.myId = myId;
        else
            this.myId = "TaskClient-" + RandomUtils.getRandomString(10) + "-" + System.currentTimeMillis();

        if (chooserPolicy != null)
            this.tasksChooser.setPolicy(chooserPolicy);


        this.workersScheduler = new Scheduler();

        if (schedulePolicy != null)
            this.workersScheduler.setPolicy(schedulePolicy);

        this.hostPort = hostPort;
        this.client = CuratorFrameworkFactory.newClient(this.hostPort, retryPolicy);
        this.leaderSelector = new LeaderSelector(this.client, ROOT_PATH_CLIENT, this);
        this.workersCache = new TreeCache(this.client, ROOT_PATH_WORKERS);
        this.tasksCache = new TreeCache(this.client, ROOT_PATH_TASKS);

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


    /**
     * 提供给外部调用，用于执行一些异步操作。
     *
     * @param task
     */
    public void myExecutor(Runnable task) {

        if (!executorService.isShutdown())
            executorService.execute(task);
    }

    /**
     * 启动 zookeeper 客户端。
     *
     * @throws Exception
     */
    private void startZK() throws Exception {

        client.start();
    }


    /**
     * 从集合中移除任务。
     *
     * @param taskId
     * @return
     */
    private TaskModel removeMyTasks(String taskId) {

        try {
            lock4MyTasks.lock();
            return myTasks.remove(taskId);
        } finally {
            lock4MyTasks.unlock();
        }
    }


    /**
     * 加入任务到集合中。
     *
     * @param taskId
     * @param taskModel
     */
    private void addToMyTasks(String taskId, TaskModel taskModel) {


        try {
            lock4MyTasks.lock();
            myTasks.put(taskId, taskModel);
        } finally {
            lock4MyTasks.unlock();
        }
    }


    /**
     * 获取集合中任务的数量。
     *
     * @return
     */
    public int getMyTasksSize() {

        try {
            lock4MyTasks.lock();
            return myTasks.size();
        } finally {
            lock4MyTasks.unlock();
        }
    }


    /**
     * 当 TaskClient 获得主节点权限后，
     * 要调用此方法从 zookeeper 中更新任务状态。
     */
    private void updateMyTasks() {

        try {
            List<String> tasks = client.getChildren().forPath(ROOT_PATH_TASKS);
            for (String item : tasks) {
                List<String> workers = client.getChildren().forPath(ROOT_PATH_TASKS + "/" + item);
                for (int i = 0; i < workers.size(); i++) {
                    String s = workers.get(i);
                    if (workers.contains(s)) {
                        workers.remove(s);
                        i--;
                    }
                }
                TaskEntity entity = JSON.parseObject(new String(client.getData().forPath(ROOT_PATH_TASKS + "/" + item), "UTF-8"), TaskEntity.class);
                String statusData = new String(client.getData().forPath(ROOT_PATH_TASKS + "/" + item + "/status"), "UTF-8");
                addToMyTasks(entity.getId(), new TaskModel(ROOT_PATH_TASKS + "/" + item, entity, statusData, workers));
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
     * 主节点权限选举
     *
     * @throws InterruptedException
     */
    private void awaitLeadership() throws InterruptedException {
        leaderLatch.await();
    }


    /**
     * 初始化配置文件。
     *
     * @param configFileName
     * @return
     */
    private Map<String, Object> initConfigs(@Nullable String configFileName) {
        Map<String, Object> configs = new HashMap<>();
        if (configFileName == null) {
            configs.put(ConfigItem.MAX_RUNNING_TASKS_SIZE, Integer.parseInt(ResourceBundle.getBundle("config").getString("MAX_RUNNING_TASKS_SIZE")));
            configs.put(ConfigItem.DEFAULT_TASK_CHOOSER, ResourceBundle.getBundle("config").getString("DEFAULT_TASK_CHOOSER"));
        }
        return configs;
    }


    /**
     * 设置配置文件
     *
     * @param configFileName
     */
    public void setConfig(String configFileName) {

        configs = initConfigs(configFileName);
    }


    /**
     * 获取配置项值。
     *
     * @param key 配置项名称。
     * @return 返回配置项对象。
     */
    public Object getConfig(String key) {
        return configs.get(key);
    }


    /**
     * 获取 TaskClient 的 ID。
     *
     * @return 返回 TaskClient 的 ID 。
     */
    public String getMyId() {
        return myId;
    }


    /**
     * 设置 TaskClient 的 ID。
     *
     * @param myId TaskClient 的 ID。
     */
    public void setMyId(String myId) {
        this.myId = myId;
    }


    /**
     * 初始化相关节点。
     */
    private void bootsrap() {

        try {
            if (client.checkExists().forPath(ROOT_PATH_LOCK) == null) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(ROOT_PATH_LOCK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取任务节点的信息。
     *
     * @param taskPath 任务节点路径
     * @return 任务实体 TaskEntity 对象。
     */
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


    /**
     * 获取任务状态信息
     *
     * @param statusPath 状态节点路径。
     * @return 当前还正在工作的节点数量。
     */
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


    /**
     * 任务更新处理。
     *
     * @param taskStatusPath 任务信息节点路径。
     * @return 更新状态，当任务已经完成并且更新成功返回 true，其他情况返回 false。
     */
    public boolean taskUpdateProcess(String taskStatusPath) {

        String taskPath = ROOT_PATH_TASKS + "/" + taskStatusPath.split("/")[2];
        if (getTaskStatusData(taskStatusPath) == 0) {
            TaskEntity entity = getTaskData(taskPath);
            if (entity == null) {
                return false;
            }
            LOGGER.info("===> taskUpdateProcess get task data:\n\t" + entity.toString());
            removeMyTasks(entity.getId());
            taskWriteBack(entity);
            taskDelete(taskPath);

            return true;
        }

        return false;

    }


    /**
     * 从数据库中获取任务的方法。
     *
     * @param size
     * @return
     */
    private List<TaskEntity> tasksGeter(int size) {
        if (size <= 0)
            return new ArrayList<>();
        return this.tasksChooser.chooser(size);
    }


    public Chooser getTasksChooser() {
        return tasksChooser;
    }

    public Scheduler getWorkersScheduler() {
        return workersScheduler;
    }




    /**
     * 创建爬取任务。
     *
     * @throws Exception
     */
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

        final Map<TaskEntity, List<String>> twMap = workersScheduler.scheduleProcess(taskEntities, workers);

        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry item : twMap.entrySet()) {

                    String taskId;
                    taskId = ((TaskEntity) item.getKey()).getId();

                    while (!isGetTaskLock(taskId)) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    TaskModel taskModel = new TaskModel();

                    try {

                        List<String> ws = (List<String>) item.getValue();

                        taskModel = new TaskModel(ROOT_PATH_TASKS + "/task-" + taskId, ((TaskEntity) item.getKey()), "" + ws.size(), ws);
                        taskModel.setTaskStatus(TaskStatusItem.TASK_STATUS_RUNNING);

                        client.create().withMode(CreateMode.PERSISTENT).forPath(ROOT_PATH_TASKS + "/task-" + taskId, taskModel.getEntity().toString().getBytes());
                        for (String w : ws) {
                            client.create().withMode(CreateMode.PERSISTENT).forPath(ROOT_PATH_TASKS + "/task-" + taskId + "/" + w, "".getBytes());
                        }
                        client.create().withMode(CreateMode.PERSISTENT).forPath(ROOT_PATH_TASKS + "/task-" + taskId + "/status", ("" + ws.size()).getBytes());

                        addToMyTasks(taskId, taskModel);
                        taskWriteBack(taskModel.getEntity());

                    } catch (Exception ex) {

                        LOGGER.error("--++--++--> task create error, taskId = " + taskId);
                        taskDelete(ROOT_PATH_TASKS + "/task-" + taskId);
                        taskModel.getEntity().setStatus(TaskStatusItem.TASK_STATUS_COMPLETED);
                        taskWriteBack(taskModel.getEntity());

                    } finally {
                        releaseTaskLock(taskId);
                    }
                }
            }
        });

    }


    /**
     * 任务扫描器。
     *
     * @throws Exception
     */
    private void tasksScanner() throws Exception {
        if (!isLeader()) {
            LOGGER.info("I am not the leader, don't need to create tasks, my id is: " + myId);
            return;
        }
        LOGGER.info("I am the leader, my id is: " + myId);
        tasksCreator();
    }


    /**
     * 任务写回数据库方法。
     *
     * @param taskEntity 任务实体对象。
     * @return
     */
    private int taskWriteBack(TaskEntity taskEntity) {

        return new TaskDao().updateTaskEntityById(taskEntity);
    }


    /**
     * 删除 tasks下已完成的任务节点
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


    /**
     * 开始进行主节点权限的选举。
     */
    private void runForTaskClient() {

        leaderSelector.setId(myId);
        LOGGER.info("Starting thisTaskClient selection: " + myId);
        leaderSelector.start();
    }


    /**
     * 看此 TaskClient 是否为主节点。
     *
     * @return 是主节点返回 true， 否则返回 false。
     */
    public boolean isLeader() {

        return leaderSelector.hasLeadership();
    }


    /**
     * 获取主节点权限后，开始进行 TaskClient 的任务操作。
     * 此方法是在获取主节点权限后执行，并一直阻塞在此方法中，
     * 只有在退出或释放主节点权限时，才能退出此方法。
     *
     * @param client zookeeper 客户端。
     * @throws Exception
     */
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


    /**
     * 保持主节点权限的阻塞方法。
     */
    private void keepAsLeader() {
        LOGGER.info("now, keep as a leader.");
        try {
            closeLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 网络连接状态的事件处理方法。
     *
     * @param client
     * @param newState
     */
    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        switch (newState) {
            case CONNECTED:
                LOGGER.info("========= stateChanged: CONNECTED");
                LOGGER.info("连接成功！");
                break;
            case RECONNECTED:
                LOGGER.info("========= stateChanged: RECONNECTED");
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


    /**
     * 启动 TaskClient 的方法。
     *
     * @throws Exception
     */
    public void startTaskClient() throws Exception {

        this.startZK();
        this.bootsrap();
        this.runForTaskClient();
        this.awaitLeadership();
    }


    /**
     * 关闭 TaskClient 的方法。
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {

        closeLatch.countDown();
        CloseableUtils.closeQuietly(workersCache);
        CloseableUtils.closeQuietly(tasksCache);
        CloseableUtils.closeQuietly(leaderSelector);
        CloseableUtils.closeQuietly(client);
    }


    /**
     * 获取 works 节点下的所有 work 节点
     *
     * @return worker 节点的路径列表。
     * @throws Exception
     */
    public List<String> getWorks() throws Exception {

        List<String> works = client.getChildren().forPath(ROOT_PATH_WORKERS);

        return works;
    }


}
