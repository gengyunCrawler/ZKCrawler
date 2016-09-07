package com.gy.wm.schedular;

import com.gy.wm.util.BloomFilter;
import redis.clients.jedis.Jedis;

/**
 * Created by Administrator on 2016/5/19.
 */
public class RedisBloomFilter {

    public static boolean notExistInBloomHash(String url,String tid, Jedis jedis,BloomFilter bloomFilter) {
        //如果key取得的value是空，或者url不包含在哈希地址中，可以插入
        if(!bloomFilter.contains("redis:bloomfilter:"+ tid,url))    {
            bloomFilter.add("redis:bloomfilter:"+ tid,url);
            return true;
        }else {
            return false;
        }
    }
}
