package com.gy.wm.controller;

import com.gy.wm.model.TaskEntity;
import com.gy.wm.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/startTask",method = RequestMethod.POST)
    public @ResponseBody String startTask(@RequestBody TaskEntity taskEntity)   {
        this.taskService.startTask(taskEntity);
        return "0";
    }

}
