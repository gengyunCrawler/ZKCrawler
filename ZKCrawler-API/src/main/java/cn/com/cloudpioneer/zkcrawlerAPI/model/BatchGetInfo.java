package cn.com.cloudpioneer.zkcrawlerAPI.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by TianyuanPan on 11/9/16.
 */

/**
 * 数据获取的信息模型，和任务相关。一个爬虫任务对应于产生的数据，
 * 存于它自己的一张Hbase表中。此模型的一条记录，对应于一个任务。
 */
public class BatchGetInfo implements Serializable {

    private long id;                // 记录id，主键.
    private String idTask;          // 任务id，来源于爬虫任务表的任务id.
    private String nextSign = "";   // 获取数据标记 nextSign.
    private String nextRow = "";    // 此任务中 Hbase 的 nextRow.
    private int lastSize = 0;       // 最后一次在此任务中取得数据的条数.
    private long allSize = 0;       // 从此任务中取得数据的历史条数.
    private Date updateTime;        // 此任务记录的更新时间.
    private Date createTime;        // 此任务的创建时间.

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
