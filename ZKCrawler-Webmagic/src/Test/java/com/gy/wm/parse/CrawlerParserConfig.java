package com.gy.wm.parse;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class CrawlerParserConfig {
    private List<HtmlFiled> fileds;
    private List<UrlPattern> urlPatterns;
    private int id;
    private String taskId;

    public List<HtmlFiled> getFileds() {
        return fileds;
    }

    public void setFileds(List<HtmlFiled> fileds) {
        this.fileds = fileds;
    }

    public List<UrlPattern> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(List<UrlPattern> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
