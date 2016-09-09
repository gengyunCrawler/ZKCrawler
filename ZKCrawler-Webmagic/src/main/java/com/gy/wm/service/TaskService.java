package com.gy.wm.service;

import com.gy.wm.entry.Crawl;
import com.gy.wm.model.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * <类详细说明:work处理webmagic 任务请求>
 *
 * @Author： Huanghai
 * @Version: 2016-09-09
 **/
@Service
public class TaskService {

    @Autowired
    private Crawl crawl;

    public void startTask(TaskEntity taskEntity) {
        this.crawl.startTask(taskEntity);
    }


}
