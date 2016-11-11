package com.gy.wm.entry;

import com.alibaba.fastjson.JSONObject;
import com.gy.wm.model.CrawlData;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务开始对种子url进行初始化
 */
public class ConfigLoader {

    //对种子对应的CrawlData进行赋值
    public List<CrawlData> load(String tid, String startTime, int pass, String type, List<String> seedingUrls, String tags) {
        List <CrawlData> crawlDataList = new ArrayList<CrawlData>();
        for(String seed : seedingUrls)  {
            CrawlData crawlData = new CrawlData();
            crawlData.setTid(tid);
            crawlData.setUrl(seed);
            crawlData.setStartTime(startTime);
            crawlData.setPass(pass);
            crawlData.setType(type);
            crawlData.setRootUrl(seed);
            crawlData.setFromUrl(seed);
            crawlData.setDepthfromSeed(0);

            JSONObject jsonObject = JSONObject.parseObject(tags);
            String tag = jsonObject.getString(seed);

            crawlData.setTag(tag);

            crawlDataList.add(crawlData);
        }
        return crawlDataList;
    }

}
