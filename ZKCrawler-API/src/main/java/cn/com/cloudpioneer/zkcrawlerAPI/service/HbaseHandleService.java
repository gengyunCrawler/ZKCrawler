package cn.com.cloudpioneer.zkcrawlerAPI.service;

import cn.com.cloudpioneer.zkcrawlerAPI.model.CrawlData;
import cn.com.cloudpioneer.zkcrawlerAPI.utils.JSONUtil;
import cn.com.cloudpioneer.zkcrawlerAPI.utils.RandomAlphaNumeric;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <类详细说明：Hbase数据库的操作>
 *
 * @Author： Huanghai
 * @Version: 2016-11-01
 **/
@Service
public class HbaseHandleService {
    private static Configuration conf;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

    /**
     * 创建表
     * @throws Exception
     */
    public void createTable()   throws Exception{
    conf = HBaseConfiguration.create();
    HBaseAdmin admin = new HBaseAdmin(conf);

    //create tableDesc, with namespace name "my_ns" and table name "mytable"
    HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf("gengyun.crawler.info"));
    tableDesc.setDurability(Durability.SYNC_WAL);

    //add a column family "data"
    HColumnDescriptor hcd = new HColumnDescriptor("data");
    tableDesc.addFamily(hcd);
    admin.createTable(tableDesc);
    admin.close();
    }

    /**
     * 抓取数据插入Hbase
     * @param crawlData
     * @param tableName
     */
    public void putRecord(CrawlData crawlData, String tableName, String taskId)  {
        conf = HBaseConfiguration.create();
        try {
            HTable table = new HTable(conf,tableName);
            Put put = new Put(Bytes.toBytes(generateRowKey(taskId)));
            put.add(Bytes.toBytes("data"),Bytes.toBytes("url"),Bytes.toBytes(crawlData.getUrl()));
            table.put(put);
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描Hbase信息
     * @param jsonObject
     * @param tableName
     * @return
     */
    public String getHBaseData(JSONObject jsonObject,String tableName) {

        conf = HBaseConfiguration.create();
        ResultScanner rs = null;
        JSONObject result =null;
        try {
            HTable htable = new HTable(conf, tableName);
            Scan scan = new Scan();
            scan.setCaching(100);
            scan.addColumn(Bytes.toBytes("data"), Bytes.toBytes("tid"));
            scan.addColumn(Bytes.toBytes("data"), Bytes.toBytes("url"));
            String tid = jsonObject.getString("tid");
            String startRow = jsonObject.getString("startRow"+"0");
            int size = jsonObject.getInteger("size");
            scan.setStartRow(Bytes.toBytes(startRow));
            // start key is inclusive
//        scan.setStopRow(Bytes.toBytes("row" + (char) 0));  // stop key is
            rs = htable.getScanner(scan);
            int count =0;
            String nextRow = null;

            JSONArray crawlerDataArray = new JSONArray();
            for(int i=0; i<size; i++)   {
                for (Result r = rs.next(); r != null; r = rs.next()) {
                    for(Cell cell : r.rawCells())   {
                       String url = Bytes.toString(CellUtil.cloneValue(cell));
                        nextRow = Bytes.toString(r.getRow()).split("\\|")[2];
                        JSONObject perObject = new JSONObject();
                        perObject.put("url",url);
                        crawlerDataArray.add(perObject);
                        count++;
                    }
                }
            }
            result.put("result",true);
            result.put("data",new JSONObject().put("data",crawlerDataArray));
            result.put("size",count);
            result.put("nextRow",nextRow);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            rs.close();
        }
        return JSONUtil.object2JacksonString(result);
    }

    public String generateRowKey(String taskId)    {
        return taskId+"|"+ sdf.format(new Date())+"|"+RandomAlphaNumeric.randomStringOfLength(5);
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static void main(String[] args) {
        HbaseHandleService handleService = new HbaseHandleService();
        String taskId =/*handleService.MD5("http://www.gygov.gov.cn/");*/"c5b475b03652d36b5fdfe97022be0240";
        /*CrawlData crawlData1 = new CrawlData();
        crawlData1.setTid("1234");
        crawlData1.setUrl("http://www.test.gov.cn/");

        CrawlData crawlData2 = new CrawlData();
        crawlData2.setTid("1234");
        crawlData2.setUrl("http://www.test.gov.cn/");

        CrawlData crawlData3 = new CrawlData();
        crawlData3.setTid("1234");
        crawlData3.setUrl("http://www.test.gov.cn/");

        CrawlData crawlData4 = new CrawlData();
        crawlData4.setTid("1234");
        crawlData4.setUrl("http://www.test.gov.cn/");

        CrawlData crawlData5 = new CrawlData();
        crawlData5.setTid("1234");
        crawlData5.setUrl("http://www.test.gov.cn/");

        CrawlData crawlData6 = new CrawlData();
        crawlData6.setTid("1234");
        crawlData6.setUrl("http://www.test.gov.cn/");

        CrawlData crawlData7 = new CrawlData();
        crawlData7.setTid("1234");
        crawlData7.setUrl("http://www.test.gov.cn/");

        handleService.putRecord(crawlData1,"gengyun.crawler.info",taskId);
        handleService.putRecord(crawlData2,"gengyun.crawler.info",taskId);
        handleService.putRecord(crawlData3,"gengyun.crawler.info",taskId);
        handleService.putRecord(crawlData4,"gengyun.crawler.info",taskId);
        handleService.putRecord(crawlData5,"gengyun.crawler.info",taskId);
        handleService.putRecord(crawlData6,"gengyun.crawler.info",taskId);*/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskid",taskId);
        jsonObject.put("size",5);
        jsonObject.put("startRow",taskId+"|");
        handleService.getHBaseData(jsonObject,"gengyun.crawler.info");
    }
}