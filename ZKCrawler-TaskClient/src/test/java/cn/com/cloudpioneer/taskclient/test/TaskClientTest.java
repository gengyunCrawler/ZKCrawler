package cn.com.cloudpioneer.taskclient.test;


import cn.com.cloudpioneer.taskclient.app.TaskClient;
import cn.com.cloudpioneer.taskclient.entity.TaskEntity;
import cn.com.cloudpioneer.taskclient.mapper.TaskEntityMapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Tianjinjin on 2016/9/1.
 */
@ContextConfiguration("classpath:spring-servlet.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TaskClientTest {
    @Autowired
    TaskEntityMapper taskEntityMapper;
    @Test
    public void testTaskEntity() throws ParseException {
        TaskEntity taskEntity =new TaskEntity();
        taskEntity.setCompleteTimes(2);
        taskEntity.setDeleteFlag(true);
        taskEntity.setCostLastCrawl(20);
        taskEntity.setCycleRecrawl(40);
        taskEntity.setDepthCrawl(3);
        taskEntity.setIdUser(0144552);
        taskEntity.setName("dfsaf");
        /*taskEntity.setPathTemplates("agvb");
        taskEntity.setSeedUrls("rety");
        taskEntity.setPathRegexFilter("trh");
        taskEntity.setPathRegexFilter("ngjrd");*/
        taskEntity.setThreads(3);
        taskEntity.setWorkNum(5);
        //taskEntity.setSeedUrls("url1,url2,url3");
        taskEntity.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-08-04 16:30:20"));
        System.out.println(taskEntity.toString());

    }

    @Test
    public void listenTaskNode() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.229.130:2181", new RetryNTimes(Integer.MAX_VALUE, 1000));
        CountDownLatch countDownLatch = new CountDownLatch(1);
        client.start();
        //ExecutorService pool = Executors.newCachedThreadPool();
        //设置节点的cache
        TreeCache treeCache = new TreeCache(client, "/tasks");
        //设置监听器和处理过程
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                ChildData data = event.getData();
                System.out.println("data:" + data);
                switch (event.getType()) {
                    case NODE_ADDED:
                        System.out.println("NODE_ADDED : " + data.getPath() + "  数据:" + new String(data.getData()));
                        break;
                    case NODE_REMOVED:
                        System.out.println("NODE_REMOVED : " + data.getPath());
                        break;
                    case NODE_UPDATED:
                        System.out.println("NODE_UPDATED : " + data.getPath() + "  数据:" + new String(data.getData()));
                        break;
                    default:
                        break;
                }
                if (data == null) {
                    System.out.println("data is null : " + event.getType());
                }
            }
        });
        //开始监听
        treeCache.start();
        //countDownLatch.countDown();
        //client.create().withMode(CreateMode.PERSISTENT).forPath("/tasks/task4", "qqq".getBytes());
        countDownLatch.await();

    }

    @Test
    public void testAddNode() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.229.130:2181", new RetryNTimes(Integer.MAX_VALUE, 1000));
        client.start();
        client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/tasks/task2", "qqq".getBytes());
        //client.close();
    }

    @Test
    public void testDeleteNode() throws Exception{
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.229.130:2181", new RetryNTimes(Integer.MAX_VALUE, 1000));
        client.start();
        client.delete().forPath("/tasks/task2");
    }


    @Test
    public void testSelectLeader() throws InterruptedException {
        TaskClient taskClient= new TaskClient();
        for(int i=0;i<10;i++){
            taskClient.setMyId("x" + i);
            taskClient.setClient(CuratorFrameworkFactory.newClient("192.168.229.130:2181", new RetryNTimes(Integer.MAX_VALUE, 1000)));
            taskClient.getClient().start();
            //taskClient.stateChanged(taskClient.getClient(),taskClient.getClient().getConnectionStateListenable());
        }
        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void testDataHandle() throws ParseException {
        List<TaskEntity> taskEntityList;
        TaskEntity resultTask;
        TaskEntity taskEntity =new TaskEntity();
        taskEntity.setId("9999");
        taskEntity.setCompleteTimes(2);
        taskEntity.setDeleteFlag(true);
        taskEntity.setCostLastCrawl(20);
        taskEntity.setCycleRecrawl(40);
        taskEntity.setDepthCrawl(3);
        taskEntity.setIdUser(0144552);
        taskEntity.setName("xxxzzz");
        taskEntity.setType(1);
        /*taskEntity.setPathTemplates("agvb");
        taskEntity.setSeedUrls("qqq");
        taskEntity.setPathRegexFilter("ppp");
        taskEntity.setPathRegexFilter("^_^");*/
        taskEntity.setThreads(3);
        taskEntity.setWorkNum(5);
        //taskEntity.setSeedUrls("url1,url2,url3");
        taskEntity.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-08-03 07:20:10"));
        //String currentDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        taskEntity.setTimeLastCrawl(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-08-04 16:30:20"));

        //从数据库读取数据
        /*taskEntityList =taskEntityMapper.findAll();
        for(TaskEntity task:taskEntityList){
            System.out.println(task.getName()+"  "+task.isDeleteFlag());
        }*/

        //插入一条数据
        //taskEntityMapper.insertTaskEntity(taskEntity);

        //软删除数据
        //taskEntityMapper.deleteTaskEntity(taskEntity.getId());

        //通过id从数据库读取task实体
        /*resultTask =taskEntityMapper.findById(taskEntity.getId());
        System.out.println(resultTask.getName());*/


        //通过类型从数据库读取task实体
        /*taskEntityList=taskEntityMapper.findByType(taskEntity.getType());
        for(TaskEntity task:taskEntityList){
            System.out.println(task.getName()+"  "+task.isDeleteFlag());
        }*/

        //通过上一次（最近一次）抓取的时间从数据库读取task实体
        /*taskEntityList=taskEntityMapper.findByTimeLastCrawl(taskEntity.getTimeLastCrawl());
        for(TaskEntity task:taskEntityList){
            System.out.println(task.getName()+"  "+task.isDeleteFlag());
        }*/

        //若爬取任务没有完成，把数据库的timeLastCrawl字段更新为最新的这次
        //taskEntityMapper.updateTimeLastCrawl(taskEntity.getId(),taskEntity.getTimeLastCrawl());

        //更新数据
        taskEntityMapper.updateTaskEntityById(taskEntity);


    }

}
