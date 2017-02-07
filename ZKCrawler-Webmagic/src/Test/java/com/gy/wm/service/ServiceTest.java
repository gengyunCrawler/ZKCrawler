package com.gy.wm.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gy.wm.ApplicationWebmagic;
import com.gy.wm.controller.API;
import com.gy.wm.dao.CrawlDataDao;
import com.gy.wm.model.TaskConfig;
import com.gy.wm.model.CrawlData;
import com.gy.wm.vo.Base;
import com.gy.wm.vo.Param;
import com.gy.wm.model.TaskParamModel;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    /**
     * 任务测试
     */
    @Test
    public void testStartTask() {
        //测试的任务Id
        String id = "oVfvcEw9ivKpOuf7dWnwNMeAm2KMOCSK";
        //测试的种子
        String testUrl  = "http://www.lpswz.com/09news/node_4151.htm";
        JSONArray seedUrlsArray = new JSONArray();
        JSONArray tagsArray = new JSONArray();
        JSONArray categoriesArray = new JSONArray();
        JSONObject seedObject = new JSONObject();
        seedObject.put("tags",tagsArray);
        seedObject.put("sourceTypeId","");
        seedObject.put("sourceRegion","");
        seedObject.put("sourceName","");
        seedObject.put("categories",categoriesArray);
        seedObject.put("url",testUrl);
        seedUrlsArray.add(seedObject);
        String seedUrls = JSONArray.toJSONString(seedUrlsArray);
        param.setSeedUrls(seedUrls);
        param.setTemplates("");

        base.setId(id);
        base.setDepthCrawl(1);

        taskParamModel.setParam(param);
        taskParamModel.setBase(base);
        //启动任务
        taskService.testStartTask(taskParamModel);
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
