package com.gy.wm.entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gy.wm.model.CrawlData;
import com.gy.wm.model.config.SeedUrl;
import com.gy.wm.model.config.SeedUrlsConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务开始对种子url进行初始化
 */
public class ConfigLoader {

    //对种子对应的CrawlData进行赋值
    public List<CrawlData> load(String tid, String startTime, int pass, String type, SeedUrlsConfig seedUrlsConfig, JSONArray tags, JSONArray categories) {
        List <CrawlData> crawlDataList = new ArrayList<>();
        List<SeedUrl> seedingUrls = seedUrlsConfig.getSeedUrls();
        for(SeedUrl seed : seedingUrls)  {
            CrawlData crawlData = new CrawlData();
            crawlData.setTid(tid);
            crawlData.setUrl(seed.getUrl());
            crawlData.setStartTime(startTime);
            crawlData.setPass(pass);
            crawlData.setType(type);
            crawlData.setRootUrl(seed.getUrl());
            crawlData.setFromUrl(seed.getUrl());
            crawlData.setDepthfromSeed(0);

            crawlData.setTag(seed.getSourceName());

            crawlData.setTags(tags);
            crawlData.setCategories(categories);

            crawlDataList.add(crawlData);
        }
        return crawlDataList;
    }

}
