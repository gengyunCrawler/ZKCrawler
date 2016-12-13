package com.gy.wm.dbpipeline.impl;

import com.gy.wm.model.CrawlData;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by TianyuanPan on 12/7/2016.
 */
public class FilePipeline implements Pipeline {

    private String datafile;
    private OutputStream outputStream;
    private Lock writeLock;

    public FilePipeline() {

        datafile = "./data.crawler." + System.currentTimeMillis() + ".txt";
        writeLock = new ReentrantLock();
        try {
            outputStream = new FileOutputStream(new File(datafile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() {
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        CrawlData crawlerData = resultItems.get("crawlerData");

        if (crawlerData != null) {

            try {
                writeLock.lock();
                try {
                    byte[] data = (crawlerData.toJSONString() + "\n").getBytes();
                    outputStream.write(data, 0, data.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {

                writeLock.unlock();
            }
        }

    }
}
