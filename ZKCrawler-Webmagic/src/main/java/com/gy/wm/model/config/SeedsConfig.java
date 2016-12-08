package com.gy.wm.model.config;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 12/7/2016.
 */

/**
 * SeedsConfig 配置的 JSON 格式。
 * <p>
 * [
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
 * ]
 */

public class SeedsConfig implements Serializable {

    private List<SeedsInfo> seedsInfoList;

    public SeedsConfig() {

        seedsInfoList = new ArrayList<>();
    }

    public SeedsConfig(String seedUrls) {

        try {
            this.seedsInfoList = JSONArray.parseArray(seedUrls, SeedsInfo.class);
        } catch (Exception e) {
            this.seedsInfoList = new ArrayList<>();
        }
    }

    public List<SeedsInfo> getSeedsInfoList() {
        return seedsInfoList;
    }

    public void setSeedsInfoList(List<SeedsInfo> seedsInfoList) {
        this.seedsInfoList = seedsInfoList;
    }

    public String toJSONString() {

        return JSONArray.toJSONString(this.seedsInfoList);
    }
}
