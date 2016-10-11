package com.gy.wm.util;

import com.gy.wm.model.CrawlData;

import java.io.*;

/**
 * <类详细说明：序列化和反序列化方法>
 *
 * @Author： Huanghai
 * @Version: 2016-09-29
 **/
public class MySerializer {
    public static byte[] serialize(Object o) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        byte [] buf = baos.toByteArray();
        oos.flush();
        return buf;
    }

    public static Object deserialize(byte[] bytes) throws IOException,ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object obj = ois.readObject();
        return obj;
    }

    //测试
    public static void main(String[] args) {
        CrawlData crawlData =new CrawlData();
        crawlData.setUrl("www.google.com");
        crawlData.setTid("aaaaaaaaaaaaa");
        try {
            byte [] crawler_s = serialize(crawlData);
            CrawlData crawlData1 = (CrawlData) deserialize(crawler_s);
            System.out.println(crawlData1.getUrl() + " " + crawlData1.getTid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
