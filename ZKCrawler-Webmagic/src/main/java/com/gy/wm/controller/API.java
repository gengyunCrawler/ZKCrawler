package com.gy.wm.controller;

import com.gy.wm.model.TaskParamModel;
import com.gy.wm.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * worker和webmagic交互的API
 *
 * @Author： Hunanghai
 * @Version: 2016-09-08
 **/
@RestController
@Scope("prototype")
public class API implements Runnable {
    @Autowired
    private TaskService taskService;

    @RequestMapping("/")
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
                API.this.taskService.startTask(taskModel);
            }
        });
        return taskModel.getBase().getId();
    }

    /**
     * 任务结束后清除rendis数据
     * @param tid
     * @return String
     */
    public String cleanTaskRedis(String tid)    {
        return this.taskService.cleanTaskRedis(tid);
    }

    @Override
    public void run() {

    }
}
