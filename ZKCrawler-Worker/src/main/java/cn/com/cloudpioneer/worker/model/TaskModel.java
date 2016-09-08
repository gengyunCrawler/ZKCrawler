package cn.com.cloudpioneer.worker.model;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/9/5.
 */
public class TaskModel implements Serializable {

    private String taskPath;
    private byte[] taskData;
    private String entityString;
    private long startTime;
    private TaskEntity entity;


    public TaskModel() {
        this.taskPath = "";
        this.taskData = new byte[0];
        this.entityString = "";
        this.startTime = System.currentTimeMillis();
        this.entity = new TaskEntity();
    }

    public TaskModel(String taskPath, byte[] taskData) {

        this.taskPath = taskPath;
        this.taskData = taskData;
        try {
            this.entityString = new String(taskData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            this.entityString = "";
            e.printStackTrace();
        }
        this.startTime = System.currentTimeMillis();

        try {
            this.entity = JSONObject.parseObject(this.entityString, TaskEntity.class);
        } catch (Exception e) {
            this.entity = new TaskEntity();
            e.printStackTrace();
        }
    }

    public void setTaskPath(String taskPath) {
        this.taskPath = taskPath;
    }

    public void setTaskData(byte[] taskData) {
        this.taskData = taskData;
        try {
            this.entityString = new String(taskData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            this.entityString = "";
            e.printStackTrace();
        }
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getTaskPath() {
        return taskPath;
    }

    public byte[] getTaskData() {
        return taskData;
    }

    public String getEntityString() {
        return entityString;
    }

    public long getStartTime() {
        return startTime;
    }

    public TaskEntity getEntity() {
        return entity;
    }

/***
 *
 public static void main(String[] args) {
 TaskEntity entity = new TaskEntity();

 entity.setId("123456789");
 entity.setName("my-test-entity");

 System.out.println("entity:====> " + entity.toString());

 TaskModel taskModel = new TaskModel("/path/test", entity.toString().getBytes());

 System.out.println("model-str:====> " + taskModel.getEntityString());
 System.out.println("model-entity: => " + taskModel.getEntity().toString());

 System.out.println("========= model +++++++");
 System.out.println(JSON.toJSONString(taskModel));
 }
 /***********/
}
