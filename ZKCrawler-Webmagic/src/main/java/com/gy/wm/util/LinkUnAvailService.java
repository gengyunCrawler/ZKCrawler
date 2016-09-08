package com.gy.wm.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
/**
 * Created by TianyuanPan on 6/28/16.
 */
public class LinkUnAvailService {

    private JedisPool jedisPool;
    private Configuration hbConfig;
    private HTable table;

    public void init(String tablename) {
        hbConfig = new Configuration();
        JedisPoolConfig config = new JedisPoolConfig();
        hbConfig.addResource("hbase-site.xml.test");
    }


    public void hbaseToRedis(String tableName) {
        Jedis jedis = null;
        ResultScanner rs = null;
        try {
            table = new HTable(hbConfig, tableName);
            Scan scan = new Scan();
            scan.setCaching(50);
            scan.addColumn(Bytes.toBytes("crawlerData"), Bytes.toBytes("text"));
            scan.addColumn(Bytes.toBytes("crawlerData"), Bytes.toBytes("url"));

            rs = table.getScanner(scan);
           // jedis = jedisPool.getResource();
           // jedis.select(9);

            for (Result r : rs) {
                String url = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("url")));
                String text = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("text")));

//                String errorWords = client.query(text);
                System.out.println("url: " + url  + "\ntext: " + text);
//                jedis.hset(tableName + "_errorwords", url, errorWords);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        LinkUnAvailService ls = new LinkUnAvailService();
        ls.init("gycrawler");
        ls.hbaseToRedis("gycrawler");
    }

}
