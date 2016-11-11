package cn.com.cloudpioneer.zkcrawlerAPI.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by TianyuanPan on 11/10/16.
 */
public class BatchGetLog implements Serializable {

    private long id;
    private String idTask;
    private String nextSign;
    private String logInfo;
    private Date createTime;


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
