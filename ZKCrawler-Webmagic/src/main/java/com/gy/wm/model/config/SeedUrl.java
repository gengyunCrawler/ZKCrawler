package com.gy.wm.model.config;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by TianyuanPan on 12/7/2016.
 */

public class SeedUrl implements Serializable{

    private String url;
    private String sourceName;


    public SeedUrl(){}
    public SeedUrl(String url, String sourceName){
        this.url = url;
        this.sourceName = sourceName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String toJSONString(){

        return JSONObject.toJSONString(this);
    }
}
