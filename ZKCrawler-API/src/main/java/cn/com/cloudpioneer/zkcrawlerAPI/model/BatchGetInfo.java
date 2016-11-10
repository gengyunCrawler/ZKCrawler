package cn.com.cloudpioneer.zkcrawlerAPI.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by TianyuanPan on 11/9/16.
 */
public class BatchGetInfo implements Serializable {

    private long id;
    private String idTask;
    private String nextSign = "";
    private String nextRow = "";
    private int lastSize = 0;
    private long allSize = 0;
    private Date updateTime;
    private Date createTime;

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

    public String getNextRow() {
        return nextRow;
    }

    public void setNextRow(String nextRow) {
        this.nextRow = nextRow;
    }

    public int getLastSize() {
        return lastSize;
    }

    public void setLastSize(int lastSize) {
        this.lastSize = lastSize;
    }

    public long getAllSize() {
        return allSize;
    }

    public void setAllSize(long allSize) {
        this.allSize = allSize;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String toJSONString() {

        return JSONObject.toJSONString(this);
    }
}
