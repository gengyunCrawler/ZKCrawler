package com.gy.wm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * <SpringBootApplication,启动Springboot提供API服务>
 *
 * @Author： Hunanghai
 * @Version: 2016-09-08
 **/
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ApplicationContext ctx =
                SpringApplication.run(Application.class,args);
    }
}
