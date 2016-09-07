package com.gy.wm.heartbeat.model;


import com.gy.wm.heartbeat.message.Message;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public class HeartbeatMsgModel implements Message{

    private String taskId;
    private String hostname;
    private int pid;
    private int theads;
    private long time;
    private int statusCode;
    private int    timeoutCount;

    public HeartbeatMsgModel() {

        hostname = "";
        hostname = "";
        pid = -1;
        theads = 1;
        statusCode = -1;
        timeoutCount = 0;
    }

    public String getTaskId() {
        return taskId;
    }

    public HeartbeatMsgModel setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public HeartbeatMsgModel setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public int getPid() {
        return pid;
    }

    public HeartbeatMsgModel setPid(int pid) {
        this.pid = pid;
        return this;
    }

    public int getTheads() {
        return theads;
    }

    public HeartbeatMsgModel setTheads(int theads) {
        this.theads = theads;
        return this;
    }

    public long getTime() {
        return time;
    }

    public HeartbeatMsgModel setTime(long time) {
        this.time = time;
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public HeartbeatMsgModel setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public int getTimeoutCount() {
        return timeoutCount;
    }

    public HeartbeatMsgModel setTimeoutCount(int timeoutCount) {
        this.timeoutCount = timeoutCount;
        return this;
    }

    @Override
    public Message getMessage() {

        return this;
    }
}
