package com.gy.wm.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gy.wm.ApplicationWebmagic;
import com.gy.wm.controller.API;
import com.gy.wm.entry.TaskConfig;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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

    private ExecutorService service = Executors.newFixedThreadPool(5);

    @Test
    public void test() throws Exception{
    }

    /**
     * 创建任务
     */
    @Test
    public void testStartTask() {
//        List<String> templateList = new ArrayList<>();
//        templateList.add("");
        List<String> seedUrls = new ArrayList<>();
//
//        seedUrls.add("http://news.qq.com/newsgn/gdxw/gedixinwen.htm");
//        seedUrls.add("http://news.qq.com/newsgn/zhxw/shizhengxinwen.htm");
//        seedUrls.add("http://news.qq.com/newssh/shwx/shehuiwanxiang.htm");
//        seedUrls.add("http://roll.news.qq.com/#");
        String id = "ea536889f698e28b3c77679010be5650";
        JSONObject object = configService.findByIdTask(id);

        seedUrls.addAll(object.keySet());
        param.setSeedUrls(seedUrls);

//        templateList.add(guiyangTemplate);
//        param.setTemplates(templateList);

        base.setId(id);
        base.setDepthCrawl(1);
        base.setTags(object.toJSONString());
//        Map<String,String> map = new HashMap<>();
//        map.put("http://www.trs.gov.cn/xwzx/zyzcxx/index.html","中央政策信息");
//        map.put("http://www.trs.gov.cn/xwzx/zwdsj/index.html","大事记");
//        base.setTags(JSON.toJSONString(map));


        taskParamModel.setParam(param);
        taskParamModel.setBase(base);
        System.out.println(api.startTask(taskParamModel));
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListTask(){

        List<TaskConfig> configs = configService.findByIdStart(12);
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
           System.out.println(api.startTask(taskParamModel));

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


}
