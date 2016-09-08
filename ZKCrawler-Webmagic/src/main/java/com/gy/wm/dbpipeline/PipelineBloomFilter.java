package com.gy.wm.dbpipeline;

import com.gy.wm.util.HashUtils;
import redis.clients.jedis.Jedis;

/**
 * Created by TianyuanPan on 8/4/16.
 */
public class PipelineBloomFilter {

    private static final int REDIS_INDEX = 6;
    private int maxKey;
    private float errorRate;
    private int hashFunctionCount;
    private Jedis jedis;
    private int bitSize;
    private static String keyInRedis;

    static {

        keyInRedis = "key::pipeline::filter";
    }
    public PipelineBloomFilter(Jedis jedis, float errorRate, int maxKey) {
        this.maxKey = maxKey;
        this.errorRate = errorRate;
        this.jedis = jedis;
        this.jedis.select(REDIS_INDEX);
        bitSize = calcOptimalM(maxKey, errorRate);
        hashFunctionCount = calcOptimalK(bitSize, maxKey);
    }


    /**
     * 当前key中添加标记
     *
     * @param bizId
     */

    public void add(String bizId) {
        int[] offset = HashUtils.murmurHashOffset(bizId, hashFunctionCount, bitSize);
        for (int i : offset) {
            jedis.setbit(this.keyInRedis, i, true);
        }

//        jedis.save();
    }

    /**
     * 检测字符串是否存在于集合中
     *
     * @param bizId
     */
    public boolean contains(String bizId) {
        int[] offset = HashUtils.murmurHashOffset(bizId, hashFunctionCount, bitSize);
        for (int i : offset) {
            if (!jedis.getbit(this.keyInRedis, i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算M和K
     * M:位数组的大小
     * K:哈希函数的个数
     *
     * @param maxKey
     * @param errorRate
     * @return
     */
    private int calcOptimalM(int maxKey, float errorRate) {
        return (int) Math.ceil(maxKey
                * (Math.log(errorRate) / Math.log(0.6185)));
    }

    /**
     * 计算M和K
     * M:为数组的大小
     * K:哈希函数的个数
     *
     * @param bitSize
     * @param maxKey
     * @return
     */
    private int calcOptimalK(int bitSize, int maxKey) {
        return (int) Math.ceil(Math.log(2) * (bitSize / maxKey));
    }


    public int getMaxKey() {
        return maxKey;
    }

    public void setMaxKey(int maxKey) {
        this.maxKey = maxKey;
    }

    public float getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(float errorRate) {
        this.errorRate = errorRate;
    }

    public int getHashFunctionCount() {
        return hashFunctionCount;
    }

    public void setHashFunctionCount(int hashFunctionCount) {
        this.hashFunctionCount = hashFunctionCount;
    }

    public int getBitSize() {
        return bitSize;
    }

    public void setBitSize(int bitSize) {
        this.bitSize = bitSize;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }


    public static String getKeyInRedis() {

        return keyInRedis;
    }

    public static void setKeyInRedis(String keyInRedis) {

       PipelineBloomFilter.keyInRedis = "key::" + keyInRedis + "::pipeline::filter";
    }
}
