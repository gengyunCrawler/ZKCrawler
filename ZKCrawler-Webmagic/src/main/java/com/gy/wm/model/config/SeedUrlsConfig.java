package com.gy.wm.model.config;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 12/7/2016.
 */

/**
 * SeedUrlsConfig 配置的 JSON 格式。
 * <p>
 * [
 * {
 * "url":"url",
 * "sourceName":"source name"
 * },
 * {
 * "url":"url",
 * "sourceName":"source name"
 * }
 * ]
 */

public class SeedUrlsConfig implements Serializable {

    List<SeedUrl> seedUrls;

    public SeedUrlsConfig() {

        seedUrls = new ArrayList<>();
    }

    public SeedUrlsConfig(String seedUrls) {

        try {
            this.seedUrls = JSONArray.parseArray(seedUrls, SeedUrl.class);
        } catch (Exception e) {
            this.seedUrls = new ArrayList<>();
        }
    }

    public List<SeedUrl> getSeedUrls() {
        return seedUrls;
    }

    public void setSeedUrls(List<SeedUrl> seedUrls) {
        this.seedUrls = seedUrls;
    }

    public String toJSONString() {

        return JSONArray.toJSONString(this.seedUrls);
    }
}
