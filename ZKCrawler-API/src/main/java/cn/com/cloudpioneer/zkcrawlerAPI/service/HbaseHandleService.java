package cn.com.cloudpioneer.zkcrawlerAPI.service;

import cn.com.cloudpioneer.zkcrawlerAPI.model.CrawlData;
import cn.com.cloudpioneer.zkcrawlerAPI.utils.JSONUtil;
import cn.com.cloudpioneer.zkcrawlerAPI.utils.ObjectSerializeUtils;
import cn.com.cloudpioneer.zkcrawlerAPI.utils.RandomAlphaNumeric;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private static final String TABLE_NAME = ResourceBundle.getBundle("config").getString("HTableName");

    /**
     * 创建Hbase表
     *
     * @throws Exception
     */
    public void createTable() throws Exception {
        conf = HBaseConfiguration.create();
        HBaseAdmin admin = new HBaseAdmin(conf);

        //create tableDesc, with namespace name "my_ns" and table name "mytable"
        HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(TABLE_NAME));

        tableDesc.setDurability(Durability.SYNC_WAL);

        //add a column family "data"
        HColumnDescriptor hcd = new HColumnDescriptor("crawlerData");
        tableDesc.addFamily(hcd);
        admin.createTable(tableDesc);
        admin.close();
    }

    /**
     * 抓取数据插入Hbase
     *
     * @param crawlData
     */
    public void putRecord(CrawlData crawlData, String taskId) {
        conf = HBaseConfiguration.create();
        try {
            HTable table = new HTable(conf, TABLE_NAME);

            Put put = new Put(Bytes.toBytes(generateRowKey(taskId)));
            put.add(Bytes.toBytes("crawlerData"), Bytes.toBytes("url"), Bytes.toBytes(crawlData.getUrl()));
            table.put(put);
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Hbase数据
     *
     * @param t_taskId
     * @param t_startRow
     * @param t_size
     * @return
     */
    public String getHBaseData(String t_taskId, String t_startRow, String t_size) {

        conf = HBaseConfiguration.create();
        ResultScanner rs = null;
        JSONObject result = new JSONObject();
        try {
            HTable htable = new HTable(conf, TABLE_NAME);
            Scan scan = new Scan();
            scan.setCaching(100);
            scan.addFamily(Bytes.toBytes("crawlerData"));
            String tid = t_taskId;
            // start key is exclusive
            String startRow = t_startRow + "0";
            int size = Integer.valueOf(t_size);
            scan.setStartRow(Bytes.toBytes(startRow));
            rs = htable.getScanner(scan);
            int readCount = 0;
            String rowkey = null;
            JSONArray crawlerDataArray = new JSONArray();

            for (Result r = rs.next(); r != null && readCount < size; r = rs.next()) {

                String docId = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"),Bytes.toBytes("docId")));
                String url = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("url")));
                int statusCode = Bytes.toInt(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("statusCode")));
                int pass = Bytes.toInt(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("pass")));
                String type = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("type")));
                String rootUrl = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("rootUrl")));
                String fromUrl = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("fromUrl")));
                String text = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("text")));
                String html = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("html")));
                String title = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("title")));
                String startTime = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("startTime")));
                String crawlTime = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("crawlTime")));
                String publishTime = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("publishTime")));
                int depthfromSeed = Bytes.toInt(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("depthfromSeed")));
                int count = Bytes.toInt(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("count")));
                String tag = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("tag")));
                boolean fetched = Bytes.toBoolean(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("fetched")));
                String author = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("author")));
                String sourceName = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("sourceName")));
                String parsedData = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("parsedData")));

                rowkey = Bytes.toString(r.getRow());
                JSONObject perObject = new JSONObject();
                perObject.put("docId",docId);
                perObject.put("tid", tid);
                perObject.put("url", url);
                perObject.put("statusCode", statusCode);
                perObject.put("pass", pass);
                perObject.put("type", type);
                perObject.put("rootUrl", rootUrl);
                perObject.put("fromUrl", fromUrl);
                perObject.put("text", text);
                perObject.put("html", html);
                perObject.put("title", title);
                perObject.put("startTime", startTime);
                perObject.put("crawlTime", crawlTime);
                perObject.put("publishTime", publishTime);
                perObject.put("depthfromSeed", depthfromSeed);
                perObject.put("count", count);
                perObject.put("tag", tag);
                perObject.put("fetched", fetched);
                perObject.put("author", author);
                perObject.put("sourceName", sourceName);
                perObject.put("parsedData", parsedData);
                crawlerDataArray.add(perObject);

                readCount++;
            }

            result.put("result", "true");
            result.put("data", crawlerDataArray);
            result.put("size", readCount);
            result.put("nextRow", rowkey);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"result\":false,\"reason\":\"Hbase读写出错\"}";
        } finally {
            rs.close();
        }
        return JSONUtil.object2JacksonString(result);
    }


    public Map<String, Object> getMapHBaseData(String t_taskId, String t_startRow, int t_size) {

        conf = HBaseConfiguration.create();
        ResultScanner rs = null;
        Map<String, Object> result = new HashMap<>();
        try {
            HTable htable = new HTable(conf, TABLE_NAME);
            Scan scan = new Scan();
            scan.setCaching(t_size);
            scan.addFamily(Bytes.toBytes("crawlerData"));
            String startRow = t_startRow + "0";

            scan.setStartRow(Bytes.toBytes(startRow));
            rs = htable.getScanner(scan);
            int readCount = 0;
            String rowkey = null;
            List<CrawlData> crawlDataList = new ArrayList<>();

            JSONArray tags, categories;

            for (Result r = rs.next(); r != null && readCount < t_size; r = rs.next()) {

                String docId = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"),Bytes.toBytes("docId")));

                try {
                    tags = (JSONArray) ObjectSerializeUtils.buildObjectFromBytes(r.getValue(Bytes.toBytes("crawlerData"),Bytes.toBytes("tags")));
                } catch (Exception e) {
                    tags = new JSONArray();
                    e.printStackTrace();
                }

                try {
                    categories = (JSONArray) ObjectSerializeUtils.buildObjectFromBytes(r.getValue(Bytes.toBytes("crawlerData"),Bytes.toBytes("categories")));
                } catch (Exception e) {
                    categories = new JSONArray();
                    e.printStackTrace();
                }

                String sourceTypeId = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("sourceTypeId")));

                String imgUrl = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("imgUrl")));

                String tid = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("tid")));

                String url = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("url")));

                int statusCode = Bytes.toInt(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("statusCode")));

                int pass = Bytes.toInt(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("pass")));

                String type = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("type")));

                String rootUrl = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("rootUrl")));

                String fromUrl = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("fromUrl")));

                String text = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("text")));

                String textPTag = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("textPTag")));

                String html = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("html")));

                String title = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("title")));

                String startTime = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("startTime")));

                String crawlTime = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("crawlTime")));

                String publishTime = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("publishTime")));

                int depthfromSeed = Bytes.toInt(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("depthfromSeed")));

                int count = Bytes.toInt(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("count")));

                String tag = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("tag")));

                boolean fetched = Bytes.toBoolean(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("fetched")));

                String author = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("author")));

                String sourceName = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("sourceName")));

                String parsedData = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("parsedData")));

                rowkey = Bytes.toString(r.getRow());

                CrawlData crawlData = new CrawlData();

                crawlData.setDocId(docId);
                crawlData.setTags(tags);
                crawlData.setCategories(categories);
                crawlData.setSourceTypeId(sourceTypeId);
                crawlData.setImgUrl(imgUrl);
                crawlData.setTextPTag(textPTag);

                crawlData.setTid(t_taskId);
                crawlData.setUrl(url);
                crawlData.setStatusCode(statusCode);
                crawlData.setPass(pass);
                crawlData.setType(type);
                crawlData.setRootUrl(rootUrl);
                crawlData.setFromUrl(fromUrl);
                crawlData.setText(text);
                crawlData.setHtml(html);
                crawlData.setTitle(title);
                crawlData.setStartTime(startTime);

                try {
                    crawlData.setCrawlTime(new Date(new SimpleDateFormat("yyyyMMddhhmmss").parse(crawlTime).getTime()));
                } catch (ParseException e) {
                    crawlData.setCrawlTime(null);
                }
                crawlData.setPublishTime(publishTime);
                crawlData.setDepthfromSeed(depthfromSeed);
                crawlData.setCount(count);
                crawlData.setTag(tag);
                crawlData.setFetched(fetched);
                crawlData.setAuthor(author);
                crawlData.setSourceName(sourceName);
                crawlData.setParsedData(parsedData);
                crawlDataList.add(crawlData);

                readCount++;
            }


            result.put("data", crawlDataList);
            result.put("size", readCount);
            result.put("nextRow", rowkey);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            rs.close();
        }
        return result;
    }


    public String generateRowKey(String taskId) {
        return taskId + "|" + new Date().getTime() + "|" + RandomAlphaNumeric.randomStringOfLength(5);
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(MD5("http://www.gaxq.gov.cn/"));
    }

    /**
     * 获取Hbase数据
     *
     * @param t_taskId
     * @param t_startRow
     * @param t_size
     * @return
     */
    public String getHBaseDataTest(String t_taskId, String t_startRow, String t_size) {

        conf = HBaseConfiguration.create();
        ResultScanner rs = null;
        JSONObject result = new JSONObject();
        try {
            HTable htable = new HTable(conf, TABLE_NAME);
            Scan scan = new Scan();
            scan.setCaching(100);
            scan.addFamily(Bytes.toBytes("crawlerData"));
            scan.addColumn(Bytes.toBytes("crawlerData"),Bytes.toBytes("crawlTime"));
            scan.addColumn(Bytes.toBytes("crawlerData"),Bytes.toBytes("publishTime"));
            String tid = t_taskId;
            // start key is exclusive
            String startRow = t_startRow + "0";
            int size = Integer.valueOf(t_size);
            scan.setStartRow(Bytes.toBytes(startRow));
            rs = htable.getScanner(scan);
            int readCount = 0;
            String rowkey = null;
            JSONArray crawlerDataArray = new JSONArray();
            for (Result r = rs.next(); r != null && readCount < size; r = rs.next()) {
                String crawlTime = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("crawlTime")));
                String publishTime = Bytes.toString(r.getValue(Bytes.toBytes("crawlerData"), Bytes.toBytes("publishTime")));
                rowkey = Bytes.toString(r.getRow());

                System.out.println("rowkey: " + rowkey + "\t" + "crawlTime:" + crawlTime + "\t" + "publishTime:" + publishTime);

                readCount++;
            }

            result.put("result", "true");
            result.put("data", crawlerDataArray);
            result.put("size", readCount);
            result.put("nextRow", rowkey);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"result\":false,\"reason\":\"Hbase读写出错\"}";
        } finally {
            rs.close();
        }
        return JSONUtil.object2JacksonString(result);
    }
}