package com.gy.wm.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <类详细说明:提供最近的ApplicationContext>
 *
 * @Author： Huanghai
 * @Version: 2016-09-14
 **/
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getContext() {
        return applicationContext;
    }
}
