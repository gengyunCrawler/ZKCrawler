package com.gy.wm.plugins.newsExportPlugin.parse;

/**
 * Created by Administrator on 2016/9/30.
 */
public class ParserEntity {
    private int id;
    private String config;
    private String tid;
    private String viewAll;
    private String nextKey;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getViewAll() {
        return viewAll;
    }

    public void setViewAll(String viewAll) {
        this.viewAll = viewAll;
    }

    public String getNextKey() {
        return nextKey;
    }

    public void setNextKey(String nextKey) {
        this.nextKey = nextKey;
    }
}