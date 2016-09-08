package com.gy.wm.util;

import com.gy.wm.model.CrawlData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/24.
 */
public class ScanRedis
{
    public void getRedisData()  {

        JedisPoolUtils jedisPoolUtils = null;
        JedisPool pool = null;
        Jedis jedis = null;

        try {
            jedisPoolUtils = new JedisPoolUtils();
            pool = jedisPoolUtils.getJedisPool();
            jedis = pool.getResource();
            String key = "webmagicCrawler::ToCrawl::WholesiteCrawler2016-6-23";
            Map<String,String> map = new HashMap<>();

            map = jedis.hgetAll(key);
            for (String s : map.keySet()) {
                String rootUrl = ((CrawlData) JSONUtil.jackson2Object(map.get(s), CrawlData.class)).getRootUrl();
                String fromUrl = ((CrawlData) JSONUtil.jackson2Object(map.get(s), CrawlData.class)).getFromUrl();
                String url = ((CrawlData) JSONUtil.jackson2Object(map.get(s), CrawlData.class)).getUrl();
                String html = ((CrawlData) JSONUtil.jackson2Object(map.get(s), CrawlData.class)).getHtml();
                System.out.println("rootUrl:" + rootUrl + '\t' + "fromUrl:" + fromUrl + '\t' + "url:"+ url + '\n' + "html:" + html );
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    public static void main(String[] args) {
        new ScanRedis().getRedisData();
    }
}
