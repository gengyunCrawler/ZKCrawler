package com.gy.wm.util;

import com.gy.wm.model.People;
import com.gy.wm.model.People_mapper;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class Test {

    public static void hbaseQuery() {
        Scan scan = new Scan();
        ResultScanner rs = null;
        try {
            HTableInterface table = HbasePoolUtils.getHTable("gycrawler");//new HTable(config, Bytes.toBytes("gycrawler"));
            rs = table.getScanner(scan);
            int i = 0;
            for (Result r : rs) {
                i++;
                for (Cell cell : r.rawCells()) {
                    String s = Bytes.toString(CellUtil.cloneRow(cell));
                    String s1 = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String s2 = Bytes.toString(CellUtil.cloneValue(cell));
                    System.out.println("row: " + s + "; qualifier: " + s1 + "; value: " + s2);
//                    System.out.println("row:" + s1);
//                    System.out.println("row:" + s2);
                }
                System.out.println("-------------------------------------------");
                for (KeyValue kv : r.list()) {


                    System.out.println("timestamp:" + kv.getTimestamp());

                    System.out.println("row:" + Bytes.toString(kv.getRow()) + ";\nfamily:" +
                            Bytes.toString(kv.getFamily()) +  ";\nqualifier: " +
                            Bytes.toString(kv.getQualifier()) + ";\nvalue: " +
                            Bytes.toString(kv.getValue()));


                }
                System.out.println("-------------------------------------------");
                System.out.println(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rs.close();
        }
    }

    public static void main(String args[]) throws IOException {

        Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession session = sqlSessionFactory.openSession();
        session.getConfiguration().addMapper(People_mapper.class);

        People_mapper mapper = session.getMapper(People_mapper.class);

        //Create a new student object
        People people = new People();

        //Set the values
        people.setId(1);
        people.setName("www.ibatis.com");

        //Insert student data
        mapper.insert(people);
        System.out.println("record inserted successfully");
        session.commit();
        session.close();

    }

}
