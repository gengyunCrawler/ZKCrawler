package com.gy.wm.queue;

import com.gy.wm.model.CrawlData;
import com.gy.wm.util.JSONUtil;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class RedisCrawledQue {

    public void putCrawledQue(List<CrawlData> crawlData, Jedis jedis, String taskid) {

        for (CrawlData data : crawlData) {
            String crawlDataJson = JSONUtil.object2JacksonString(data);
            jedis.hset("webmagicCrawler::Crawled::" + taskid, data.getUrl(),crawlDataJson);
        }
    }
}
