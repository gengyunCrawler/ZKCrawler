package com.gy.wm.util;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.Serializable;


/**
 * Created by 黄海 on 15-12-30.
 */

public class JedisPoolUtils implements Serializable {
    private static JedisPool pool;

    public JedisPoolUtils() throws IOException {
        makepool();
    }

    public static void makepool() throws IOException {

        String redisHost = ConfigUtils.getResourceBundle().getString("REDIS_HOSTNAME");
        int    redisPort = Integer.parseInt(ConfigUtils.getResourceBundle().getString("REDIS_PORT"));
        if (pool == null) {
            JedisPoolConfig conf = new JedisPoolConfig();
            conf.setMaxTotal(-1);
            conf.setMaxWaitMillis(60000L);
            pool = new JedisPool(conf, redisHost, redisPort,100000);
        }
    }

    public static JedisPool getJedisPool() {
        return pool;
    }
}

