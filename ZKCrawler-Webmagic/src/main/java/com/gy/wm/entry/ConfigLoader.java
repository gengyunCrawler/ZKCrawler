package com.gy.wm.entry;

import com.gy.wm.model.CrawlData;
import com.gy.wm.model.config.SeedsConfig;
import com.gy.wm.model.config.SeedsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务开始对种子url进行初始化
 */
public class ConfigLoader {

    //对种子对应的CrawlData进行赋值
    public List<CrawlData> load(String tid, String startTime, int pass, String type, SeedsConfig seedsConfig) {
        List<CrawlData> crawlDataList = new ArrayList<>();
        List<SeedsInfo> seedsInfoList = seedsConfig.getSeedsInfoList();
        for (SeedsInfo seedInfo : seedsInfoList) {

            CrawlData crawlData = new CrawlData();

            crawlData.setTid(tid);

            crawlData.setUrl(seedInfo.getUrl());

            crawlData.setStartTime(startTime);

            crawlData.setPass(pass);

            crawlData.setType(type);

            crawlData.setRootUrl(seedInfo.getUrl());

            crawlData.setFromUrl(seedInfo.getUrl());

            crawlData.setDepthfromSeed(0);

            crawlData.setTag(seedInfo.getSourceName());

            crawlData.setTags(seedInfo.getTags());

            crawlData.setCategories(seedInfo.getCategories());

            crawlData.setSourceTypeId(seedInfo.getSourceTypeId());

            crawlData.setSourceRegion(seedInfo.getSourceRegion());


            crawlDataList.add(crawlData);
        }
        return crawlDataList;
    }

}
