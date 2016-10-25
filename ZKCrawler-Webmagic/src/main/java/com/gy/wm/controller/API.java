package com.gy.wm.controller;

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
public class API implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(API.class);

    @Autowired
    private TaskService taskService;

    @RequestMapping("/test")
    public String test() {
        return "test!";
    }


    /**开始任务接口
     * @param taskParamModel
     * @return
     */
    @RequestMapping(value = "/startTask", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String startTask(@RequestBody TaskParamModel taskParamModel) {
        final TaskParamModel taskModel = taskParamModel;
        TaskService.taskExecutor(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("==++==> starting task. taskId: " + taskModel.getBase().getId());
                API.this.taskService.startTask(taskModel);
            }
        });
        return taskModel.getBase().getId();
    }


    /**
     * @param taskId
     * @return
     */
    @RequestMapping("/stopTask/{taskId}")
    public String stopTask(@PathVariable("taskId") String taskId) {

        return taskId;
    }

    /**
     * 任务结束后清除rendis数据
     *
     * @param taskId
     * @return String
     */
    @RequestMapping(value = "/cleanTaskRedis/{taskId}", method = RequestMethod.POST)
    public String cleanTaskRedis(@PathVariable("taskId") final String taskId) {
        TaskService.taskExecutor(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("===> Now, clean the Redis, taskId: " + taskId);
                taskService.cleanTaskRedis(taskId);
            }
        });
        return taskId;
    }

    @Override
    public void run() {

    }
}
