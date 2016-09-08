package com.gy.wm.controller;

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

    @RequestMapping("/")
    public String index()   {
        return "hello world";
    }
}
