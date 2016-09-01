package cn.com.cloudpioneer.master;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
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
        this.client= CuratorFrameworkFactory.newClient(hostPort, 1600, 1600, retryPolicy);
        this.leaderSelector = new LeaderSelector(this.client, "/master", this);
        this.workersCache = new PathChildrenCache(this.client, "/workers", true);
        this.tasksCache = new PathChildrenCache(this.client, "/tasks", true);

    }

    /**
     * 启动连接
     */
    public void startZK(){
        client.start();
    }

    /**
     * 初始化节点如果节点不存在择创建节点
     * @throws Exception
     */
    public void bootstrap() throws Exception {
       Stat stat= client.checkExists().forPath("/workers");
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
     *  选举leader成功后执行该方法，在此方法中启动对/workers, /tasks的监听，并在/assign下进行进行任务分配
     */
    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception
    {

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

    @Override
    public void close() throws IOException
    {

    }

}
