package com.gy.wm.entry;

import com.gy.wm.model.CrawlData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/3.
 */
public class ConfigLoader {

    //对种子对应的CrawlData进行赋值
    public List<CrawlData> load(String tid, String startTime, int pass, String type, List<String> seedingUrls) {
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

            crawlDataList.add(crawlData);
        }
        return crawlDataList;
    }

}
