package com.gy.wm.controller;

import com.gy.wm.model.TaskEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * worker和webmagic交互的API
 *
 * @Author： Hunanghai
 * @Version: 2016-09-08
 **/
@RestController
public class API {
    @RequestMapping("/startTask")
    public String startTask(TaskEntity taskEntity)   {
        return null;
    }
}
