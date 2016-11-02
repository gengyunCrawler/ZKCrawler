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
import java.util.ResourceBundle;

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
    private static final String TABLE_NAME= ResourceBundle.getBundle("config").getString("HTableName");

    /**
     * 创建Hbase表
     * @throws Exception
     */
    public void createTable()   throws Exception{
    conf = HBaseConfiguration.create();
    HBaseAdmin admin = new HBaseAdmin(conf);

    //create tableDesc, with namespace name "my_ns" and table name "mytable"
    HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
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
     */
    public void putRecord(CrawlData crawlData, String taskId)  {
        conf = HBaseConfiguration.create();
        try {
            HTable table = new HTable(conf,TABLE_NAME);
            Put put = new Put(Bytes.toBytes(generateRowKey(taskId)));
            put.add(Bytes.toBytes("data"),Bytes.toBytes("url"),Bytes.toBytes(crawlData.getUrl()));
            table.put(put);
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Hbase数据
     * @param t_taskId
     * @param t_startRow
     * @param t_size
     * @return
     */
    public String getHBaseData(String t_taskId,String t_startRow,String t_size) {

        conf = HBaseConfiguration.create();
        ResultScanner rs = null;
        JSONObject result = new JSONObject();
        try {
            HTable htable = new HTable(conf, TABLE_NAME);
            Scan scan = new Scan();
            scan.setCaching(100);
            scan.addColumn(Bytes.toBytes("data"), Bytes.toBytes("tid"));
            scan.addColumn(Bytes.toBytes("data"), Bytes.toBytes("url"));
            String tid = t_taskId;
            String startRow = t_startRow +"0";
            int size = Integer.valueOf(t_size);
            scan.setStartRow(Bytes.toBytes(startRow));
            // start key is inclusive
//        scan.setStopRow(Bytes.toBytes("row" + (char) 0));  // stop key is
            rs = htable.getScanner(scan);
            int count =0;
            String rowkey = null;
            JSONArray crawlerDataArray = new JSONArray();
            for (Result r = rs.next(); r != null&& count < size; r = rs.next()) {
                //column qualifier iterator
                for(Cell cell : r.rawCells())   {
                   String url = Bytes.toString(CellUtil.cloneValue(cell));
                    rowkey = Bytes.toString(r.getRow());
                    JSONObject perObject = new JSONObject();
                    perObject.put("url",url);
                    crawlerDataArray.add(perObject);
                }
                count++;
            }

            result.put("result","true");
            result.put("data",crawlerDataArray);
            result.put("size",count);
            result.put("nextRow",rowkey);
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
}