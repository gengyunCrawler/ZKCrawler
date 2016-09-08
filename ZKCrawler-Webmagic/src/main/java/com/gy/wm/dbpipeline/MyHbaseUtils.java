package com.gy.wm.dbpipeline;

import com.gy.wm.util.ConfigUtils;
import com.gy.wm.util.HbasePoolUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

/**
 * Created by TianyuanPan on 6/23/16.
 */
public class MyHbaseUtils {

    private static String tableName;
    private static String columnFamilyName;
    private static Configuration conf;

    static {

        tableName = ConfigUtils.getResourceBundle().getString("HBASE_TABLE_NAME");
        columnFamilyName = ConfigUtils.getResourceBundle().getString("HBASE_COLUMNFAMILY_NAME");
        conf = HbasePoolUtils.getConfiguration();
    }

    private MyHbaseUtils() {

    }

    public static boolean isThisTableExist(String tableName) {

        try {

            HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);

            if (hBaseAdmin.tableExists(tableName))
                return true;

        } catch (IOException e) {

            e.printStackTrace();
        }

        return false;
    }

    public static boolean createTable() {

        if (isThisTableExist(tableName))
            return false;
        try {

            HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);

            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);

            tableDescriptor.addFamily(new HColumnDescriptor(columnFamilyName));

            hBaseAdmin.createTable(tableDescriptor);

        } catch (Exception ex) {

            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean createTable(String tableName, String[] columnFamilyName) {

        try {

            HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);

            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);

            for (String familyName : columnFamilyName)
                tableDescriptor.addFamily(new HColumnDescriptor(familyName));

            hBaseAdmin.createTable(tableDescriptor);

        } catch (Exception ex) {

            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean createTable(String tableName, String columnFamilyName) {

        try {

            HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);

            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);

            tableDescriptor.addFamily(new HColumnDescriptor(columnFamilyName));

            hBaseAdmin.createTable(tableDescriptor);

        } catch (Exception ex) {

            ex.printStackTrace();
            return false;
        }

        return true;
    }


    public static boolean deleteAfterCreateTable() {

        try {

            HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);

            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);

            tableDescriptor.addFamily(new HColumnDescriptor(columnFamilyName));

            if (hBaseAdmin.tableExists(tableName))
                hBaseAdmin.deleteTable(tableName);

            hBaseAdmin.createTable(tableDescriptor);

        } catch (Exception ex) {

            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public static String getTableName() {

        return tableName;
    }

    public static void setTableName(String tableName) {

        MyHbaseUtils.tableName = tableName;
    }

    public static String getColumnFamilyName() {

        return columnFamilyName;
    }

    public static void setColumnFamilyName(String columnFamilyName) {

        MyHbaseUtils.columnFamilyName = columnFamilyName;
    }
}




