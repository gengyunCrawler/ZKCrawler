package com.gy.wm.service;

import com.gy.wm.entry.InstanceFactory;
import com.gy.wm.model.CrawlData;
import com.gy.wm.queue.RedisCrawledQue;
import com.gy.wm.queue.RedisToCrawlQue;
import com.gy.wm.schedular.RedisBloomFilter;
import com.gy.wm.util.*;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 网页抓取器。
 *
 * @author yinlei
 *         2014-3-5 下午4:27:51
 */
@Component
public class CustomPageProcessor implements PageProcessor {
    private String tid;
    private String domain;
    //解析的深度，对应爬虫任务设置的深度
    private final int DEPTH = InstanceFactory.getCrawlConfig().getDepth();

    public CustomPageProcessor(String tid, String domain) {
        this.tid = tid;
        this.domain = domain;
    }

    public CustomPageProcessor()    {

    }

    private Site site = Site.me().setDomain(domain).setRetryTimes(3).setSleepTime(1000).setTimeOut(1000);

    @Override
    public void process(Page page) {
        JedisPoolUtils jedisPoolUtils = null;
        JedisPool pool = null;
        Jedis jedis = null;
        Jedis bloomJedis = null;
        try {
            jedisPoolUtils = new JedisPoolUtils();
            pool = jedisPoolUtils.getJedisPool();
            jedis = pool.getResource();
            bloomJedis = pool.getResource();
            bloomJedis.select(1);

            byte [] byte_crawlData = jedis.hget(("webmagicCrawler::ToCrawl::" + tid).getBytes(), page.getRequest().getUrl().getBytes());
            CrawlData page_crawlData = null;
            try {
                page_crawlData = (CrawlData) MySerializer.deserialize(byte_crawlData);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            jedis.hdel(("webmagicCrawler::ToCrawl::" + tid).getBytes(), page.getRequest().getUrl().getBytes());

            int statusCode = page.getStatusCode();
            String html = page.getHtml().toString();

            //对源码和访问状态码进行赋值
            page_crawlData.setHtml(html);
            page_crawlData.setStatusCode(statusCode);

            /**
             * 通过反射拿到解析类并执行解析方法
             * 在PluginUtil中定义了插件名称制定路径
             */
            List<CrawlData> perPageCrawlDataList = null;
            try {
                perPageCrawlDataList = new PluginUtil().excutePluginParse(page_crawlData);
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<CrawlData> nextCrawlData = new ArrayList<>();
            List<CrawlData> crawledData = new ArrayList<>();

            BloomFilter bloomFilter = new BloomFilter(bloomJedis, 1000, 0.001f, (int) Math.pow(2, 31));
            for (CrawlData crawlData : perPageCrawlDataList) {
                if(crawlData.getDepthfromSeed() <= DEPTH)    {
                    if (!crawlData.isFetched()) {
                        //链接fetched为false,即导航页,bloomFilter判断待爬取队列没有记录
                        boolean isNew = RedisBloomFilter.notExistInBloomHash(crawlData.getUrl(), tid, bloomJedis, bloomFilter);
                        if (isNew && URLFilter.linkFilter(crawlData.getUrl()) && URLFilter.matchDomain(crawlData.getUrl(), domain)) {
                            nextCrawlData.add(crawlData);
                            page.addTargetRequest(crawlData.getUrl());
                        }
                    } else {
                        //链接fetched为true,即文章页，添加到redis的已爬取队列
//                        crawledData.add(crawlData);
                        page.putField("crawlerData", crawlData);
                    }
                }
            }

            RedisToCrawlQue nextQueue = InstanceFactory.getRedisToCrawlQue();

            //加入到待爬取队列
            nextQueue.putNextUrls(nextCrawlData, jedis, tid);
            //加入到已爬取队列
            new RedisCrawledQue().putCrawledQue(crawledData, jedis, tid);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
            pool.returnResource(bloomJedis);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}

