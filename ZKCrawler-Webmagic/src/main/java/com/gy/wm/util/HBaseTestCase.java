package com.gy.wm.util;


import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseTestCase {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String tableName = "test";
        String columnFamily = "cf";
        try {

            if (true == HBaseTestCase.delete(tableName)) {
                System.out.println("Delete Table " + tableName + " success!");

            }

            /*HBaseTestCase.create(tableName, columnFamily);
            HBaseTestCase.put(tableName, "row1", columnFamily, "column1",
                    "data1");
            HBaseTestCase.put(tableName, "row2", columnFamily, "column2",
                    "data2");
            HBaseTestCase.put(tableName, "row3", columnFamily, "column3",
                    "data3");
            HBaseTestCase.put(tableName, "row4", columnFamily, "column4",
                    "data4");
            HBaseTestCase.put(tableName, "row5", columnFamily, "column5",
                    "data5");

            HBaseTestCase.get(tableName, "row1");*/

            HBaseTestCase.scan("gycrawler");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Configuration cfg = HBaseConfiguration.create();
    static {
        System.out.println(cfg.get("hbase.master"));
    }

    public static void create(String tableName, String columnFamily)
            throws Exception {
        HBaseAdmin admin = new HBaseAdmin(cfg);
        if (admin.tableExists(tableName)) {
            System.out.println(tableName + " exists!");
        } else {
            HTableDescriptor tableDesc = new HTableDescriptor(tableName);
            tableDesc.addFamily(new HColumnDescriptor(columnFamily));
            admin.createTable(tableDesc);
            System.out.println(tableName + " create successfully!");
        }
    }

    public static void put(String tablename, String row, String columnFamily,
                           String column, String data) throws Exception {

        HTable table = new HTable(cfg, tablename);
        Put put = new Put(Bytes.toBytes(row));

        put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),
                Bytes.toBytes(data));

        table.put(put);

        System.out.println("put '" + row + "', '" + columnFamily + ":" + column
                + "', '" + data + "'");

    }

    public static void get(String tablename, String row) throws Exception {
        HTable table = new HTable(cfg, tablename);
        Get get = new Get(Bytes.toBytes(row));
        Result result = table.get(get);
        System.out.println("Get: " + result);
    }

    public static void scan(String tableName) throws Exception {

        HTable table = new HTable(cfg, tableName);
        Scan s = new Scan();
        ResultScanner rs = table.getScanner(s);

        for (Result r : rs) {
            System.out.println("Scan: " + r);

        }
    }

    public static boolean delete(String tableName) throws IOException {

        HBaseAdmin admin = new HBaseAdmin(cfg);
        if (admin.tableExists(tableName)) {
            try {
                admin.disableTable(tableName);
                admin.deleteTable(tableName);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}