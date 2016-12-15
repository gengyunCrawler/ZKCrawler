package com.gy.wm.model;

/**
 * Created by Administrator on 2016/12/15.
 */
public class WxArticle {
    private int id;
    private String title;
    private String content;
    private String wxName;
    private String url;
    private String wxBiz;

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    private String tag;
    private String author;
    private String publishTime;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public WxArticle() {

    }

    public WxArticle( String title, String content, String wxName, String url, String wxBiz, String tag,String author,String publishTime) {
        this.title = title;
        this.content = content;
        this.wxName = wxName;
        this.url = url;
        this.wxBiz = wxBiz;
        this.tag = tag;
        this.author = author;
        this.publishTime=publishTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWxBiz() {
        return wxBiz;
    }

    public void setWxBiz(String wxBiz) {
        this.wxBiz = wxBiz;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
