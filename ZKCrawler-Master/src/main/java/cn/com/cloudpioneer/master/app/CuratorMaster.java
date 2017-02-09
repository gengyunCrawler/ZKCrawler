package cn.com.cloudpioneer.master.app;

import cn.com.cloudpioneer.master.listener.TasksCacheListener;
import cn.com.cloudpioneer.master.listener.WorkersCacheListener;
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

    /**
     * slf4j 日志记录成员。
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorMaster.class);


    /**
     * CuratorMaster 设计成单实例，此静态变量为提供给外部引用。
     */
    private static volatile CuratorMaster thisMaster;


    /**
     * master 注册时的 ID 号，若初始化时没有指定，那么随机成，生成的方法为：
     * "master-" + "十个有数字和大小写字母组成的随机字符串" + "-" + "master 初始化时的系统当前时间戳"
     */
    private String myId = "master-" + RandomUtils.getRandomString(10) + "-" + System.currentTimeMillis();


    /**
     * 链接 zoookeeper 的客户端。
     */
    private CuratorFramework client;


    /**
     * zookeeper 的主节点选举器成员。
     */
    private LeaderSelector leaderSelector;


    /**
     * 根节点 workers 的树缓存。
     */
    private TreeCache workersCache;


    /**
     * 根节点 tasks 的树缓存。
     */
    private TreeCache tasksCache;


    /**
     * 对/workers节点进行监听的监视器需完成对相应事件的处理
     */
    private TreeCacheListener workersCacheListener = new WorkersCacheListener();
    /**
     * 对tasks进行监听的监视器需完成对相应事件的处理
     */
    private TreeCacheListener tasksCacheListener = new TasksCacheListener();



    /**
     * zookeeper 的链接字符串。
     */
    private String hostPort;


    /**
     * 主节点锁
     */
    private CountDownLatch leaderLatch = new CountDownLatch(1);


    /**
     * 关闭锁
     */
    private CountDownLatch closeLatch = new CountDownLatch(1);


    /**
     * master 初始化方法，角色 master 必须由此方法来进行实例化。
     *
     * @param hostPort   zookeeper 的链接字符串。
     * @param retryPolic zookeeper 链接失败时的重试策略。
     * @param myId       master 的 ID ，可以为 null， 若为空时使用默认值。
     * @return Mater 的对象。
     */
    public static CuratorMaster initializeMaster(String hostPort, RetryPolicy retryPolic, @Nullable String myId) {

        if (thisMaster == null)
            thisMaster = new CuratorMaster(hostPort, retryPolic, myId);
        return thisMaster;
    }


    /**
     * 获取此 master 实例的方法，此方法需在实例化方法对 master 实例化后方可调用，
     * 否则会得到空对象 null。
     *
     * @return Master 的对象。
     */
    public static CuratorMaster getThisMaster() {

        return thisMaster;
    }


    /**
     * Master 的私有构造方法，由静态的初始化方法调用，得到 master 实例。
     *
     * @param myId        master 的 ID ，可以为 null， 若为空时使用默认值。
     * @param hostPort    zookeeper 的链接字符串。
     * @param retryPolicy zookeeper 链接失败时的重试策略。
     */
    private CuratorMaster(String hostPort, RetryPolicy retryPolicy, @Nullable String myId) {
        if (myId != null) {
            this.myId = myId;
        }
        this.hostPort = hostPort;
        this.client = CuratorFrameworkFactory.newClient(hostPort, retryPolicy);
        this.leaderSelector = new LeaderSelector(this.client, ValueDef.PATH_ROOT_MASTER, this);
        this.workersCache = new TreeCache(this.client, ValueDef.PATH_ROOT_WORKERS);
        this.tasksCache = new TreeCache(this.client, ValueDef.PATH_ROOT_TASKS);

    }


    /**
     * 获取 Master 的 zookeeper 链接客户端。
     *
     * @return 此 Master 的 CuratorFramework 的客户端。
     */
    public CuratorFramework getClient() {
        return client;
    }


    /**
     * 获取 Master 的 ID。
     *
     * @return 此 master 的 ID。
     */
    public String getMyId() {

        return this.myId;
    }

    /**
     * 启动连接客户端，进行 zookeeper 的链接。
     */
    public void startZK() throws Exception {
        client.start();

    }


    /**
     * 创建各个角色的根节点，如果它们不存在的话。
     *
     * @throws Exception 创建出错的的时候抛出异常。
     */
    private void bootstrap() throws Exception {

        if (!CuratorUtils.isNodeExist(client, ValueDef.PATH_ROOT_MASTER)) {
            client.create().forPath(ValueDef.PATH_ROOT_MASTER, new byte[0]);
        }

        if (!CuratorUtils.isNodeExist(client, ValueDef.PATH_ROOT_WORKERS)) {
            client.create().forPath(ValueDef.PATH_ROOT_WORKERS, new byte[0]);
        }

        if (!CuratorUtils.isNodeExist(client, ValueDef.PATH_ROOT_TASKS)) {
            client.create().forPath(ValueDef.PATH_ROOT_TASKS, new byte[0]);
        }

    }

    /**
     * 启动 master，将会启动主节点选择器并进行主节点的选举。
     */
    private void runForMaster() throws Exception {

        leaderSelector.setId(myId);
        LOGGER.info("Starting master selection: " + myId);
        leaderSelector.start();
    }


    /**
     * 为 workersCache 添加监听器。
     *
     * @param workersListener workers 节点的监听器。
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


    /**
     * 查看此 master 是否是主节点权限。
     *
     * @return true 为主节点，false 非主节点。
     */
    public boolean isLeader() {
        return leaderSelector.hasLeadership();
    }


    CountDownLatch recoveryLatch = new CountDownLatch(0);


    /**
     * 阻塞当前方法方法退出，用在 master 获得主节点权限后，保持权限，直到 close 调用。
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
     * 选举 leader 成功后执行该方法，在此方法中启动对/zkcrawler_workers, /zkcrawler_tasks的监听，并进行任务分配。
     * 若退出此方法，则为放弃主节点权限。
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

        checkWorkers();

        //保持主节点权限。
        keepAsLeader();

    }


    /**
     * curatorFramework 与 zookeeper 的网络连接状态在这里根据不同状态进行处理，比如网络连接异常。
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
                LOGGER.warn("Session suspended");
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


    @Override
    public void close() throws IOException {
        closeLatch.countDown();
        CloseableUtils.closeQuietly(tasksCache);
        CloseableUtils.closeQuietly(workersCache);
        CloseableUtils.closeQuietly(leaderSelector);
        CloseableUtils.closeQuietly(client);
    }



    /**
     * 当leader挂掉从新启动时得再次确定task是否已经挂载到了具体的worker下面
     *
     * @throws Exception
     */
    private void recoverTask() throws Exception {
        List<String> tasks = client.getChildren().forPath(ValueDef.PATH_ROOT_TASKS);
        for (String task : tasks) {
            List<String> workers = client.getChildren().forPath(ValueDef.PATH_ROOT_TASKS.concat("/").concat(task));
            for (String worker : workers) {
                CuratorUtils.task4worker(client, task, worker);
            }
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


    /**
     * 获取该节点下的所有子节点
     *
     * @param znode
     * @return List<String>
     * @throws Exception
     */
    public List<String> getChildren(String znode) throws Exception {
        return client.getChildren().forPath(znode);
    }

    /**
     * 获取该节点下的数据
     *
     * @param znode
     * @return
     * @throws Exception
     */
    public byte[] getNodeData(String znode) throws Exception {
        if (CuratorUtils.isNodeExist(client, znode)) {
            return client.getData().forPath(znode);
        }
        return null;
    }


    /**
     * 检查 worker 的有效性，若 worker 无效，则把它删除。
     *
     * @throws Exception
     */
    private void checkWorkers() throws Exception {

        LOGGER.info("check workers ...");
        List<String> workers = getChildren(ValueDef.PATH_ROOT_WORKERS);
        if (workers == null || workers.size() == 0) {
            LOGGER.info("not have invalid workers.");
            return;
        }

        for (String item : workers) {

            if (!CuratorUtils.isHaveSpecificChild(this.client, ValueDef.PATH_ROOT_WORKERS + "/" + item, "status")) {
                LOGGER.info("====> clean invalid worker: " + ValueDef.PATH_ROOT_WORKERS + "/" + item);
                CuratorUtils.deletePathAndChildren(this.client, ValueDef.PATH_ROOT_WORKERS + "/" + item);
            }
        }

    }

}
