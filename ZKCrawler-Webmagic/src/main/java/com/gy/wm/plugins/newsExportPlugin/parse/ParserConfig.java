package com.gy.wm.plugins.newsExportPlugin.parse;

import java.util.List;

/**
 * Created by tijun on 2016/9/30.
 * @author TijunWang
 */
public class ParserConfig {
    //start url
    public static final String URL_PARTTERN_TYPE_START="START";
    // find links which suitable with link regex
    public static final String URL_PARTTERN_TYPE_COLUMN_REGEX="COLUMN_REGEX";
    //judge the page is our content page
    public static final String HTML_LINK_REGEX="CONTENT_LINK_REGEX";
    //all fieds are needed to find from html
    private List<HtmlField>fields ;
    //start url and url regex
    private List<UrlPattern> urlPatterns;
    private int id;
    private String taskId;

    private String imagePrefix;

    public String getImagePrefix() {
        return imagePrefix;
    }

    public void setImagePrefix(String imagePrefix) {
        this.imagePrefix = imagePrefix;
    }


    public List<HtmlField> getFields() {
        return fields;
    }

    public void setFields(List<HtmlField> fields) {
        this.fields = fields;
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
