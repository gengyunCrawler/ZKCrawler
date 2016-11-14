package cn.com.cloudpioneer.zkcrawlerAPI.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by TianyuanPan on 11/10/16.
 */

/**
 * 数据访问日志模型，每次取的任务，情况都要记录于此表中。
 */
public class BatchGetLog implements Serializable {

    private long id;            // 记录id，主键
    private String idTask;      // 任务id，来源于表BatchGetInfo的idTask.
    private String nextSign;    // 数据读取标记 nextSign.
    private String logInfo;     // 日志信息，内容为BatchGetInfo的JSON形式实体.
    private Date createTime;    // 日志创建时间。


    public BatchGetLog(){

        createTime = new Date();
    }

    public BatchGetLog(String idTask, String nextSign, String logInfo){

        this.idTask = idTask;
        this.nextSign = nextSign;
        this.logInfo = logInfo;
        this.createTime = new Date();
    }

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

    public String getNextSign() {
        return nextSign;
    }

    public void setNextSign(String nextSign) {
        this.nextSign = nextSign;
    }

    public String getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String toJSONString(){

        return JSONObject.toJSONString(this);
    }
}
