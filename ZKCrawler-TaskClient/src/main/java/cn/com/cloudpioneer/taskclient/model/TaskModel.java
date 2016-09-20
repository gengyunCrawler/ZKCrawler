package cn.com.cloudpioneer.taskclient.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class TaskModel implements Serializable {

    private TaskEntity entity;
    private List<String> workers;
    private String taskPath;
    private String statusData;

    public TaskModel() {

        entity = new TaskEntity();
        workers = new ArrayList<>();
        taskPath = "";
        statusData = "";
    }

    public TaskModel(String taskPath, TaskEntity entity, String statusData, List<String> workers) {

        this.taskPath = taskPath;
        this.entity = entity;
        this.statusData = statusData;
        this.workers = workers;
    }


    public TaskEntity getEntity() {
        return entity;
    }

    public List<String> getWorkers() {
        return workers;
    }

    public String getTaskPath() {
        return taskPath;
    }

    public String getStatusData() {
        return statusData;
    }


    public int getTaskStatus() {
        return entity.getStatus();
    }

    public void setTaskStatus(int taskStatus) {
        entity.setStatus(taskStatus);
    }

    @Override
    public String toString() {

        JSONObject object = new JSONObject();
        object.put("entity", this.entity);
        object.put("workers", this.workers);
        object.put("taskPath", this.taskPath);
        object.put("statusData", this.statusData);

        return object.toJSONString();
    }
}
