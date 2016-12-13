package com.gy.wm.model.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by TianyuanPan on 12/7/2016.
 */

/** 种子配置格式
 *
 * {
 * "url":"http://url-01",
 * "sourceName":"source-name-01",
 * "categories":[
 *      {
 *          "name":"name-01",
 *          "categoryId":"category-id-01",
 *          "addTime":"2016-10-28 15:55:00"
 *      },
 *      {
 *          "name":"name-02",
 *          "categoryId":"category-id-02",
 *          "addTime":"2016-10-28 15:55:00"
 *      },
 *      {
 *          "name":"name-03",
 *          "categoryId":"category-id-03",
 *          "addTime":"2016-10-28 15:55:00"
 *      }
 *    ],
 * "tags":[
 *     {
 *        "name":"tag-name-01",
 *        "type":"base",
 *        "tagId":"tag-id-01",
 *        "addTime":"2016-11-17 12:32:00"
 *     },
 *     {
 *        "name":"tag-name-02",
 *        "type":"base",
 *        "tagId":"tag-id-02",
 *        "addTime":"2016-11-17 12:32:00"
 *     },
 *     {
 *       "name":"tag-name-03",
 *       "type":"base",
 *       "tagId":"tag-id-03",
 *       "addTime":"2016-11-17 12:32:00"
 *     }
 *   ]
 *  }

 */

public class SeedsInfo implements Serializable {

    private String url;
    private String sourceName;
    private JSONArray tags;
    private JSONArray categories;


    public SeedsInfo() {
        this.url = "";
        this.sourceName = "";
        this.tags = new JSONArray();
        this.categories = new JSONArray();
    }

    public SeedsInfo(String url, String sourceName) {
        this.url = url;
        this.sourceName = sourceName;
        this.tags = new JSONArray();
        this.categories = new JSONArray();
    }

    public SeedsInfo(String url, String sourceName, JSONArray tags, JSONArray categories) {
        this.url = url;
        this.sourceName = sourceName;
        this.tags = tags;
        this.categories = categories;
    }

    public SeedsInfo(String url, String sourceName, String tagsJSONStr, String categoriesJSONStr) {

        this.url = url;
        this.sourceName = sourceName;

        try {
            this.tags = JSONArray.parseArray(tagsJSONStr);
        } catch (Exception e) {
            this.tags = new JSONArray();
        }

        try {
            this.categories = JSONArray.parseArray(categoriesJSONStr);
        } catch (Exception e) {
            this.categories = new JSONArray();
        }
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

    public JSONArray getTags() {
        return tags;
    }

    public String tagsJSONString() {

        return tags.toJSONString();
    }

    public void setTags(JSONArray tags) {
        this.tags = tags;
    }

    public JSONArray getCategories() {
        return categories;
    }

    public String categoriesJSONString() {

        return categories.toJSONString();
    }

    public void setCategories(JSONArray categories) {
        this.categories = categories;
    }

    public String toJSONString() {

        return JSONObject.toJSONString(this);
    }
}
