package com.gy.wm.entry;

import com.alibaba.fastjson.JSONArray;
import com.gy.wm.dbpipeline.MyHbaseUtils;
import com.gy.wm.model.CrawlData;
import com.gy.wm.model.TaskParamModel;
import com.gy.wm.model.config.*;
import com.gy.wm.util.GetDomain;
import com.gy.wm.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2016/5/18.
 */
@Service
@Scope("prototype")
public class Crawl {

    private static String nomalEndUri = ResourceBundle.getBundle("api").getString("nomalEndUri");
    private static final String DOWNLOAD_PLUGIN_NAME = ResourceBundle.getBundle("config").getString("donwloadPluginName");
    private static final Logger LOGGER = LoggerFactory.getLogger(Crawl.class);

    private void kick(String tid, String type, String startTime, int depth, int pass,
                      SeedUrlsConfig seedUrlsConfig, TemplatesConfig templatesConfig,
                      ProtocolConfig protocolConfig, PostRegexConfig postRegexConfig,
                      ClickRegexConfig clickRegexConfig, JSONArray tags, JSONArray categories) throws Exception {

        //tid_startTime作为appname，即作为这个爬虫的任务名称
        InitCrawlerConfig crawlerConfig = new InitCrawlerConfig(
                tid + startTime, depth, templatesConfig.getTemplates(),
                new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());

        InstanceFactory.getInstance(crawlerConfig);

        //种子的加载
        ConfigLoader configLoader = new ConfigLoader();
        List<CrawlData> crawlDataList = configLoader.load(tid, startTime, pass, type, seedUrlsConfig, tags, categories);

        //初始化HBASE
        String domain = GetDomain.getDomain(crawlDataList.get(0).getUrl());
        MyHbaseUtils.setTableName(domain); //hbase table info
        MyHbaseUtils.setColumnFamilyName("crawlerData");

        CrawlerWorkflowManager workflow = new CrawlerWorkflowManager(tid, "appname");
        workflow.crawl(crawlDataList, tid, startTime, pass);
    }

    public void startTask(TaskParamModel taskParamModel) {

        long startTimeLong = System.currentTimeMillis();

        int depth = taskParamModel.getBase().getDepthCrawl();

        int pass = taskParamModel.getBase().getPass();

        String tid = taskParamModel.getBase().getId();

        String startTime = "" + taskParamModel.getBase().getTimeStart();

        String type = "" + taskParamModel.getBase().getType();

        int recallDepth = taskParamModel.getBase().getDepthDynamic();


        JSONArray jsonArrayTags;
        JSONArray jsonArrayCategories;
        SeedUrlsConfig seedUrlsConfig;
        TemplatesConfig templatesConfig;


        String tags = taskParamModel.getParam().getTags();
        String categories = taskParamModel.getParam().getCategories();
        String seedUrls = taskParamModel.getParam().getSeedUrls();
        String templates = taskParamModel.getParam().getTemplates();

        //String protocolList = taskParamModel.getParam().getProtocolFilter();
        //String postregexList = taskParamModel.getParam().getSuffixFilter();
        //String clickregexList = taskParamModel.getParam().getClickRegex();
        //String configpath = taskParamModel.getParam().getConfigs();

        seedUrlsConfig = new SeedUrlsConfig(seedUrls);
        if (!(seedUrlsConfig.getSeedUrls().size() > 0)) {
            LOGGER.error("解析种子配置(seedUrls)出错,无法启动爬取任务,也许是由于种子配置的JSON语法错误导致.");
            return;
        }

        try {
            jsonArrayTags = JSONArray.parseArray(tags);
        } catch (Exception e) {
            LOGGER.warn("解析标签配置出错,请检查任务的标签配置,出错的原因可能是JSON语法错误导致.", e);
            jsonArrayTags = new JSONArray();
        }
        try {
            jsonArrayCategories = JSONArray.parseArray(categories);
        } catch (Exception e) {
            LOGGER.warn("解析分类(栏目,频道)标签配置出错,请检查分类(栏目,频道)的标签配置,出错的原因可能是JSON语法错误导致.", e);
            jsonArrayCategories = new JSONArray();
        }


        templatesConfig = new TemplatesConfig(templates);
        if (!(templatesConfig.getTemplates().size() > 0)) {
            LOGGER.warn("解析模板配置(templates)出错,这会导致爬取任务解析数据时不正常.");
        }


        try {

            kick(tid, type, startTime, depth, pass, seedUrlsConfig,
                    templatesConfig, new ProtocolConfig(), new PostRegexConfig(),
                    new ClickRegexConfig(), jsonArrayTags, jsonArrayCategories);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("爬取任务完成, 域名: [" + GetDomain.getDomain(seedUrls) + "], 任务ID(taskId): [" + tid + "]");
        long endTimeLong = System.currentTimeMillis();
        LOGGER.info("本次爬取任务花费时间(秒): " + ((endTimeLong - startTimeLong) / 1000.00));

        // 向worker发送任务结束信息
        HttpUtil.postMethod(nomalEndUri + taskParamModel.getBase().getId());
    }
}
