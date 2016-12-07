package com.gy.wm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <SpringBootApplication,启动Springboot提供API服务>
 *
 * @Author： Hunanghai
 * @Version: 2016-09-08
 **/
@SpringBootApplication
@EnableAsync
public class ApplicationWebmagic {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationWebmagic.class,args);
    }

}
