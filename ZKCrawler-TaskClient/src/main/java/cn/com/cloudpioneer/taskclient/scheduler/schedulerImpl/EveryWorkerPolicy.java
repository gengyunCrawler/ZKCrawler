package cn.com.cloudpioneer.taskclient.scheduler.schedulerImpl;

import cn.com.cloudpioneer.taskclient.model.TaskEntity;
import cn.com.cloudpioneer.taskclient.scheduler.SchedulePolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tianjinjin on 2016/9/2.
 */
public class EveryWorkerPolicy implements SchedulePolicy {


    @Override
    public Map<TaskEntity, List<String>> process(List<TaskEntity> tasks, List<String> workers) {
        Map<TaskEntity, List<String>> map = new HashMap<>();
        for (TaskEntity task : tasks) {
            map.put(task, workers);
        }
        return map;
    }
}
