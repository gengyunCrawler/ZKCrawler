package com.gy.wm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * <SpringBootApplication,启动Springboot提供API服务>
 *
 * @Author： Hunanghai
 * @Version: 2016-09-08
 **/
@SpringBootApplication
@ComponentScan
public class Application {
    public static void main(String[] args) {
       SpringApplication.run(Application.class,args);
    }
}
