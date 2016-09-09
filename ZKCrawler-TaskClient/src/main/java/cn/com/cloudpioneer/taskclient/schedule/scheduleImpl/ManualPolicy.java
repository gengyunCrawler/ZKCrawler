package cn.com.cloudpioneer.taskclient.schedule.scheduleImpl;

import cn.com.cloudpioneer.taskclient.entity.TaskEntity;
import cn.com.cloudpioneer.taskclient.schedule.SchedulePolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tianjinjin on 2016/9/2.
 */
public class ManualPolicy implements SchedulePolicy{


    @Override
    public Map<String, List<String>> process(List<String> workers, List<TaskEntity> tasks) {
        Map<String,List<String>> map = new HashMap<String, List<String>>();
        for(TaskEntity task:tasks){
            map.put(task.getName(),workers);
        }
        return map;
    }
}
