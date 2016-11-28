package com.gy.wm.util;

import com.alibaba.fastjson.JSONObject;
import com.gy.wm.controller.API;
import com.gy.wm.model.TaskParamModel;
import com.gy.wm.service.TaskConfigService;
import com.gy.wm.vo.Base;
import com.gy.wm.vo.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * <类详细说明:启动任务异步测试>
 *
 * @Author： Huanghai
 * @Version: 2016-11-28
 **/
public class TaskTest {

    public static void main(String[] args) {
        List<String> seedUrls = new ArrayList<>();
        Base base = new Base();
        Param param = new Param();
        TaskParamModel taskParamModel =new TaskParamModel();
        API api = new API();
        String id ="2ebb2984228fd024bfac23dbcb375a9e";
        TaskConfigService taskConfigService = new TaskConfigService();
        JSONObject object = taskConfigService.findByIdTask(id);

        seedUrls.addAll(object.keySet());
        param.setSeedUrls(seedUrls);

        base.setId(id);
        base.setDepthCrawl(1);
        base.setTags(object.toJSONString());

        taskParamModel.setParam(param);
        taskParamModel.setBase(base);
        //启动任务
        String reslut = api.startTask(taskParamModel);
        System.out.println("api result param:" + reslut);

    }
}
