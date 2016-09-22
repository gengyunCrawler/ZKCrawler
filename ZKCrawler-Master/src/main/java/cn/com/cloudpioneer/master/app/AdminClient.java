package cn.com.cloudpioneer.master.app;

import cn.com.cloudpioneer.master.utils.CuratorUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;

import java.util.List;

/**
 * 向外提供集群的装态信息，主要为/tasks和/workers节点的节点和状态信息
 * Created by Tijun on 2016/9/6.
 * @author TijunWang
 * @version 1.0
 */
public class AdminClient
{
    CuratorFramework client;
    public AdminClient(String hostPort, RetryPolicy retryPolicy){

        this.client= CuratorFrameworkFactory.newClient(hostPort, retryPolicy);
    }
    public void start(){
        client.start();
    }
    public CuratorFramework getClient(){
        return client;
    }

    public void getTasksStatus(){

    }

    /**
     * 获取该节点下的所有子节点
     * @param znode
     * @return List<String>
     * @throws Exception
     */
    public List<String> getChildren(String znode) throws Exception
    {
       return client.getChildren().forPath(znode);
    }

    /**
     * 获取该节点下的数据
     * @param znode
     * @return
     * @throws Exception
     */
    public byte[] getData(String znode) throws Exception
    {
        if(CuratorUtils.isNodeExist(client,znode)){
            return client.getData().forPath(znode);
        }
     return null;
    }
}
