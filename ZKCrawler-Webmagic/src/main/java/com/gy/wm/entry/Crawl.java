package com.gy.wm.entry;

import com.gy.wm.dbpipeline.MyHbaseUtils;
import com.gy.wm.model.CrawlData;
import com.gy.wm.model.TaskEntity;
import com.gy.wm.util.GetDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
@Service
public class Crawl {
    private static final Logger LOG= LoggerFactory.getLogger(Crawl.class);

    public static void kick(int depth, int pass, String tid, String starttime, String seedpath, String protocolDir,
                            String postregexDir, String type, int recalldepth, String templatesDir, String clickregexDir, String configpath) throws Exception {
        //tid_startTime作为appname，即作为这个爬虫的任务名称
        InitCrawlerConfig crawlerConfig = new InitCrawlerConfig(tid + starttime, recalldepth, templatesDir, clickregexDir, protocolDir, postregexDir);
        InstanceFactory.getInstance(crawlerConfig);

        //种子的加载
        ConfigLoader configLoader = new ConfigLoader();
        List<CrawlData> crawlDataList = configLoader.load(depth, tid, starttime, pass, seedpath, type);

        //初始化HBASE
        String domain = GetDomain.getDomain(crawlDataList.get(0).getUrl());
        MyHbaseUtils.setTableName(domain); //hbase table info
        MyHbaseUtils.setColumnFamilyName("crawlerData");

        CrawlerWorkflowManager workflow = new CrawlerWorkflowManager(tid, "appname");
        workflow.crawl(crawlDataList, tid, starttime, pass);
    }

    public static void startTask(TaskEntity taskEntity) {
        long start_time = System.currentTimeMillis();
        int depth = taskEntity.getDepthCrawl();
        int pass = taskEntity.getPass();
        String tid = taskEntity.getId();
        String starttime = String.valueOf(taskEntity.getTimeStart());
        String seedpath = taskEntity.getPathSeeds();
        String protocolDir = taskEntity.getPathProtocolFilter();
        String postregexDir = taskEntity.getPathSuffixFilter();
        String type = String.valueOf(taskEntity.getType());
        int recalldepth = taskEntity.getDepthDynamic();
        String templateDir = taskEntity.getPathTemplates();
        String clickregexDir = taskEntity.getPathRegexFilter();
        String configpath = taskEntity.getPathConfigs();

        try {
            kick(depth, pass, tid, starttime, seedpath, protocolDir, postregexDir, type, recalldepth, templateDir, clickregexDir, configpath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long end_time = System.currentTimeMillis();
        LOG.info("time elapse(seconds):" + ((end_time - start_time) / 1000.00));

    }
}
