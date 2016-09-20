package com.gy.wm.vo;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <类详细说明:任务参数,包括一些不可变参数，如taskid,一些可变的回写参数，如完成次数completeTimes,
 * 总共21个参数>
 *
 * @Author： Huanghai
 * @Version: 2016-09-20
 **/
@Component
public class Base {
    private int completeTimes;

    private int costLastCrawl;

    private int cycleRecrawl;

    private boolean deleteFlag;

    private int depthCrawl;

    private int depthDynamic;

    private String id;

    //idUser为保留字段
    private Long idUser;

    private String name;

    private int pass;

    private String remark;

    private int scheduleType;

    private int status;

    private int threads;

    private Date timeLastCrawl;

    private Date timeStart;

    private Date timeStop;

    private int type;

    private int weight;

    private int workNum;

    private Date createDate;

    public int getCompleteTimes() {
        return completeTimes;
    }

    public void setCompleteTimes(int completeTimes) {
        this.completeTimes = completeTimes;
    }

    public int getCostLastCrawl() {
        return costLastCrawl;
    }

    public void setCostLastCrawl(int costLastCrawl) {
        this.costLastCrawl = costLastCrawl;
    }

    public int getCycleRecrawl() {
        return cycleRecrawl;
    }

    public void setCycleRecrawl(int cycleRecrawl) {
        this.cycleRecrawl = cycleRecrawl;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public int getDepthCrawl() {
        return depthCrawl;
    }

    public void setDepthCrawl(int depthCrawl) {
        this.depthCrawl = depthCrawl;
    }

    public int getDepthDynamic() {
        return depthDynamic;
    }

    public void setDepthDynamic(int depthDynamic) {
        this.depthDynamic = depthDynamic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(int scheduleType) {
        this.scheduleType = scheduleType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public Date getTimeLastCrawl() {
        return timeLastCrawl;
    }

    public void setTimeLastCrawl(Date timeLastCrawl) {
        this.timeLastCrawl = timeLastCrawl;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(Date timeStop) {
        this.timeStop = timeStop;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWorkNum() {
        return workNum;
    }

    public void setWorkNum(int workNum) {
        this.workNum = workNum;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
