package cn.com.cloudpioneer.taskclient.test;


import cn.com.cloudpioneer.taskclient.entity.TaskEntity;
import cn.com.cloudpioneer.taskclient.mapper.TaskEntityMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        taskEntity.setPathTemplates("agvb");
        taskEntity.setSeedUrls("rety");
        taskEntity.setPathRegexFilter("trh");
        taskEntity.setPathRegexFilter("ngjrd");
        taskEntity.setThreads(3);
        taskEntity.setWorkNum(5);
        taskEntity.setSeedUrls("url1,url2,url3");
        taskEntity.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-08-04 16:30:20"));
        System.out.println(taskEntity.toString());

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
        taskEntity.setPathTemplates("agvb");
        taskEntity.setSeedUrls("qqq");
        taskEntity.setPathRegexFilter("ppp");
        taskEntity.setPathRegexFilter("^_^");
        taskEntity.setThreads(3);
        taskEntity.setWorkNum(5);
        taskEntity.setSeedUrls("url1,url2,url3");
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
