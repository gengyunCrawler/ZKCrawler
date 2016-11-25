package com.gy.wm.service;

import com.gy.wm.entry.Crawl;
import com.gy.wm.model.TaskParamModel;
import com.gy.wm.util.JedisPoolUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <类详细说明:work处理webmagic 任务请求>
 *
 * @Author： Huanghai
 * @Version: 2016-09-09
 **/
@Service
@Component
@Scope("prototype")
public class TaskService {

    private static final Logger LOGGER = Logger.getLogger(TaskService.class);

    @Autowired
    private Crawl crawl;

    @Async
    public Future<Long> startTask(TaskParamModel taskParamModel) {
        this.crawl.startTask(taskParamModel);
        LOGGER.info("异步返回：任务 "+taskParamModel.getBase().getId()+" 已完成！");
        LOGGER.info("异步中的线程名称： " + java.lang.Thread.currentThread().getName() + "异步中的线程id： " + Thread.currentThread().getId());
        return new AsyncResult<>(java.lang.Thread.currentThread().getId());
    }

    public String cleanTaskRedis(String tid)  {
        JedisPoolUtils jedisPoolUtils = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            jedisPoolUtils = new JedisPoolUtils();
            pool = jedisPoolUtils.getJedisPool();
            jedis = pool.getResource();

            //结束之后清空对应任务的redis
            jedis.del("queue_" + tid);
            jedis.del(("webmagicCrawler::ToCrawl::" + tid).getBytes());
            jedis.del(("webmagicCrawler::Crawled::" + tid).getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
        return tid + "has been cleaned";
    }

    public void test() {
        System.out.println("test");
    }
}
