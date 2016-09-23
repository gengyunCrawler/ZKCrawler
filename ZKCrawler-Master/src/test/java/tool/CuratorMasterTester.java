package tool;

import cn.com.cloudpioneer.master.app.CuratorMaster;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

import java.util.regex.Pattern;

/**
 * Created by Tijun on 2016/9/2.
 */
public class CuratorMasterTester
{
    @Test
    public void testGetTasks() throws Exception
    {
        CuratorMaster master=new CuratorMaster("192.168.142.2:2181",new ExponentialBackoffRetry(1000,5000), null);
        master.startZK();
//        master.getClient().create().withMode(CreateMode.PERSISTENT).forPath("/assigns");
//        master.getClient().create().withMode(CreateMode.PERSISTENT).forPath("/tasks/task-123", new byte[0]);
//        master.getClient().create().withMode(CreateMode.PERSISTENT).forPath("/tasks/task-456",new byte[0]);
//        master.getClient().create().withMode(CreateMode.PERSISTENT).forPath("/tasks/task-789",new byte[0]);
//
//        master.getClient().create().withMode(CreateMode.PERSISTENT).forPath("/assigns/worker-123",new byte[0]);
        master.getClient().create().withMode(CreateMode.PERSISTENT).forPath("/assigns/worker-123/task-123",new byte[0]);
//        master.getClient().create().withMode(CreateMode.PERSISTENT).forPath("/assigns/worker-456/task-456",new byte[0]);
//        master.getClient().create().withMode(CreateMode.PERSISTENT).forPath("/assigns/worker-789/task-789",new byte[0]);

      //  master.getTasks();
    }

    @Test
    public void regrexTest(){
        Pattern pattern=Pattern.compile("^worker.*");
        String s="/tasks/worker-01013/tasks-34343";
        String []arr=s.split("/");
        System.out.println(arr);
    }
}
