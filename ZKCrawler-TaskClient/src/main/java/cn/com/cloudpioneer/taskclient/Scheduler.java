package cn.com.cloudpioneer.taskclient;

import cn.com.cloudpioneer.taskclient.schedule.SchedulePolicy;

import java.util.List;
import java.util.Map;

/**
 * Created by Tianjinjin on 2016/9/2.
 */
public class Scheduler {

    //调度策略成员
    private SchedulePolicy policy;

    public Scheduler() {

    }

    public Scheduler(SchedulePolicy policy) {
        this.policy = policy;
    }

    public void setPolicy(SchedulePolicy policy){
        this.policy = policy;
    }

    public Map<String,List<String>> scheduleProcess(List<Object> workers, List<Object> tasks){
        return null;
    }
}
