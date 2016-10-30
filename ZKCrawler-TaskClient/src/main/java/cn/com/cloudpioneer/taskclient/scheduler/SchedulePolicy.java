package cn.com.cloudpioneer.taskclient.scheduler;

import cn.com.cloudpioneer.taskclient.model.TaskEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Tianjinjin on 2016/9/2.
 */
public interface SchedulePolicy {

    Map<TaskEntity, List<String>> process(List<TaskEntity> tasks, List<String> workers);

    String getPolicyName();
}
