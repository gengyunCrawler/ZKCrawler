package com.gy.wm.service;

import com.gy.wm.ApplicationWebmagic;
import com.gy.wm.controller.API;
import com.gy.wm.util.JSONUtil;
import com.gy.wm.vo.Base;
import com.gy.wm.vo.Param;
import com.gy.wm.model.TaskParamModel;
import com.gy.wm.service.CustomPageProcessor;
import com.gy.wm.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Test
    public void test() throws Exception{
    }

    //启动任务单元测试
    @Test
    public void testStartTask() {
//        List<String> templateList = new ArrayList<>();
//        templateList.add("");
        List<String> seedUrls = new ArrayList<>();

        seedUrls.add("http://www.gzgov.gov.cn/xwzx/gzxw/");
        param.setSeedUrls(seedUrls);

//        templateList.add(guiyangTemplate);
//        param.setTemplates(templateList);

        base.setId("http://www.gzgov.gov.cn");
        base.setTags(setTag("贵州","贵州新闻"));

        /*base.setDepthCrawl(1);
        taskParamModel.setParam(param);
        taskParamModel.setBase(base);
        System.out.println(api.startTask(taskParamModel));
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    public String setTag(String area,String column)    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("area",area);
        jsonObject.put("column",column);
        System.out.println(JSONUtil.object2JacksonString(jsonObject));
        return JSONUtil.object2JacksonString(jsonObject);
    }
}
