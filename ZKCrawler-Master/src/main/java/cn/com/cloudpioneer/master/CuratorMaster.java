package cn.com.cloudpioneer.master;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Tijun on 2016/9/1.
 * @author  TijunWang
 * @version 1.0
 */
public class CuratorMaster implements Closeable,LeaderSelectorListener
{
    private static final Logger LOG= LoggerFactory.getLogger(CuratorMaster.class);

    //serverId
    private String myId="masterId-"+new Random().nextInt();

    //连接zk的CuratorClient
    private CuratorFramework client;

    //进行leader选举
    private LeaderSelector leaderSelector;

    //对/workers节点进行监听的PathChildrenCache
    private PathChildrenCache workersCache;

    //对/tasks节点进行监听的PathChildrenCache
    private PathChildrenCache tasksCache;

    private String hostPort;

    CountDownLatch leaderLatch;

    /**
     *初始化client连接配置参数
     * @param myId
     * @param hostPort
     * @param retryPolicy
     */
    public CuratorMaster(String myId,String hostPort,RetryPolicy retryPolicy){
        if (myId!=null){
            this.myId=myId;
        }
        this.hostPort=hostPort;
        this.client= CuratorFrameworkFactory.newClient(hostPort, 16000, 16000, retryPolicy);

        this.leaderSelector = new LeaderSelector(this.client, "/master", this);
        this.workersCache = new PathChildrenCache(this.client, "/workers", true);
        this.tasksCache = new PathChildrenCache(this.client, "/tasks", true);

    }

    public CuratorFramework getClient(){
        return client;
    }
    /**
     * 启动连接
     */
    public void startZK(){
        LOG.info("zk Starting");
        client.start();
        LOG.info("started zk");
    }

    /**
     * 初始化节点如果节点不存在择创建节点
     * @throws Exception
     */
    public void bootstrap() throws Exception {

        client.create().forPath("/workers", new byte[0]);
        client.create().forPath("/assign", new byte[0]);
        client.create().forPath("/tasks", new byte[0]);
    }

    /**
     * 启动master，将会启动对/workers,/tasks节点的监听，如果监听到有可用的/tasks和可用/workers就会进行任务分配
     * @return
     */
    public CuratorFramework runForMaster() {
        leaderSelector.setId(myId);
        LOG.info("Starting master selection: " + myId);
        //leaderSelector.autoRequeue();
        leaderSelector.start();
        return client;
    }

    /**
     * 对多个任务进行分配
     */

    void assignTasks (List<String> tasks)
            throws Exception {
        for(String task : tasks) {
            assignTask(task, client.getData().forPath("/tasks/" + task));
        }
    }

    /**
     * 对单个任务进行分配 这里要调用分配策略
     * @param task
     * @param data
     * @throws Exception
     */
    void assignTask (String task, byte[] data)
            throws Exception {
        /*
         * Choose worker at random.
         */
        //String designatedWorker = workerList.get(rand.nextInt(workerList.size()));
        List<ChildData> workersList = workersCache.getCurrentData();
        Random rand = new Random(System.currentTimeMillis());
        LOG.info("Assigning task {}, data {}", task, new String(data));
        String designatedWorker;
        if (workersList!=null&&workersList.size()>0){
            designatedWorker = workersList.get(rand.nextInt(workersList.size())).getPath().replaceFirst("/workers/", "");
        }else {return;}


        /*
         * Assign task to randomly chosen worker.
         */
        String path = "/assign/" + designatedWorker + "/" + task;
        createAssignment(path, data);
    }

    /**
     * 为workersCache 添加监视器
     * @param workersListener
     */
    public void  addWorkersListener(PathChildrenCacheListener workersListener){
        this.workersCache.getListenable().addListener(workersListener);
    }

    /**
     * 为tasksCache 添加监视器
     * @param tasksListener
     */
    public void  addTasksListener(PathChildrenCacheListener tasksListener){
        this.workersCache.getListenable().addListener(tasksListener);
    }
    /**
     * 任务删除
     * @param task
     */
    public void deleteTask(String task) throws Exception
    {
        client.delete().forPath(task);
    }

    /**
     * Creates an assignment.
     *
     * @param path
     *          path of the assignment
     */
    void createAssignment(String path, byte[] data)
            throws Exception {
        /*
         * The default ACL is ZooDefs.Ids#OPEN_ACL_UNSAFE
         */
        client.create().withMode(CreateMode.PERSISTENT).inBackground().forPath(path, data);
    }

    /**
     *  选举leader成功后执行该方法，在此方法中启动对/workers, /tasks的监听，并在/assign下进行进行任务分配
     */
    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception
    {
        //如果当当前选举的master挂掉之后会进行重新选举，此时当前的master得再次发现之前可用的worker和tasks
        LOG.info( "Mastership participants: " + myId + ", " + leaderSelector.getParticipants() );
         /*
         * Register listeners for master
         *
         * client.getCuratorListenable().addListener(masterListener);
        client.getUnhandledErrorListenable().addListener(errorsListener);*/
        //为workerCache添加监视器并并启动监听
        addWorkersListener(workersCacheListener);
        workersCache.start();

    }

    /**
     *curatorFramework 与zkserver的网络连接状态
     * @param curatorFramework
     * @param connectionState
     */
    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState)
    {
        switch(connectionState){
            case CONNECTED:
                //Nothing to do in this case.

                break;
            case RECONNECTED:
                // Reconnected, so I should
                // still be the leader.

                break;
            case SUSPENDED:
                LOG.warn("Session suspended");

                break;
            case LOST:
                try{
                    close();
                } catch (IOException e) {
                    LOG.warn( "Exception while closing", e );
                }

                break;
            case READ_ONLY:
                // We ignore this case

                break;
        }
    }

    /**
     * 检查节点是否存在
     * @return
     */
    public boolean isNodeExist(String node) throws Exception
    {
        Stat stat=client.checkExists().forPath(node);
        if (stat!=null){
            return true;
        }
        return false;
    }
    /**
     * 查找未分配的任务节点
     * @return
     * @throws Exception
     */
    public List<String> getTasks() throws Exception
    {
        List<String> tasks=  client.getChildren().forPath("/tasks");
        List<String> assignsedTasks=  client.getChildren().forPath("/assigns");
        List notAssignedTasks=new ArrayList();
        for(String assign:assignsedTasks){
         String assignTask= assign.split("/")[0];
            if(!tasks.contains(assignTask)){
                notAssignedTasks.add(assignTask);
            }
        }
        return notAssignedTasks;
    }
    @Override
    public void close() throws IOException
    {

    }

    /**
     * 对/workers节点进行监听的监视器需完成对相应事件的处理
     */
    PathChildrenCacheListener workersCacheListener = new PathChildrenCacheListener() {
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) {
            switch (event.getType()){
                case CHILD_REMOVED:break;
                case CHILD_ADDED:break;
                case CHILD_UPDATED:break;
            }


        }
    };
    /**
     * 对tasks进行监听的监视器需完成对相应事件的处理
     */
    PathChildrenCacheListener tasksCacheListener = new PathChildrenCacheListener() {
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) {
            switch (event.getType()){
                case  CHILD_ADDED:{
                    try{
                        assignTask(event.getData().getPath().replaceFirst("/tasks/", ""),
                                event.getData().getData());
                    } catch (Exception e) {
                        LOG.error("Exception when assigning task.", e);
                    }
                }
            }
        }
    };

}
