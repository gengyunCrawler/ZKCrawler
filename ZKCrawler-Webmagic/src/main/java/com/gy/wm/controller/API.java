package com.gy.wm.controller;

import com.gy.wm.model.TaskEntity;
import com.gy.wm.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class API {
    @Autowired
    private TaskService taskService;

    @RequestMapping("/")
    public String test()  {
        return "test!";
    }

    /**
     *
     * @param taskEntity
     * @return
     */
    @RequestMapping(value = "/startTask",method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    public String startTask(@RequestBody TaskEntity taskEntity)   {
        this.taskService.startTask(taskEntity);
        return "ok";
    }

}
