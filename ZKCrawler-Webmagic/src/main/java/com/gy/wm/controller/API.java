package com.gy.wm.controller;

import com.gy.wm.model.TaskParamModel;
import com.gy.wm.service.TaskService;
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
    @Autowired
    private TaskService taskService;

    @RequestMapping("/test")
    public String test() {
        return "test!";
    }

    /**
     * @param taskParamModel
     * @return
     */
    @RequestMapping(value = "/startTask", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String startTask(@RequestBody TaskParamModel taskParamModel) {
        final TaskParamModel taskModel = taskParamModel;
        TaskService.taskExecutor(new Runnable() {
            @Override
            public void run() {
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
                taskService.cleanTaskRedis(taskId);
            }
        });
        return taskId;//this.taskService.cleanTaskRedis(taskId);
    }

    @Override
    public void run() {

    }
}
