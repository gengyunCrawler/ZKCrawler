package cn.com.cloudpioneer.taskclient.app;

import cn.com.cloudpioneer.taskclient.model.TaskEntity;
import cn.com.cloudpioneer.taskclient.scheduler.SchedulePolicy;
import cn.com.cloudpioneer.taskclient.scheduler.schedulerImpl.EveryWorkerPolicy;

import java.util.List;
import java.util.Map;

/**
 * Created by Tianjinjin on 2016/9/2.
 */
public class Scheduler {

    //调度策略成员
    private SchedulePolicy policy;

    public Scheduler() {

        policy = new EveryWorkerPolicy();
    }

    public Scheduler(SchedulePolicy policy) {
        this.policy = policy;
    }

    public void setPolicy(SchedulePolicy policy) {
        this.policy = policy;
    }

    public SchedulePolicy getPolicy() {
        return policy;
    }

    public Map<TaskEntity, List<String>> scheduleProcess(List<TaskEntity> tasks, List<String> workers) {
        return policy.process(tasks, workers);
    }
}
