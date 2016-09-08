package com.gy.wm.schedular;

import com.gy.wm.util.JedisPoolUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;

import java.io.IOException;

public class RedisScheduler implements Scheduler {
    private static final String QUEUE_PREFIX = "queue_";
    private String domain;

    public RedisScheduler(String domain)   {
        this.domain = domain;
    }

    @Override
    public void push(Request request, Task task) {

        JedisPoolUtils jedisPoolUtils = null;
        try {
            jedisPoolUtils = new JedisPoolUtils();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JedisPool pool = jedisPoolUtils.getJedisPool();
        Jedis jedis = pool.getResource();

        try {
            jedis.rpush(QUEUE_PREFIX + task.getUUID(), request.getUrl());
        } finally {
            pool.returnResource(jedis);
        }
    }


    @Override
    public Request poll(Task task) {

        JedisPoolUtils jedisPoolUtils = null;
        try {
            jedisPoolUtils = new JedisPoolUtils();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JedisPool pool = jedisPoolUtils.getJedisPool();
        Jedis jedis = pool.getResource();


        String url = null;
        try {
            url = jedis.lpop(QUEUE_PREFIX+task.getUUID());
        } finally {
            pool.returnResource(jedis);
        }

        if (url==null) {
            return null;
        } else {
            return new Request(url);
        }
    }
}