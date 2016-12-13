package com.gy.wm.controller;

import com.alibaba.fastjson.JSONObject;
import com.gy.wm.model.TaskParamModel;
import com.gy.wm.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * worker和webmagic交互的API
 *
 * @Author： Hunanghai
 * @Version: 2016-09-08
 **/
@RestController
@Scope("prototype")
@RequestMapping("/WebMagic")
public class API {

    private static final Logger LOGGER = LoggerFactory.getLogger(API.class);

    @Autowired
    private TaskService taskService;

    @RequestMapping("/test")
    public String test() {
        return "Test!";
    }


    /**
     * 开始任务接口
     *
     * @param taskParamModel
     * @return
     */
    @RequestMapping(value = "/startTask", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String startTask(@RequestBody TaskParamModel taskParamModel) {
        final TaskParamModel taskModel = taskParamModel;
        API.this.taskService.startTask(taskModel);
        System.out.println("taskModel: " + JSONObject.toJSONString(taskParamModel));

        return taskModel.getBase().getId();
    }


    /**
     * @param taskId
     * @return
     */
    @RequestMapping("/stopTask")
    public String stopTask(@RequestParam("taskId") String taskId) {
        return taskId;
    }

    /**
     * 任务结束后清除rendis数据
     * <p>
     * //     * @param taskId
     *
     * @return String
     */
    @RequestMapping(value = "/cleanTaskRedis", method = RequestMethod.POST)
    public String cleanTaskRedis(@RequestParam("taskId") final String taskId) {

        LOGGER.info("====>> Now, clean the Redis, taskId: " + taskId);
        taskService.cleanTaskRedis(taskId);

        return taskId;
    }

}
