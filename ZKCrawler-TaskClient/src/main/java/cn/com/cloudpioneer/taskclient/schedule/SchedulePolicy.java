package cn.com.cloudpioneer.taskclient.schedule;

import cn.com.cloudpioneer.taskclient.entity.TaskEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Tianjinjin on 2016/9/2.
 */
public interface SchedulePolicy {
    public Map<String,List<String>> process(List<String> workers, List<TaskEntity> tasks);
}
