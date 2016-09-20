package com.gy.wm.entry;

import com.gy.wm.dbpipeline.MyHbaseUtils;
import com.gy.wm.model.CrawlData;
import com.gy.wm.model.TaskEntity;
import com.gy.wm.model.TaskParamModel;
import com.gy.wm.util.GetDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2016/5/18.
 */
@Service
public class Crawl {
    private static String nomalEndUri = ResourceBundle.getBundle("api").getString("nomalEndUri");

    private static final Logger LOG= LoggerFactory.getLogger(Crawl.class);

    public static void kick(int depth, int pass, String tid, String starttime, List<String> seedList, List<String> protocolList,
                            List<String> postregexList, String type, int recalldepth, List<String> templateList, List<String> clickregexList, List<String> configpath) throws Exception {
        //tid_startTime作为appname，即作为这个爬虫的任务名称
        InitCrawlerConfig crawlerConfig = new InitCrawlerConfig(tid + starttime, depth, templateList, clickregexList, protocolList, postregexList);
        InstanceFactory.getInstance(crawlerConfig);

        //种子的加载
        ConfigLoader configLoader = new ConfigLoader();
        List<CrawlData> crawlDataList = configLoader.load(tid, starttime, pass, type, seedList);

        //初始化HBASE
        String domain = GetDomain.getDomain(crawlDataList.get(0).getUrl());
        MyHbaseUtils.setTableName(domain); //hbase table info
        MyHbaseUtils.setColumnFamilyName("crawlerData");

        CrawlerWorkflowManager workflow = new CrawlerWorkflowManager(tid, "appname");
        workflow.crawl(crawlDataList, tid, starttime, pass);
    }

    public static void startTask(TaskParamModel taskParamModel) {
        long start_time = System.currentTimeMillis();
        int depth = taskParamModel.getBase().getDepthCrawl();
        int pass = taskParamModel.getBase().getPass();
        String tid = taskParamModel.getBase().getId();
        String starttime = ""+taskParamModel.getBase().getTimeStart();
        String type = ""+taskParamModel.getBase().getType();
        int recalldepth = taskParamModel.getBase().getDepthDynamic();

        List<String> seedList = taskParamModel.getParam().getSeedUrls();
        List<String> protocolList = taskParamModel.getParam().getProtocolFilter();
        List<String> postregexList = taskParamModel.getParam().getSuffixFilter();
        List<String> templateList = taskParamModel.getParam().getTemplates();
        List<String> clickregexList = taskParamModel.getParam().getClickRegex();
        List<String> configpath = taskParamModel.getParam().getConfigs();

        try {
            kick(depth, pass, tid, starttime, seedList, protocolList, postregexList, type, recalldepth, templateList, clickregexList, configpath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.info("***********************************it's finished******************************************");
        long end_time = System.currentTimeMillis();
        LOG.info("time elapse(seconds):" + ((end_time - start_time) / 1000.00));
//        HttpUtil.postMethod(nomalEndUri + taskEntity.getId());
    }
}
