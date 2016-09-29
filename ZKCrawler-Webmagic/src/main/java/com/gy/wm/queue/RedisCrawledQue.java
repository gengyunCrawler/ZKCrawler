package com.gy.wm.queue;

import com.gy.wm.model.CrawlData;
import com.gy.wm.util.JSONUtil;
import com.gy.wm.util.MySerializer;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class RedisCrawledQue {

    public void putCrawledQue(List<CrawlData> crawlData, Jedis jedis, String taskid) {

        for (CrawlData data : crawlData) {
            byte [] byte_crawlData = null;
            try {
                byte_crawlData = MySerializer.serialize(crawlData);
            } catch (Exception e) {
                e.printStackTrace();
            }
            jedis.hset(("webmagicCrawler::Crawled::" + taskid).getBytes(), data.getUrl().getBytes(),byte_crawlData);
        }
    }
}
