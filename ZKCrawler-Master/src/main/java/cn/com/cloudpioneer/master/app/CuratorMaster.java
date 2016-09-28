package cn.com.cloudpioneer.master.app;

import cn.com.cloudpioneer.master.utils.CuratorUtils;
import cn.com.cloudpioneer.master.utils.RandomUtils;
import com.sun.istack.internal.Nullable;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

/**
 * Created by Tijun on 2016/9/1.
 *
 * @author TijunWang
 * @version 1.0
 */
public class CuratorMaster implements Closeable, LeaderSelectorListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorMaster.class);

    private static volatile CuratorMaster thisMaster;

    //serverId
    private String myId = "master-" + RandomUtils.getRandomString(10) + "-" + System.currentTimeMillis();

    //连接zk的client
    private CuratorFramework client;

    //进行leader选举
    private LeaderSelector leaderSelector;

    //对/workers节点进行监听的workersCache
    private TreeCache workersCache;

    //对/tasks节点进行监听的tasksCache
    private TreeCache tasksCache;


    private static final String PATH_ROOT_TASKS = "/tasks";

    private static final String PATH_ROOT_MASTER = "/master";
    private static final String PATH_ROOT_WORKERS = "/workers";


    private final static Pattern TASK_WORKER = Pattern.compile(PATH_ROOT_TASKS + "/task-.*/worker-.*");


    private String hostPort;


    private CountDownLatch leaderLatch = new CountDownLatch(1);
    private CountDownLatch closeLatch = new CountDownLatch(1);


    public static CuratorMaster initializeMaster(String hostPort, RetryPolicy retryPolic, @Nullable String myId) {

        if (thisMaster == null)
            thisMaster = new CuratorMaster(hostPort, retryPolic, myId);
        return thisMaster;
    }

    public static CuratorMaster getThisMaster() {

        return thisMaster;
    }

    /**
     * 初始化client连接配置参数
     *
     * @param myId
     * @param hostPort
     * @param retryPolicy
     */
    private CuratorMaster(String hostPort, RetryPolicy retryPolicy, @Nullable String myId) {
        if (myId != null) {
            this.myId = myId;
        }
        this.hostPort = hostPort;
        this.client = CuratorFrameworkFactory.newClient(hostPort, retryPolicy);
        this.leaderSelector = new LeaderSelector(this.client, PATH_ROOT_MASTER, this);
        this.workersCache = new TreeCache(this.client, PATH_ROOT_WORKERS);
        this.tasksCache = new TreeCache(this.client, PATH_ROOT_TASKS);

    }

    public CuratorFramework getClient() {
        return client;
    }

    /**
     * 启动连接
     */
    public void startZK() throws Exception {
        client.start();

    }

    /**
     * 初始化节点如果节点不存在择创建节点
     *
     * @throws Exception
     */
    public void bootstrap() throws Exception {
        if (!isNodeExist(PATH_ROOT_MASTER)) {
            client.create().forPath(PATH_ROOT_MASTER, new byte[0]);
        }
        if (!isNodeExist(PATH_ROOT_WORKERS)) {
            client.create().forPath(PATH_ROOT_WORKERS, new byte[0]);
        }
        if (!isNodeExist(PATH_ROOT_TASKS)) {
            client.create().forPath(PATH_ROOT_TASKS, new byte[0]);
        }

    }

    /**
     * 启动master，将会启动对/workers,/tasks节点的监听
     *
     * @return
     */
    public CuratorFramework runForMaster() throws Exception {
        leaderSelector.setId(myId);
        LOGGER.info("Starting master selection: " + myId);
        leaderSelector.start();
        return client;
    }


    /**
     * 为workersCache 添加监视器
     *
     * @param workersListener
     */
    public void addWorkersListener(TreeCacheListener workersListener) {
        this.workersCache.getListenable().addListener(workersListener);
    }

    /**
     * 为tasksCache 添加监视器
     *
     * @param tasksListener
     */
    public void addTasksListener(TreeCacheListener tasksListener) {
        this.tasksCache.getListenable().addListener(tasksListener);
    }

    /**
     * 等待选举结束
     *
     * @throws InterruptedException
     */
    public void awaitLeadership() throws InterruptedException {
        leaderLatch.await();
    }

    public boolean isLeader() {
        return leaderSelector.hasLeadership();
    }


    CountDownLatch recoveryLatch = new CountDownLatch(0);

    /**
     * 选举leader成功后执行该方法，在此方法中启动对/workers, /tasks的监听，并在/assign下进行进行任务分配
     */
    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
        //之前无法进入此方法的原因是受log4j的影响
        //如果当当前选举的master挂掉之后会进行重新选举，此时当前的master得再次发现之前可用的worker和tasks然后对必要的未将
        recoverTask();

        LOGGER.info("Mastership participants: " + myId + ", " + leaderSelector.getParticipants());

        addWorkersListener(workersCacheListener);
        addTasksListener(tasksCacheListener);

        workersCache.start();
        tasksCache.start();
        leaderLatch.countDown();

        keepAsLeader();

    }

    /**
     * 阻塞当前线程方法，保证监听一直监听下去，指导下达close命令。
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
     * curatorFramework 与zkserver的网络连接状态在这里根据不同状态进行处理，比如网络连接异常
     *
     * @param curatorFramework
     * @param connectionState
     */
    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
        switch (connectionState) {
            case CONNECTED:
                //Nothing to do in this case.

                break;
            case RECONNECTED:
                // Reconnected, so I should
                // still be the leader.

                break;
            case SUSPENDED:
                LOGGER.warn("Session suspended");//
                try {
                    closeLatch.countDown();
                    runForMaster();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case LOST:
                try {
                    close();
                    startZK();
                    runForMaster();

                } catch (Exception e) {
                    LOGGER.warn("Exception while closing", e);
                }

                break;
            case READ_ONLY:
                // We ignore this case

                break;
        }
    }


    /**
     * 检查节点是否存在
     *
     * @return
     */
    public boolean isNodeExist(String node) throws Exception {
        Stat stat = client.checkExists().forPath(node);
        if (stat != null) {
            return true;
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        closeLatch.countDown();
        CloseableUtils.closeQuietly(tasksCache);
        CloseableUtils.closeQuietly(workersCache);
        CloseableUtils.closeQuietly(leaderSelector);
        CloseableUtils.closeQuietly(client);
    }

    /**
     * 对/workers节点进行监听的监视器需完成对相应事件的处理
     */
    private TreeCacheListener workersCacheListener = new TreeCacheListener() {

        String nodePath = "";

        public void childEvent(CuratorFramework client, TreeCacheEvent event) {
            switch (event.getType()) {
                case NODE_ADDED:
                    nodePath = event.getData().getPath();
                    LOGGER.info("===> NODE_ADDED Event, path: " + nodePath);

                   /*
                    Pattern workers = Pattern.compile(PATH_ROOT_WORKERS + "/worker-.*");
                    Pattern workerStatus = Pattern.compile(PATH_ROOT_WORKERS + "/worker-.*//*status");


                    if (workers.matcher(nodePath).matches() && !workerStatus.matcher(nodePath).matches()) {
                        LOGGER.info("===> check workers.");
                        try {
                            if (!CuratorUtils.isHaveChildren(client, nodePath))
                                CuratorUtils.deletePath(client, nodePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    */

                    break;
                case NODE_UPDATED:
                    nodePath = event.getData().getPath();
                    LOGGER.info("NODE_UPDATED:" + nodePath);
                    break;
                case NODE_REMOVED:

                    nodePath = event.getData().getPath();

                    LOGGER.info("NODE_REMOVED:" + nodePath);
                    try {
                        //删除过期的worker节点
                        String path = nodePath.replace("/status", "");
                        LOGGER.info("将删除:" + path);
                        if (isNodeExist(path)) {
                            CuratorUtils.deletePathAndChildren(client, path);
                        }
                        //
                        LOGGER.info("已将删除:" + path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //  Pattern pattern=Pattern.compile(PATH_ROOT_WORKERS.concat("/").concat())
                    break;

            }


        }
    };


    /**
     * 对tasks进行监听的监视器需完成对相应事件的处理
     */
    private TreeCacheListener tasksCacheListener = new TreeCacheListener() {

        String child = null;

        public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {

            switch (event.getType()) {

                case NODE_ADDED:

                    child = event.getData().getPath();
                    LOGGER.info("===> taskCacheListener NODE_ADDED Event.");
                    LOGGER.info("Event Path:" + child);
                    //先判增加的节点是task或者task-*/worker?
                    //节点为/tasks/task-*/worker-* 类型
                    if (TASK_WORKER.matcher(child).matches()) {
                        String[] arr = child.split("/");
                        String taskId = arr[2];
                        String workerId = arr[3];
                        task4worker(taskId, workerId);

                    }

                    break;

                case NODE_REMOVED:
                    child = event.getData().getPath();
                    LOGGER.info("===> taskCacheListener NODE_ADDED Event.");
                    LOGGER.info("===> Event Path: " + child);
                    //先判增加的节点是task或者task-*/worker?
                    //节点为/tasks/task-*/worker-* 类型
                    if (TASK_WORKER.matcher(child).matches()) {
                        String[] arr = child.split("/");
                        String worker = PATH_ROOT_WORKERS + "/" + arr[3];
                        String task = arr[2];
                        LOGGER.info("===> removing znode: " + worker + "/" + task);
                        CuratorUtils.deletePathAndChildren(client, worker + "/" + task);
                    }
                    break;

                case NODE_UPDATED:
                    LOGGER.info("update");
                    break;
                default:

                    break;
            }
        }
    };

    /**
     * 当leader挂掉从新启动时得再次确定task是否已经挂载到了具体的worker下面
     *
     * @throws Exception
     */
    private void recoverTask() throws Exception {
        List<String> tasks = client.getChildren().forPath(PATH_ROOT_TASKS);
        for (String task : tasks) {
            List<String> workers = client.getChildren().forPath(PATH_ROOT_TASKS.concat("/").concat(task));
            for (String worker : workers) {
                task4worker(task, worker);
            }
        }
    }

    /**
     * 将task复制到相应的worker目录下
     *
     * @param taskId
     * @param workerId
     */
    private void task4worker(String taskId, String workerId) {
        try {
            //task下的配置信息
            byte[] taskData = client.getData().forPath(PATH_ROOT_TASKS + "/" + taskId);
            //判断worker是否存在
            Stat stat = client.checkExists().forPath(PATH_ROOT_WORKERS + "/" + workerId);
            if (stat != null) {
                //将任务挂载到worker下面

                String task4workerPath = PATH_ROOT_WORKERS.concat("/").concat(workerId).concat("/").concat(taskId);

                if (!isNodeExist(task4workerPath)) {
                    client.create().withMode(CreateMode.PERSISTENT).forPath(task4workerPath, taskData);
                }

            } else {
                //当任务分配要挂载的worker不存在时删除具体任务下的worker
                client.delete().forPath(PATH_ROOT_TASKS.concat("/").concat(taskId).concat("/").concat(workerId));
            }

        } catch (Exception e) {

        }
    }

    /**
     * 启动master
     *
     * @throws Exception
     */
    public void startMaster() throws Exception {
        this.startZK();
        this.bootstrap();
        this.runForMaster();
        this.awaitLeadership();
    }

    /**
     * 关闭master
     *
     * @throws IOException
     */
    public void stopMaster() throws IOException {
        close();
    }

}
