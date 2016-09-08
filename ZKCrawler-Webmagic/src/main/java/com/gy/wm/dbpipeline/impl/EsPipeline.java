package com.gy.wm.dbpipeline.impl;

import com.gy.wm.dbpipeline.PipelineBloomFilter;
import com.gy.wm.dbpipeline.dbclient.EsClient;
import com.gy.wm.model.CrawlData;
import com.gy.wm.util.JedisPoolUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

/**
 * Created by TianyuanPan on 5/9/16.
 */
public class EsPipeline extends BaseDBPipeline {

    private EsClient esClient;

    private PipelineBloomFilter pipelineBloomFilter;


    public EsPipeline() {

        this.esClient = new EsClient();
        pipelineBloomFilter = new PipelineBloomFilter(JedisPoolUtils.getJedisPool().getResource(), 0.001f, (int) Math.pow(2, 31));
    }


    @Override
    public int insertRecord(Object obj) {
        return 0;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {


        System.out.println("EsPipeline resultItems size: " + resultItems.getAll().size() +
                "\n\tTask uuid: " + task.getUUID());

        //logger.debug("EsPipeline resultItems size: " + resultItems.getAll().size() +
        //        "\n\tTask uuid: " + task.getUUID());

        CrawlData crawlerData = resultItems.get("crawlerData");

        if (crawlerData != null) {

            if (!pipelineBloomFilter.contains(crawlerData.getUrl())) {
                pipelineBloomFilter.add(crawlerData.getUrl());

                this.esClient.add(crawlerData);
                int i = this.esClient.doSetInsert();
                System.out.println("EsPipeline doInsert Successful number: " + i);
                //logger.debug("EsPipeline doInsert Successful number: " + i);
            }

            return;
        }

        System.out.println("at EsPipeline, crawler data IS NULL !!!");
        //logger.debug("at EsPipeline, crawler data IS NULL !!!");

    }

}
