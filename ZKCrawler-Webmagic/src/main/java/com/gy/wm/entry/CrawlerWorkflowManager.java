package com.gy.wm.entry;

import com.gy.wm.dbpipeline.PipelineBloomFilter;
import com.gy.wm.dbpipeline.impl.EsPipeline;
import com.gy.wm.dbpipeline.impl.HbaseEsPipeline;
import com.gy.wm.dbpipeline.impl.HbasePipeline;
import com.gy.wm.model.CrawlData;
import com.gy.wm.parser.analysis.TextAnalysis;
import com.gy.wm.queue.RedisCrawledQue;
import com.gy.wm.queue.RedisToCrawlQue;
import com.gy.wm.schedular.RedisScheduler;
import com.gy.wm.service.WholesitePageProcessor;
import com.gy.wm.util.BloomFilter;
import com.gy.wm.util.GetDomain;
import com.gy.wm.util.JedisPoolUtils;
import com.gy.wm.util.LogManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class CrawlerWorkflowManager {
    private LogManager logger = new LogManager(CrawlerWorkflowManager.class);
    //初始化爬虫工厂是，用于解析的模板文件
    private TextAnalysis textAnalysis = InstanceFactory.getTextAnalysis();

    //待爬取队列
    private RedisToCrawlQue nextQueue = InstanceFactory.getRedisToCrawlQue();
    //已爬取队列
    private RedisCrawledQue crawledQueue = InstanceFactory.getRedisCrawledQue();

    private String tid;

    private String appname;

    private String domain;


    public CrawlerWorkflowManager(String tid, String appname) {
        this.tid = tid;
        this.appname = appname;
    }

    public void crawl(List<CrawlData> seeds, String tid, String starttime, int pass) throws IOException {

        JedisPoolUtils jedisPoolUtils = new JedisPoolUtils();
        JedisPool pool = jedisPoolUtils.getJedisPool();
        Jedis jedis = pool.getResource();
        domain = GetDomain.getDomain(seeds.get(0).getUrl());

        System.out.println("**********domain************: " + domain);
        try {
            nextQueue.putNextUrls(seeds, jedis, tid);
        } finally {
            pool.returnResource(jedis);
        }

        //初始化布隆过滤hash表
        BloomFilter bloomFilter = new BloomFilter(jedis, 1000, 0.001f, (int) Math.pow(2, 31));
        for (CrawlData seed : seeds) {
            bloomFilter.add("redis:bloomfilter:" + tid, seed.getUrl());
        }
        //初始化webMagic的Spider程序
        initSpider(seeds, textAnalysis, domain);

        //结束之后清空对应任务的redis
//        jedis.del("redis:bloomfilter:" + tid);
//        jedis.del("queue_" + tid);
//        jedis.del("webmagicCrawler::ToCrawl::" + tid);
//        jedis.del("webmagicCrawler::Crawled::" + tid);

    }

    protected void initSpider(List<CrawlData> seeds, TextAnalysis textAnalysis, String domain) {
        List<String> seedList = new ArrayList<>();
        for (CrawlData crawlData : seeds) {
            seedList.add(crawlData.getUrl());
        }
        String[] urlArray = seedList.toArray(new String[seedList.size()]);
        /* set the pipeline filter key */
        PipelineBloomFilter.setKeyInRedis(tid);

        Spider.create(new WholesitePageProcessor(tid, textAnalysis, domain))
                .setScheduler(new RedisScheduler(domain)).setUUID(tid)
                //从seed开始抓
                .addUrl(urlArray)
//                .addPipeline(new MysqlPipeline("tb_fbird", new FengBirdModel()))
                .addPipeline(new EsPipeline())
                .addPipeline(new HbaseEsPipeline())
                .addPipeline(new HbasePipeline())
                        //开启5个线程抓取
                .thread(10)
                        //启动爬虫
                .run();
    }
}
