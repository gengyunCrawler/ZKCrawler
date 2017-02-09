package cn.com.cloudpioneer.worker.tasks;

import cn.com.cloudpioneer.worker.app.ValueDef;
import cn.com.cloudpioneer.worker.model.TaskModel;
import cn.com.cloudpioneer.worker.utils.JedisPoolUtil;
import cn.com.cloudpioneer.worker.utils.ObjectSerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 2016/9/6.
 * <p>
 * 此类是 worker 用于存储自己当前的所有任务。
 * 内容存储在 Redis 里的 hash 表
 */
public class MyTasks {

    // 日志
    private static final Logger LOGGER = LoggerFactory.getLogger(MyTasks.class);

    /**
     * Redis 里的哈希表的键
     */
    private static String key;

    /**
     * 操作 Redis 的成员
     */
    private Jedis jedis;


    /**
     * 构造方法
     *
     * @param key 哈希表的键
     */
    public MyTasks(String key) {

        MyTasks.key = key;

    }


    /**
     * 添加任务到 worker 的任务列表中。
     *
     * @param task 要添加的任务。
     */
    public boolean addTask(TaskModel task) {

        jedis = JedisPoolUtil.getJedis();

        try {

            jedis.select(ValueDef.REDIS_INDEX_MY_TASKS_TABLE);
            byte[] bytesTaskModel = ObjectSerializeUtils.serializeObjectToBytes(task);
            if (null == bytesTaskModel) {
                LOGGER.warn("method addTask, serializeObjectToBytes return value is null.");
                return false;
            }

            return jedis.hset(key.getBytes(), task.getEntity().getId().getBytes(), bytesTaskModel) > 0;

        } catch (Exception e) {
            LOGGER.warn("method addTask, jedis operation Exception! ", e);
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }

        return false;
    }


    /**
     * 从字典中移除任务。
     *
     * @param id 任务ID
     * @return 移除的任务，若任务不存在获取出错，则为 null。
     */
    public TaskModel removeTask(String id) {

        jedis = JedisPoolUtil.getJedis();

        try {
            jedis.select(ValueDef.REDIS_INDEX_MY_TASKS_TABLE);

            byte[] bytes = jedis.hget(key.getBytes(), id.getBytes());

            if (null == bytes) {
                LOGGER.warn("method removeTask, hget return is null, key: " + key + ", field: " + id);
                return null;
            }

            if (!(jedis.hdel(key.getBytes(), id.getBytes()) > 0)) {
                LOGGER.warn("method removeTask, hdel FAILED, key: " + key + ", field: " + id);
            }

            Object object = ObjectSerializeUtils.buildObjectFromBytes(bytes);

            if (null == object) {
                LOGGER.warn("method removeTask, buildObjectFromBytes return is null.");
                return null;
            }

            return (TaskModel) object;

        } catch (Exception e) {

            LOGGER.warn("method removeTask, jedis operation Exception! ", e);

        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }

        return null;
    }


    /**
     * 获取任务数量。
     *
     * @return 任务数量。
     */
    public long getMyTasksSize() {

        jedis = JedisPoolUtil.getJedis();

        try {
            jedis.select(ValueDef.REDIS_INDEX_MY_TASKS_TABLE);
            return jedis.hlen(key.getBytes());
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }


    /**
     * 表中是否包含此 field
     *
     * @param field
     * @return 包含 true 否则 false
     */
    public boolean containsField(String field) {

        jedis = JedisPoolUtil.getJedis();

        try {
            jedis.select(ValueDef.REDIS_INDEX_MY_TASKS_TABLE);
            return jedis.hexists(key.getBytes(), field.getBytes());
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }


    /**
     * 获取当前所有任务
     *
     * @return 任务列表。
     */
    public List<TaskModel> getTasks() {

        List<TaskModel> taskModels = new ArrayList<>();

        jedis = JedisPoolUtil.getJedis();

        try {

            jedis.select(ValueDef.REDIS_INDEX_MY_TASKS_TABLE);

            Map<byte[], byte[]> map = jedis.hgetAll(key.getBytes());

            for (byte[] bytes : map.keySet()) {

                Object object = ObjectSerializeUtils.buildObjectFromBytes(map.get(bytes));
                if (null != object) {
                    taskModels.add((TaskModel) object);
                } else {

                    LOGGER.warn("method getTasks, buildObjectFromBytes return is null.");
                }
            }

        } catch (Exception e) {

            LOGGER.warn("method removeTask, jedis operation Exception! ", e);

        } finally {

            JedisPoolUtil.closeJedis(jedis);
        }

        return taskModels;
    }

}
