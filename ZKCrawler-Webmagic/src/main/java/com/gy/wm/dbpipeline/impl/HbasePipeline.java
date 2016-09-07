package com.gy.wm.dbpipeline.impl;

import com.gy.wm.dbpipeline.MyHbaseUtils;
import com.gy.wm.dbpipeline.PipelineBloomFilter;
import com.gy.wm.dbpipeline.dbclient.HbaseClient;
import com.gy.wm.model.CrawlData;
import com.gy.wm.util.JedisPoolUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

/**
 * Created by TianyuanPan on 5/18/16.
 */
public class HbasePipeline extends BaseDBPipeline {


    private HbaseClient hbaseClient;

    private PipelineBloomFilter pipelineBloomFilter;


    public HbasePipeline() {
        MyHbaseUtils.createTable();
        //MyHbaseUtils.deleteAfterCreateTable();
        this.hbaseClient = new HbaseClient();
        pipelineBloomFilter = new PipelineBloomFilter(JedisPoolUtils.getJedisPool().getResource(), 0.001f, (int) Math.pow(2, 31));
    }

    @Override
    public int insertRecord(Object obj) {
        return 0;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        System.out.println("HbasePipeline resultItems size: " + resultItems.getAll().size() +
                "\n\tTask uuid: " + task.getUUID());

        //logger.debug("HbasePipeline resultItems size: " + resultItems.getAll().size() +
        //        "\n\tTask uuid: " + task.getUUID());

        CrawlData crawlerData = resultItems.get("crawlerData");

        if (crawlerData != null) {

            try {
                if (!pipelineBloomFilter.contains(crawlerData.getUrl())) {
                    pipelineBloomFilter.add(crawlerData.getUrl());
                    this.hbaseClient.add(crawlerData);
                    int i = this.hbaseClient.doSetInsert();
                    System.out.println("HbasePipeline doInsert Successful number: " + i);
                    //logger.debug("HbasePipeline doInsert Successful number: " + i);
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
            return;
        }

        System.out.println("at HbasePipeline, crawler data IS NULL !!!");
        //logger.debug("at HbasePipeline, crawler data IS NULL !!!");

    }
}
