package com.gy.wm.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Tijun on 2016/11/14.
 * @author TijunWang
 */
public class SQLSessionFactory {


    private static SqlSessionFactory sqlSessionFactory;

    private  SQLSessionFactory(){}

    static {

         Reader reader;

        try {

            reader = Resources.getResourceAsReader("SqlMapConfig.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
