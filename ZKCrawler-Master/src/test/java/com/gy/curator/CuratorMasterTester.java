package com.gy.curator;

import org.apache.curator.retry.ExponentialBackoffRetry;
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
        Thread.sleep(Long.MAX_VALUE);

    }

    @Test
    public void deleteTaskZnode() throws Exception
    {
    CuratorMaster master=new CuratorMaster("123","192.168.142.2:2181",new ExponentialBackoffRetry(1000,5000));
        master.startZK();
        master.getClient().delete().forPath("/tasks/taks1");
    }

    @Test
    public void assignTask() throws Exception
    {
        CuratorMaster master=new CuratorMaster("123","192.168.142.2:2181",new ExponentialBackoffRetry(1000,5000));
        master.startZK();
       // master.getClient().create().forPath("/tasks/task-44", "agbag".getBytes());
        master.getClient().create().forPath("/tasks/task-44/worker-1","agbag".getBytes());
    }
    @Test
    public void testNodeSplit(){
        String s="/tasks/task-*/worker";
       String []arr= s.split("/");

    }
}
