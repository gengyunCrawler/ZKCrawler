package com.gy.wm.dbpipeline.impl;

import com.gy.wm.model.CrawlData;
import com.gy.wm.util.AlphabeticRandom;
import com.gy.wm.util.ObjectSerializeUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * <类详细说明：中央媒体资源库Crawler数据，存入到Hbase>
 *
 * @Author： Huanghai
 * @Version: 2016-11-03
 **/
public class CMSHbasePipeline implements Pipeline{

    private static Configuration conf;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
    private static final String TABLE_NAME= ResourceBundle.getBundle("config").getString("HTableName");

    @Override
    public void process(ResultItems resultItems, Task task) {
        CrawlData crawlerData = resultItems.get("crawlerData");

        if(crawlerData != null) {
            putRecord(crawlerData,crawlerData.getTid());
        }
    }

    /**
     * 抓取数据插入Hbase
     * @param crawlData
     */
    public void putRecord(CrawlData crawlData, String taskId)  {
        conf = HBaseConfiguration.create();
        try {
            HTable table = new HTable(conf,TABLE_NAME);
            String rowKey = generateRowKey(taskId);
            Put put = new Put(Bytes.toBytes(rowKey));
            //Hbase Put value 不能为null,加逗号表达式进行null和空字符串处理
            int status = crawlData.getStatusCode();
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("docId"),Bytes.toBytes(rowKey));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("tid"),Bytes.toBytes(crawlData.getTid()==null?"":crawlData.getTid()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("url"),Bytes.toBytes(crawlData.getUrl() ==null?"":crawlData.getUrl()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("statusCode"),Bytes.toBytes(crawlData.getStatusCode()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("pass"),Bytes.toBytes(crawlData.getPass()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("type"),Bytes.toBytes(crawlData.getType()==null?"":crawlData.getType()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("rootUrl"),Bytes.toBytes(crawlData.getRootUrl()==null?"":crawlData.getRootUrl()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("fromUrl"),Bytes.toBytes(crawlData.getFromUrl()==null?"":crawlData.getFromUrl()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("text"),Bytes.toBytes(crawlData.getText()==null?"":crawlData.getText()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("html"),Bytes.toBytes(crawlData.getHtml()==null?"":crawlData.getHtml()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("title"),Bytes.toBytes(crawlData.getTitle()==null?"":crawlData.getTitle()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("startTime"),Bytes.toBytes(crawlData.getStartTime()==null?"":crawlData.getStartTime()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("crawlTime"),Bytes.toBytes(sdf.format(crawlData.getCrawlTime())==null?"":sdf.format(crawlData.getCrawlTime())));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("publishTime"),Bytes.toBytes(crawlData.getPublishTime()==null?"":crawlData.getPublishTime()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("depthfromSeed"),Bytes.toBytes(crawlData.getDepthfromSeed()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("count"),Bytes.toBytes(crawlData.getCount()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("tag"),Bytes.toBytes(crawlData.getTag()==null?"":crawlData.getTag()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("fetched"),Bytes.toBytes(crawlData.isFetched()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("author"),Bytes.toBytes(crawlData.getAuthor()==null?"":crawlData.getAuthor()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("sourceName"),Bytes.toBytes(crawlData.getSourceName()==null?"":crawlData.getSourceName()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("parsedData"),Bytes.toBytes(crawlData.getParsedData()==null?"":crawlData.getParsedData()));

            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("tags"), ObjectSerializeUtils.serializeObjectToBytes(crawlData.getTags()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("categories"), ObjectSerializeUtils.serializeObjectToBytes(crawlData.getCategories()));

            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("textPTag"), Bytes.toBytes(crawlData.getTextPTag()==null?"":crawlData.getTextPTag()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("imgUrl"), Bytes.toBytes(crawlData.getImgUrl()==null?"":crawlData.getImgUrl()));
            put.add(Bytes.toBytes("crawlerData"),Bytes.toBytes("sourceTypeId"), Bytes.toBytes(crawlData.getSourceTypeId()==null?"":crawlData.getSourceTypeId()));


            table.put(put);
            table.close();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }

    /*public boolean nullValuePutCheck()    {

    }*/

    public String generateRowKey(String taskId)    {
        return taskId+"|"+new Date().getTime()+"|"+ AlphabeticRandom.randomStringOfLength(5);
    }

}
