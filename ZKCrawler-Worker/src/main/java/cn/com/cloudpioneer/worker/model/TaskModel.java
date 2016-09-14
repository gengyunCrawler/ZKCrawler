package cn.com.cloudpioneer.worker.model;


import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by TianyuanPan on 2016/9/5.
 * <p>
 * TaskModel 类是 worker 用于描述一个任务的模型。
 * worker 的操作将会围绕此模型进行。
 */
public class TaskModel implements Serializable {

    /**
     * taskPath，任务节点在 zookeeper 中的绝对路径。
     */
    private String taskPath;

    /**
     * taskData，任务的配置信息，存储在任务节点中。
     */
    private byte[] taskData;

    /**
     * entityString，任务配置信息的实体 JSON 字符串。
     */
    private String entityString;

    /**
     * worker 启动任务时的时间。
     */
    private long startTime;

    /**
     * 任务对象实体。
     */
    private TaskEntity entity;


    /**
     * 无参数构造方法，构造空数据的任务模型。
     */
    public TaskModel() {
        this.taskPath = "";
        this.taskData = new byte[0];
        this.entityString = "";
        this.startTime = System.currentTimeMillis();
        this.entity = new TaskEntity();
    }


    /**
     * 参数构造方法，构造 worker 所需的任务模型。
     *
     * @param taskPath 任务节点的绝对路径
     * @param taskData 任务数据
     */
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


    /**
     * 设置任务路径
     *
     * @param taskPath 任务节点绝对路径
     */
    public void setTaskPath(String taskPath) {
        this.taskPath = taskPath;
    }


    /**
     * 设置任务数据
     *
     * @param taskData 任务数据
     */
    public void setTaskData(byte[] taskData) {
        this.taskData = taskData;
        try {
            this.entityString = new String(taskData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            this.entityString = "";
            e.printStackTrace();
        }
    }

    /**
     * 设置任务启动时间
     *
     * @param startTime 任务启动时间
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取任务路径
     *
     * @return
     */
    public String getTaskPath() {
        return taskPath;
    }

    /**
     * 获取任务数据
     *
     * @return
     */
    public byte[] getTaskData() {
        return taskData;
    }

    /**
     * 获取任务配置实体的 JSON 字符串。
     *
     * @return
     */
    public String getEntityString() {
        return entityString;
    }

    /**
     * 获取任务的启动时间。
     *
     * @return
     */
    public long getStartTime() {
        return startTime;
    }


    /**
     * 获取任务实体对象。
     *
     * @return
     */
    public TaskEntity getEntity() {
        return entity;
    }

}
