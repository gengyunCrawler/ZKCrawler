package com.gy.wm.service;

import com.gy.wm.entry.Crawl;
import com.gy.wm.model.TaskParamModel;
import com.gy.wm.util.JedisPoolUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static ExecutorService executorService;

    @Autowired
    private Crawl crawl;

    static {
        int poolSize = 20;
        try {
            int i;
            poolSize = (i = Integer.parseInt(ResourceBundle.getBundle("config").getString("TASK_SERVICE_THREAD_POOL_SIZE"))) > poolSize ? i : poolSize;
            LOGGER.info("===> get thread pool size: " + poolSize);
        } catch (Exception e) {
            LOGGER.warn("===> get thread pool size error, use the default size: " + poolSize);
        }
        executorService = Executors.newFixedThreadPool(poolSize);
    }


    public static void taskExecutor(Runnable task) {
        if (!executorService.isShutdown()) {
            LOGGER.info("===> starting crawler task ......");
            executorService.execute(task);
            return;
        }
        LOGGER.warn("===> executorService was shutdown !!!! crawler task can't be started");
    }

    public TaskService() {
    }

    public void startTask(TaskParamModel taskParamModel) {
        this.crawl.startTask(taskParamModel);
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
            jedis.del("redis:bloomfilter:" + tid);
            jedis.del("queue_" + tid);
            jedis.del("webmagicCrawler::ToCrawl::" + tid);
            jedis.del("webmagicCrawler::Crawled::" + tid);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
        return tid+"has been cleaned";
    }

    public void test() {
        System.out.println("test");
    }
}
