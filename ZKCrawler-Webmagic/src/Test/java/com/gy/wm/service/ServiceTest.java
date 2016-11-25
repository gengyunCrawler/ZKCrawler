package com.gy.wm.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gy.wm.ApplicationWebmagic;
import com.gy.wm.controller.API;
import com.gy.wm.dao.CrawlDataDao;
import com.gy.wm.entry.TaskConfig;
import com.gy.wm.model.CrawlData;
import com.gy.wm.vo.Base;
import com.gy.wm.vo.Param;
import com.gy.wm.model.TaskParamModel;
import com.gy.wm.service.CustomPageProcessor;
import com.gy.wm.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <类详细说明：单元测试>
 *
 * @Author： Huanghai
 * @Version: 2016-09-13
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationWebmagic.class)
@WebAppConfiguration

public  class ServiceTest {
    @Autowired
    private TaskService taskService;
    @Autowired
    private CustomPageProcessor customPageProcessor;
    @Autowired
    private API api;
    @Autowired
    private TaskParamModel taskParamModel;
    @Autowired
    private Param param;
    @Autowired
    private Base base;
    @Autowired
    private TaskConfigService configService;
    @Autowired
    private CrawlDataDao crawlDataDao;

    @Test
    public void test() throws Exception{
    }

    /**
     * 启动单条任务
     */
    @Test
    public void testStartTask() {
        List<String> seedUrls = new ArrayList<>();

        String id = "ee6ea2c2cefb7229b95ac869ae6b0c15";
        JSONObject object = configService.findByIdTask(id);

        seedUrls.addAll(object.keySet());
        param.setSeedUrls(seedUrls);

        base.setId(id);
        base.setDepthCrawl(1);

        taskParamModel.setParam(param);
        taskParamModel.setBase(base);
        //启动任务
        String reslut = api.startTask(taskParamModel);
        System.out.println("**************API接口返回任务Id: *****************" + reslut);
    }

    /**
     * 同时启动多条任务
     */
    @Test
    public void testListTask(){

        List<TaskConfig> configs = configService.findByIdStart(10);
       for (TaskConfig conf:configs){

           JSONObject object = JSON.parseObject(conf.getConfValue());

           List<String> seedUrls = new ArrayList<>();

           String id =conf.getIdTask();

           seedUrls.addAll(object.keySet());
           param.setSeedUrls(seedUrls);


           base.setId(id);
           base.setDepthCrawl(1);
           base.setTags(object.toJSONString());

           taskParamModel.setParam(param);
           taskParamModel.setBase(base);
           //启动任务
           api.startTask(taskParamModel);

       }

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void splitInfobar()  {
        String infobar = "索引号：52230011/2016-173111 文章来源：黔西南日报 发布时间：2016/11/11 9:37:15 作者：方文毅 娄孝云 点击：119 【字体： 】大中小【打印内容】【内容纠错】";
        String [] arr = infobar.split(" ");
        for (int i=0; i< arr.length; i++) {
            System.out.println(i+": " + arr[i]);
        }
    }

    @Test
    public void getCrawlDataById() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        CrawlData crawlData = crawlDataDao.findCrawlDataById(182);
        System.out.println("发布时间：" + (crawlData.getCrawlTime()==null?"":sdf.format(crawlData.getCrawlTime())));
    }


    @Test
    public void insertCrawlDataTest()   {
        CrawlData crawlData = new CrawlData();
        crawlData.setTid("a123412341");
        crawlData.setUrl("www.test.com");
        crawlData.setPublishTime(null);
        crawlDataDao.insertCrawlData(crawlData);
    }

    @Test
    public void testThread()    {
        System.out.println(Thread.currentThread().getName());
    }
}
