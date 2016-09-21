package com.gy.wm.service;

import com.gy.wm.entry.Crawl;
import com.gy.wm.model.TaskParamModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * <类详细说明:work处理webmagic 任务请求>
 *
 * @Author： Huanghai
 * @Version: 2016-09-09
 **/
@Service
@Component
@Scope("prototype")
public class TaskService {

    @Autowired
    private Crawl crawl;

    public TaskService()    {
    }

    public void startTask(TaskParamModel taskParamModel) {
        this.crawl.startTask(taskParamModel);
    }

    public void test()  {
        System.out.println("test");
    }
}
