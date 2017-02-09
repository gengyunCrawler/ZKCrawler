package cn.com.cloudpioneer.worker.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by TianyuanPan on 1/11/2017.
 * <p>
 * JdeisPool 工具类,用于连接Redis数据库。
 */
public class JedisPoolUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisPoolUtil.class);
    private static JedisPool pool;


    private JedisPoolUtil() {

    }


    /**
     * 初始化JedisPool
     *
     * @param redisHost Redis主机
     * @param redisPort Redis端口
     */
    public static void initJedisPool(String redisHost, int redisPort) {

        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMaxTotal(-1);
        conf.setMaxWaitMillis(60000L);
        JedisPoolUtil.pool = new JedisPool(conf, redisHost, redisPort, 100000);
        LOGGER.info("初始化 JedisPool 信息: host [ " + redisHost + " ], port [ " + redisPort + " ]");
    }


    /**
     * 获取Jedis,此方法必须在初始化后才能调用，否则得空指针错误
     *
     * @return Jedis 对象。
     */
    public static Jedis getJedis() {

        return JedisPoolUtil.pool.getResource();
    }


    /**
     * 释放Jedis对象。
     *
     * @param jedis 要释放的Jedis对象。
     */
    public static void closeJedis(Jedis jedis) {

        JedisPoolUtil.pool.returnResource(jedis);
    }


}
