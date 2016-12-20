package com.gy.wm.queue;

import com.gy.wm.model.CrawlData;
import com.gy.wm.util.JSONUtil;
import com.gy.wm.util.LogManager;
import com.gy.wm.util.MySerializer;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class RedisToCrawlQue {
    private transient static LogManager logger = new LogManager(RedisToCrawlQue.class);

    public void putNextUrls(List<CrawlData> crawlData,Jedis jedis, String tid ) {
        for (CrawlData nextCrawlData : crawlData) {
            byte [] byte_crawlData = null;
            try {
                byte_crawlData = MySerializer.serialize(nextCrawlData);
            } catch (Exception e) {
                logger.logInfo("序列化放入带爬取队列的url 发生异常！！！");
                e.printStackTrace();
            }
            jedis.hset(("webmagicCrawler::ToCrawl::" + tid).getBytes(), nextCrawlData.getUrl().getBytes(), byte_crawlData);
        }
    }
}
