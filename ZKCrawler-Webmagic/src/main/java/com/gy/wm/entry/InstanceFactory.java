package com.gy.wm.entry;

import com.gy.wm.plugins.topicPlugin.analysis.BaseTemplate;
import com.gy.wm.plugins.wholesitePlugin.analysis.WholesiteTextAnalysis;
import com.gy.wm.plugins.wholesitePlugin.analysis.WholeSiteAnalysis;
import com.gy.wm.queue.RedisCrawledQue;
import com.gy.wm.queue.RedisToCrawlQue;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class InstanceFactory {
    private static InitCrawlerConfig CRAWL_CONFIG;

    private static InstanceFactory singleton;
    private static List<BaseTemplate> BASE_TEMPLATE;
    private static List<String> POST_REGEXS;
    private static List<String> PROTOCOLS;
    private static List<String> REGEXS;
    private static WholesiteTextAnalysis TEXT_ANALYSIS;

    private static RedisToCrawlQue REDIS_TO_CRAWL_QUE = new RedisToCrawlQue();
    private static RedisCrawledQue REDIS_CRAWLED_QUE = new RedisCrawledQue();

    private static WholeSiteAnalysis WHOLE_SITE_ANALYSIS = new WholeSiteAnalysis();

    public InstanceFactory(InitCrawlerConfig obj)   {
        CRAWL_CONFIG = obj;
        BASE_TEMPLATE = CRAWL_CONFIG.getListTemplate();
        POST_REGEXS = CRAWL_CONFIG.getPostRegex();
        PROTOCOLS = CRAWL_CONFIG.getProtocols();
        REGEXS = CRAWL_CONFIG.getRegexList();

    }

    /**
     * 在静态方法中按使用静态字段，多线程存在，字段同时被修改
     * @param object
     * @return
     */
    public static InstanceFactory getInstance(final InitCrawlerConfig object) {
        if (singleton == null) {
            synchronized (InstanceFactory.class) {
                if (singleton == null) {
                    singleton = new InstanceFactory(object);
                }
            }
        }
        return singleton;
    }

    public static InstanceFactory getSingleton() {
        return singleton;
    }

    public static void setSingleton(InstanceFactory singleton) {
        InstanceFactory.singleton = singleton;
    }

    public static List<BaseTemplate> getBaseTemplate() {
        return BASE_TEMPLATE;
    }

    public static void setBaseTemplate(List<BaseTemplate> baseTemplate) {
        BASE_TEMPLATE = baseTemplate;
    }

    public static List<String> getPostRegexs() {
        return POST_REGEXS;
    }

    public static void setPostRegexs(List<String> postRegexs) {
        POST_REGEXS = postRegexs;
    }

    public static List<String> getPROTOCOLS() {
        return PROTOCOLS;
    }

    public static void setPROTOCOLS(List<String> PROTOCOLS) {
        InstanceFactory.PROTOCOLS = PROTOCOLS;
    }

    public static List<String> getREGEXS() {
        return REGEXS;
    }

    public static void setREGEXS(List<String> REGEXS) {
        InstanceFactory.REGEXS = REGEXS;
    }

    public static WholesiteTextAnalysis getTextAnalysis() {
        return TEXT_ANALYSIS;
    }

    public static void setTextAnalysis(WholesiteTextAnalysis textAnalysis) {
        TEXT_ANALYSIS = textAnalysis;
    }

    public static RedisToCrawlQue getRedisToCrawlQue() {
        return REDIS_TO_CRAWL_QUE;
    }

    public static void setRedisToCrawlQue(RedisToCrawlQue redisToCrawlQue) {
        REDIS_TO_CRAWL_QUE = redisToCrawlQue;
    }

    public static RedisCrawledQue getRedisCrawledQue() {
        return REDIS_CRAWLED_QUE;
    }

    public static void setRedisCrawledQue(RedisCrawledQue redisCrawledQue) {
        REDIS_CRAWLED_QUE = redisCrawledQue;
    }

    public static WholeSiteAnalysis getWholeSiteAnalysis() {
        return WHOLE_SITE_ANALYSIS;
    }

    public static void setWholeSiteAnalysis(WholeSiteAnalysis wholeSiteAnalysis) {
        WHOLE_SITE_ANALYSIS = wholeSiteAnalysis;
    }


}
