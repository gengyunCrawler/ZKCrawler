package cn.com.cloudpioneer.worker.model;


import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
     * 任务具体配置
     */
    private Map<String, TaskConfigEntity> configsMap;


    /**
     * 获取配置项列表。
     *
     * @param typeName 配置项类型名称
     * @return
     */
    public TaskConfigEntity getConfigs(String typeName) {

        return configsMap.get(typeName);
    }

    /**
     * 设置任务配置
     *
     * @param configsMap
     */
    public void setConfigsMap(Map<String, TaskConfigEntity> configsMap) {
        this.configsMap = configsMap;
    }

    /**
     * 获取配置
     *
     * @return
     */
    public Map<String, TaskConfigEntity> getConfigsMap() {
        return configsMap;
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
     * 无参数构造方法，构造空数据的任务模型。
     */
    public TaskModel() {
        this.taskPath = "";
        this.taskData = new byte[0];
        this.entityString = "";
        this.startTime = System.currentTimeMillis();
        this.entity = new TaskEntity();
        this.configsMap = new HashMap<>();
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


    /**
     * 转换成 JSON 字符串
     *
     * @return
     */
    public String toJSONString() {

        JSONObject objectA = new JSONObject();
        JSONObject objectB = new JSONObject();

        if (getConfigs(ConfigType.SEED_URLS) != null)
            objectA.put(ConfigType.SEED_URLS, getConfigs(ConfigType.SEED_URLS).getConfValue());
        else
            objectA.put(ConfigType.SEED_URLS, "");

        if (getConfigs(ConfigType.TEMPLATES) != null)
            objectA.put(ConfigType.TEMPLATES, getConfigs(ConfigType.TEMPLATES).getConfValue());
        else
            objectA.put(ConfigType.TEMPLATES, "");

        if (getConfigs(ConfigType.CONFIGS) != null)
            objectA.put(ConfigType.CONFIGS, getConfigs(ConfigType.CONFIGS).getConfValue());
        else
            objectA.put(ConfigType.CONFIGS, "");

        if (getConfigs(ConfigType.CLICK_REGEX) != null)
            objectA.put(ConfigType.CLICK_REGEX, getConfigs(ConfigType.CLICK_REGEX).getConfValue());
        else
            objectA.put(ConfigType.CLICK_REGEX, "");

        if (getConfigs(ConfigType.PROTOCOL_FILTER) != null)
            objectA.put(ConfigType.PROTOCOL_FILTER, getConfigs(ConfigType.PROTOCOL_FILTER).getConfValue());
        else
            objectA.put(ConfigType.PROTOCOL_FILTER, "");

        if (getConfigs(ConfigType.REGEX_FILTER) != null)
            objectA.put(ConfigType.REGEX_FILTER, getConfigs(ConfigType.REGEX_FILTER).getConfValue());
        else
            objectA.put(ConfigType.REGEX_FILTER, "");

        if (getConfigs(ConfigType.SUFFIX_FILTER) != null)
            objectA.put(ConfigType.SUFFIX_FILTER, getConfigs(ConfigType.SUFFIX_FILTER).getConfValue());
        else
            objectA.put(ConfigType.SUFFIX_FILTER, "");

        if (getConfigs(ConfigType.PROXY) != null)
            objectA.put(ConfigType.PROXY, getConfigs(ConfigType.PROXY).getConfValue());
        else
            objectA.put(ConfigType.PROXY, "");

        if (getConfigs(ConfigType.TAGS) != null)
            objectA.put(ConfigType.TAGS, getConfigs(ConfigType.TAGS).getConfValue());
        else
            objectA.put(ConfigType.TAGS, "");

        if (getConfigs(ConfigType.CATEGORIES) != null)
            objectA.put(ConfigType.CATEGORIES, getConfigs(ConfigType.CATEGORIES).getConfValue());
        else
            objectA.put(ConfigType.CATEGORIES, "");

        objectB.put("base", this.entity);
        objectB.put("param", objectA);

        return objectB.toJSONString();

    }

}
