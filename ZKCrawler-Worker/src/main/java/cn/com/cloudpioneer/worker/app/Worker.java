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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by TianyuanPan on 2016/9/1.
 */

/**
 * Worker 是一个单例，任何时候的任何有关 worker 操作的请求，
 * 只由此一个 work 处理。
 * Worker 启动后，就去 zookeeper 中创建相关节点，并监听任务分配给自己的
 * 情况，然后调用 WebMagic 进行任务爬取，或者终止 WebMagic 的任务爬取。
 */
public class Worker implements Closeable, ConnectionStateListener {

    /**
     * LOGGER 是 slf4j 日志记录成员
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);

    /**
     * thisWorker 是一个静态引用，worker 是单例模式，
     * 其它的引用是通过 thisWorker 完成。
     */
    private volatile static Worker thisWorker;

    /**
     * MY_STATUS 是 worker 在线时，写在其 status 短暂节点中的数据。
     */
    private static final String MY_STATUS = "alive";

    /**
     * ROOT_PATH_TASKS 是 zookeeper 中的任务根节点，节点是永久类型（persistent）。
     */
    public static final String ROOT_PATH_TASKS = "/tasks";

    /**
     * ROOT_PATH_WORKERS 是 zookeeper 中挂载 worker 的根节点，节点是永久类型（persistent）。
     */
    public static final String ROOT_PATH_WORKERS = "/workers";

    /**
     * ROOT_PATH_LOCK 是任务锁节点的根节点，节点是永久类型（persistent）。
     */
    public static final String ROOT_PATH_LOCK = "/lock-4-tasks";

    /**
     * API_CRAWLER_TASK_STARTER 启动爬取任务时，访问的 WebMagic API，存储在配置文件中。
     * API_CRAWLER_TASK_STOPPER 终止爬取任务时，访问的 WebMagic API，存储在配置文件中。
     */
    public static final String API_CRAWLER_TASK_STARTER = ResourceBundle.getBundle("config").getString("API_CRAWLER_TASK_STARTER");
    public static final String API_CRAWLER_TASK_STOPPER = ResourceBundle.getBundle("config").getString("API_CRAWLER_TASK_STOPPER");
    public static final String API_CRAWLER_TASK_CLEAN_R = ResourceBundle.getBundle("config").getString("API_CRAWLER_TASK_CLEAN_R");

    /**
     * worker 自己拥有的所有锁的集合。taskLockMap 存储 worker 当前自己拥有的锁。
     */
    private Map<String, String> taskLockMap;


    /**
     * myTasksLock 是在操作 myTasks 时加的锁。
     */
    private Lock myTasksLock;


    /**
     * myTasks 存储 worker 当前正在进行的爬取任务。
     */
    private MyTasks myTasks;

    /**
     * myTasksCache 是 worker 监听的节点数中的缓存。
     */
    private final TreeCache myTasksCache;


    /**
     * workerId 在 zookeeper 节点中标识 worker 自己，与其他的 worker 节点不同，
     * 也就是说，workerId 就是 work 在路径中的名称。
     * 例如：worker 的 workId 为 w-123456，那么它在路径中如下所示，
     * <p>
     * workers
     * |_____ w-123456 ———— status
     * <p>
     * 其中，status 表名 worker 的状态是在线的。
     */
    private String workerId;

    /**
     * client 是 zookeeper 的客户端，worker 依赖它来完成工作。
     */
    private CuratorFramework client;


    /**
     * myTaskCacheListener 是任务监听器，worker 通过此监听器来发现 zookeeper 节点的变化情况。
     * 根据变化情况的不同，worker 完成不同的操作。
     */
    private TreeCacheListener myTaskCacheListener;


    /**
     *
     */
    private ExecutorService executorService;

    /**
     * 获取 worker 的 workerId，获取方式是
     * 字符串 worker- 加上长度为 10
     * 的随机字符串，该字符串由大、小写字母和数字组成,加上字符 '-'加上当前时间的毫秒数。
     * workId 的示例如下：
     * worker-q78KtWyUpz-1473737224577
     *
     * @return 返回值就是生成的 workerId
     */
    private String getMyId() {

        return ("worker-" + RandomUtils.getRandomString(10)) + "-" + System.currentTimeMillis();
    }


    /**
     * 节点注册方法，需要 worker 注册的节点为：
     * 自己本身，锁根节点，worker 的状态节点。
     *
     * @throws Exception 当注册失败时，抛出异常。
     */
    private void register() throws Exception {

        // 注册锁根节点,永久类型
        if (client.checkExists().forPath(ROOT_PATH_LOCK) == null) {
            client.create().withMode(CreateMode.PERSISTENT).forPath(ROOT_PATH_LOCK);
            LOGGER.info("register znode: " + ROOT_PATH_LOCK + " finished.");
        }

        // 去 workers 节点下注册 worker 节点，持久类型（ephemeral）
        if (client.checkExists().forPath(ROOT_PATH_WORKERS + "/" + workerId) == null) {
            client.create().withMode(CreateMode.PERSISTENT).forPath(ROOT_PATH_WORKERS + "/" + workerId, workerId.getBytes());
            LOGGER.info("register zonode: " + ROOT_PATH_WORKERS + "/" + workerId + " finished.");
        }

        // 去 workers 节点下之间的节点woker 注册状态节点status，短暂类型（persistent）
        if (client.checkExists().forPath(ROOT_PATH_WORKERS + "/" + workerId + "/status") == null) {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(ROOT_PATH_WORKERS + "/" + workerId + "/status", MY_STATUS.getBytes());
            LOGGER.info("register zonode: " + ROOT_PATH_WORKERS + "/" + workerId + "/status" + " finished.");
        }
    }


    /**
     * worker 在工作时，是围绕任务来进行的，因而锁也是围绕任务来建立的。
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
     * 释放 worker 获得的锁。
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
     * workerId 的 get 方法。
     *
     * @return worker 的 id。
     */
    public String getWorkerId() {

        return this.workerId;
    }


    /**
     * 此方法是对任务的数据回写。当任务爬取完成过后，要把相关数据写回 task 节点。
     * <p>
     * 一个任务可以由多个 worker 共同完成，那么就会出现了一个问题，由哪个 worker
     * 来回写数据？
     * <p>
     * 这里的解决方案是，由最后完成工作的那个 worker 来负责任务回写。
     * <p>
     * worker 如何知道自己是否是最后一个完成任务的 worker ？
     * <p>
     * 方法如下：首先，task 节点下的 status 节点里的数据存一个整数，
     * 就是 task 的 worker 数，比如 5，那么当 worker 完成任务后，首先
     * 获取该任务的锁，得到锁过后，读取 task 下的 status 数据，并把数据
     * 减一，看是否为 0，若不为零，则自己不是最后一个完成工作的 worker，那么把 减一
     * 后的数据写入 task 节点下的 status 节点，任务数据不必写回，然后释放锁；若减一
     * 得零了，说明自己是最后一个完成工作的 worker，那么把数据写回 task 节点，把零写
     * 如 task 节点下的 status 节点，然后释放锁。若一开始得不到锁，则继续请求锁，直到
     * 得到锁，然后操作。
     *
     * @param taskId 任务完成并要回写的任务ID
     */
    public void myTaskWriteBack(String taskId) {

        TaskModel task = removeTask(taskId);
        if (task == null) {
            LOGGER.warn("try to write back a null task to the TaskClient, taskId = " + taskId + ", ignore it.");
            return;
        }

        int costTime = (int) ((System.currentTimeMillis() - task.getStartTime()) / 1000 / 60);

        task.getEntity().setTimeStop(new Date());
        task.getEntity().setCostLastCrawl(costTime);
        task.getEntity().setCompleteTimes(task.getEntity().getCompleteTimes() + 1);
        task.getEntity().setStatus(0);

        final TaskModel backTask = task;

        executorService.execute(new Runnable() {
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
                            Thread.sleep(20);
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

                    LOGGER.info("===> count data is: " + count);

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
                            LOGGER.warn("write task status data back to znode: " + backTask.getTaskPath() + "/status Exception. retry ......");
                            retry--;
                            isException = true;
                            continue;
                        }
                        isException = false;
                        LOGGER.info("write task status data back to znode: " + backTask.getTaskPath() + "/status Success.");
                        break;
                    }

                    if (isException) {
                        LOGGER.error("task write back error. release the lock and return.");
                        releaseTaskLock(backTask.getEntity().getId());
                        return;
                    }

                    LOGGER.info("write task data back to TaskClient Success. release the lock and return this thread.");
                    releaseTaskLock(backTask.getEntity().getId());

                    LOGGER.info("===> now, clean WebMagic redis call.");
                    HttpClientUtils.jsonPostRequest(API_CRAWLER_TASK_CLEAN_R + backTask.getEntity().getId(), "[]");

                } else {

                    LOGGER.info("I am not the last worker, count for me is: " + count);
                    retry = 5;

                    while (retry > 0) {

                        try {

                            client.setData().forPath(backTask.getTaskPath() + "/status", ("" + count).getBytes());

                        } catch (Exception e) {

                            LOGGER.warn("write count back to znode: " + backTask.getTaskPath() + "/status Exception. retry ......");
                            retry--;
                            isException = true;
                            continue;
                        }

                        isException = false;
                        LOGGER.info(("write count back to znode: " + backTask.getTaskPath() + "/status Success."));
                        break;
                    }

                    if (isException) {

                        LOGGER.error("write count data back error. release the lock and return this thread.");
                        releaseTaskLock(backTask.getEntity().getId());
                        return;
                    }

                    LOGGER.info("write count data back to TaskClient Success. release the lock.");
                    releaseTaskLock(backTask.getEntity().getId());
                }
            }
        });

    }


    /**
     * worker 的构造函数。
     *
     * @param hostPort    链接 zookeeper 的链接字符串，形式为 主机名:端口号
     * @param retryPolicy 链接的重试策略
     */
    private Worker(String hostPort, RetryPolicy retryPolicy) {

        taskLockMap = new HashMap<>();
        myTasksLock = new ReentrantLock();
        myTasks = new MyTasks();
        workerId = getMyId();
        executorService = Executors.newFixedThreadPool(10);
        LOGGER.info("Worker constructing, hostPort:" + hostPort);
        LOGGER.info("my work id: " + workerId);
        this.client = CuratorFrameworkFactory.newClient(hostPort, retryPolicy);
        this.client.getConnectionStateListenable().addListener(this);
        this.myTasksCache = new TreeCache(this.client, ROOT_PATH_WORKERS + "/" + workerId);
        this.myTaskCacheListener = new MyTaskCacheListener();

    }


    /**
     * worker 的初始化静态方法。在 worker 做任何工作之前，先调此初始化方法。
     * 此方法里调 worker 的构造方法来完成 worker 对象的实例化。
     *
     * @param hostPort    链接 zookeeper 的链接字符串，形式为 主机名:端口号
     * @param retryPolicy 链接的重试策略
     * @return
     */
    public static Worker initializeWorker(String hostPort, RetryPolicy retryPolicy) {

        if (thisWorker == null)
            thisWorker = new Worker(hostPort, retryPolicy);
        return thisWorker;
    }

    /**
     * 添加任务使其运行。此方法最终调用 WebMagic 提供的接口启动 worker 的任务。
     *
     * @param task 要启动的任务
     */
    public void addTaskToRunning(final TaskModel task) {

        addTask(task);
        LOGGER.info("task added: myTasksSize: " + getMyTasksSize());

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    LOGGER.info("=====>> Start request Webmagic.......");
                    LOGGER.info("=====>> task info:\n\t" + task.toString());
                    HttpClientUtils.jsonPostRequest(API_CRAWLER_TASK_STARTER, task.toString());
                    LOGGER.info("=====>> Ended request Webmagic.......");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

            }
        });
    }


    /**
     * 停止正在爬取的任务。此方法最终调用 WebMagic 提供的任务停止接口来停止 worker 的任务
     *
     * @param task
     */
    public void removeTaskInRunning(final TaskModel task) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpClientUtils.jsonPostRequest(API_CRAWLER_TASK_STOPPER + task.getEntity().getId(), task.getEntity().getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * 清理 redis 调用。
         */
        /**
         executorService.execute(new Runnable() {
        @Override public void run() {
        try {
        HttpClientUtils.jsonPostRequest(API_CRAWLER_TASK_CLEAN_R + task.getEntity().getId(), task.getEntity().getId());
        } catch (Exception e) {
        e.printStackTrace();
        }
        }
        });

         **/

    }


    /**
     * 此方法获取节点的数据。
     *
     * @param znode 节点的绝对路径
     * @return 节点的字节数据数组。
     */
    public byte[] getZnodeData(String znode) {

        try {
            return client.getData().forPath(znode);
        } catch (Exception e) {
            LOGGER.warn("get znode data error.");
        }
        return null;
    }


    /**
     * 添加任务到 worker 的任务字典中。
     *
     * @param task 要添加的任务。
     */
    private void addTask(TaskModel task) {

        try {
            myTasksLock.lock();
            myTasks.addTask(task);
        } catch (Exception e) {
            LOGGER.warn("myTasks.addTask Exception...!!!");
        } finally {

            myTasksLock.unlock();
        }

    }

    /**
     * 从 worker 中的任务字典移除任务
     *
     * @param id 任务ID
     * @return
     */
    private TaskModel removeTask(String id) {

        try {
            myTasksLock.lock();
            return myTasks.removeTask(id);
        } catch (Exception e) {
            return null;
        } finally {

            myTasksLock.unlock();
        }
    }


    public boolean containsTask(String taskId) {

        try {
            myTasksLock.lock();
            return myTasks.containsKey(taskId);
        } catch (Exception e) {
            return false;
        } finally {

            myTasksLock.unlock();
        }
    }

    /**
     * 此方法获取 worker 当前的所有任务。
     *
     * @return worker 当前任务的列表。
     */
    public List<TaskModel> getMyTasks() {

        try {
            myTasksLock.lock();

            return myTasks.getTasks();

        } catch (Exception e) {
            return new LinkedList<>();
        } finally {
            myTasksLock.unlock();
        }

    }


    /**
     * 获取 worker 当前的的任务总数。
     *
     * @return 当前任务总数。
     */
    public int getMyTasksSize() {
        try {
            myTasksLock.lock();
            return myTasks.getMyTasksSize();
        } catch (Exception ex) {
            return 0;
        } finally {
            myTasksLock.unlock();
        }
    }


    /**
     * 此方法启动 zookeeper 客户端。
     */
    private void startZK() {

        LOGGER.info("starting zookeeper client.");
        client.start();
        LOGGER.info("zookeeper client started.");
    }

    public void myExecutor(Runnable task) {

        if (!executorService.isShutdown())
            executorService.execute(task);
    }

    /**
     * 此方法调用节点注册方法 register，进行
     * worker 的相关节点的注册。
     */
    private void bootsrap() {

        try {
            register();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("register znode Exception");
        }

    }


    /**
     * 此方法进行任务监听器的添加和启动，监听
     * worker 关心的节点的变化情况。
     */
    private void runForWorker() {

        myTasksCache.getListenable().addListener(myTaskCacheListener);
        try {
            myTasksCache.start();
        } catch (Exception e) {
            LOGGER.warn("myTasksCache starting Exception");
            e.printStackTrace();

        }

    }


    /**
     * 关闭方法，当 worker 退出时，关闭相关成员，
     * 释放资源。此方法为重载其父类的方法。
     * 主要关闭的是任务监听器和 zookeeper 客户端 client。
     *
     * @throws IOException 可能会抛出关闭异常。
     */
    @Override
    public void close() throws IOException {

        LOGGER.info("closing worker ......");
        CloseableUtils.closeQuietly(myTasksCache);
        CloseableUtils.closeQuietly(client);
        LOGGER.info("worker closed.");

    }


    /**
     * 此方法是 worker 的启动方法，worker 初始化好后，
     * 由此方法启动。
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


    /**
     * 此静态方法返回 worker 给外部调用者。
     *
     * @return Worker 实例对象。
     */
    public static synchronized Worker getThisWorker() {

        return Worker.thisWorker;
    }


    /**
     * 此方法是回调方法，当客户端 client 与 zookeeper 服务之间的链接状态
     * 发生改变时，此方法用来处理一些事件。
     *
     * @param curatorFramework
     * @param connectionState
     */
    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {

        switch (connectionState) {

            case CONNECTED:
                LOGGER.info("======== worker connected. ======");
                break;
            case SUSPENDED:
                LOGGER.info("======== worker suspended. =======");
                break;
            case RECONNECTED:
                LOGGER.info("======== worker reconnected. =======");
                bootsrap();
                break;
            case LOST:
                LOGGER.info("======== worker lost. ========");
                break;
            case READ_ONLY:
                LOGGER.info("======== worker read only event. =========");
                break;
        }
    }

}
