package com.gy.curator;

import cn.com.cloudpioneer.master.utils.CuratorUtils;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

/**
 * Created by Tijun on 2016/9/2.
 */
public class CuratorMasterTester
{
    @Test
    public void testLister() throws Exception
    {

      CuratorMaster master=new CuratorMaster("123","192.168.142.2:2181",new ExponentialBackoffRetry(1000,5));
        master.startZK();
        master.runForMaster();
        Thread.sleep(500);
        CuratorMaster master1=new CuratorMaster("456","192.168.142.2:2181",new ExponentialBackoffRetry(1000,5));
        master1.startZK();
        master1.runForMaster();
//
//        CuratorMaster master2=new CuratorMaster("789","192.168.142.2:2181",new ExponentialBackoffRetry(1000,5));
//        master2.startZK();
//        master2.runForMaster();
//
//        CuratorMaster master3=new CuratorMaster("123","192.168.142.2:2181",new ExponentialBackoffRetry(1000,5));
//        master3.startZK();
//        master3.runForMaster();
     //   Thread.sleep(Long.MAX_VALUE);


    }

    @Test
    public void deleteTaskZnode() throws Exception
    {
    CuratorMaster master=new CuratorMaster("123","192.168.142.2:2181",new ExponentialBackoffRetry(1000,5000));
        master.startZK();
        master.getClient().delete().forPath("/tasks/taks1");
    }
    @Test
    public void deleteWorkers() throws Exception
    {
        CuratorMaster master=new CuratorMaster("123","192.168.142.2:2181",new ExponentialBackoffRetry(1000,5000));
        master.startZK();
        CuratorUtils.deleteChidrenForPath(master.getClient(), "/workers");

    }



    @Test
    public void addStatus() throws Exception
    {
        CuratorMaster master=new CuratorMaster("123","192.168.142.2:2181",new ExponentialBackoffRetry(1000,5000));
        master.startZK();
      String s=master.getClient().create().withMode(CreateMode.EPHEMERAL).forPath("/as","sf".getBytes());
        System.out.println("----------------");
        System.out.println(s);
    }

    @Test
    public void assignTask() throws Exception
    {
        CuratorMaster master=new CuratorMaster("123","88.88.88.110:2181",new ExponentialBackoffRetry(1000,5000));
        master.startZK();
        master.getClient().create().forPath("/tasks/task-44", "agbag".getBytes());
        master.getClient().create().forPath("/workers/worker-1","agbag".getBytes());
       boolean is= master.isNodeExist("/workers/worker-1");
        System.out.println(is);
    }
    @Test
    public void testNodeSplit(){
        String s="/tasks/task-*/worker";
       String []arr= s.split("/");

    }
}
