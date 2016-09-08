package com.gy.wm.dbpipeline.dbclient;

import com.gy.wm.dbpipeline.MyHbaseUtils;
import com.gy.wm.model.CrawlData;
import com.gy.wm.util.CrawlerDataUtils;
import com.gy.wm.util.HbasePoolUtils;
import com.gy.wm.util.RandomUtils;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 5/18/16.
 */
public class HbaseClient extends AbstractDBClient {


    private String tableName;
    private String columnFamilyName;
    private HTableInterface myTable;


    private List<CrawlData> dataList;

    public HbaseClient() {

        this.dataList = new ArrayList<>();
        this.tableName = MyHbaseUtils.getTableName();//ConfigUtils.getResourceBundle().getString("HBASE_TABLE_NAME");
        this.columnFamilyName = MyHbaseUtils.getColumnFamilyName(); //ConfigUtils.getResourceBundle().getString("HBASE_COLUMNFAMILY_NAME");
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnFamilyName() {
        return columnFamilyName;
    }

    @Override
    public Object getConnection() {
        return null;
    }

    @Override
    public void closeConnection() {

    }

    @Override
    public int doSetInsert() {
        int count = 0;
        int size = dataList.size();
        for (int i = 0; i < size; ++i) {

            try {
                count += this.insertRecord(tableName, RandomUtils.getRandomString(50) + "_" + new Date().getTime(), columnFamilyName, dataList.get(i));
            } catch (Exception ex) {
                logger.warn("HbaseClient doSetInsert Exception!!! Message: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        this.dataList = new ArrayList<>();

        return count;
    }

    @Override
    public boolean isConnOpen() {
        return false;
    }


    public void add(CrawlData data) {

        this.dataList.add(data);
    }


    public int insertRecord(String tableName, String rowKey, String columnFamilyName, CrawlData data) {

        String columnQualifier = null;
        String value = null;
        String type;

        myTable = HbasePoolUtils.getHTable(tableName);

        Put put = new Put(Bytes.toBytes(rowKey));// 设置rowkey

        CrawlerDataUtils utils = CrawlerDataUtils.getCrawlerDataUtils(data);

        List<Map<String, Object>> myDataList = utils.getAttributeInfoList();
        int size = myDataList.size();
        try {

            for (int i = 0; i < size; ++i) {
                try {
                    columnQualifier = myDataList.get(i).get("name").toString();
                    type = myDataList.get(i).get("type").toString();

                    switch (type) {

                        case "int":
                            value = Integer.toString((int) myDataList.get(i).get("value"));
                            break;
                        case "long":
                            value = Long.toString((long) myDataList.get(i).get("value"));
                            break;
                        case "boolean":
                            value = Boolean.toString((boolean) myDataList.get(i).get("value"));
                            break;
                        default:
                            value = (String) myDataList.get(i).get("value");
                            break;
                    }

                } catch (Exception ex) {

                    logger.warn("get data Exception! columnQualifier = " + columnQualifier + ", value = " + value);
                    ex.printStackTrace();

                }

                if (value == null)
                    value = "null";

                if (columnQualifier != null)
                    put.add(Bytes.toBytes(columnFamilyName),
                            Bytes.toBytes(columnQualifier),
                            Bytes.toBytes(value));
            }

            myTable.put(put);

        } catch (Exception ex) {

            try {
                myTable.close();
            } catch (Exception exc) {
                logger.warn("Hbase table is close() or connection is close() error!!! Message: " + exc.getMessage());
                exc.printStackTrace();
            }

            logger.warn("HBase Put data Exception!!! Message: " + ex.getMessage());
            ex.printStackTrace();
            return 0;
        }


        try {

            myTable.close();

        } catch (Exception ex) {
            logger.warn("Hbase table.close() or connection,close() error!!! Message: " + ex.getMessage());
            ex.printStackTrace();
        }


        return 1;
    }

}
