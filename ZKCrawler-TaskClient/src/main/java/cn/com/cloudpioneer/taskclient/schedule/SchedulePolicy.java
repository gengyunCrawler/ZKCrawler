package cn.com.cloudpioneer.taskclient.schedule;

import java.util.List;
import java.util.Map;

/**
 * Created by Tianjinjin on 2016/9/2.
 */
public interface SchedulePolicy {
    public Map<String,List<String>> process(List<Object> workers, List<Object> tasks);
}
