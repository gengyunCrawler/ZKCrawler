package cn.com.cloudpioneer.worker.model;

import com.alibaba.fastjson.JSONObject;

import java.sql.Date;

/**
 * Created by Administrator on 2016/9/18.
 */
public class TaskConfigEntity {

    // 任务配置id，在数据库中为主键
    private long id;

    // 任务id，外键，参考任务表
    private String idTask;

    // 配置项名称
    private String confName;

    // 配置项类型
    private String confType;

    // 配置内容
    private String confValue;

    // 配置项添加或修改的时间
    private Date addDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdTask() {
        return idTask;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public String getConfName() {
        return confName;
    }

    public void setConfName(String confName) {
        this.confName = confName;
    }

    public String getConfType() {
        return confType;
    }

    public void setConfType(String confType) {
        this.confType = confType;
    }

    public String getConfValue() {
        return confValue;
    }

    public void setConfValue(String confValue) {
        this.confValue = confValue;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    @Override
    public String toString() {

        return JSONObject.toJSONString(this);
    }
}
