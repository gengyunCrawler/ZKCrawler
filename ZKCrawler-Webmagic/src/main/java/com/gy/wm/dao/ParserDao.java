package com.gy.wm.dao;

import com.gy.wm.mapper.ParserMapper;

import com.gy.wm.plugins.newsExportPlugin.parse.ParserEntity;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by tijun on 2016/10/8.
 * @author TijunWang
 */
public class ParserDao {
    private static Reader reader;
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            reader = Resources.getResourceAsReader("SqlMapConfig.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }
    //just for query
    public <T> T getMapper(Class<T> clazz){
       SqlSession session= sqlSessionFactory.openSession();
       return session.getMapper(clazz);
    }

    public ParserEntity find(String tid){
        ParserMapper mapper=getMapper(ParserMapper.class);
        return   mapper.find(tid);
    }

    public void insert(ParserEntity entity){
        SqlSession session= sqlSessionFactory.openSession();
        ParserMapper mapper= session.getMapper(ParserMapper.class);
        mapper.insert(entity);
        session.commit();
        session.close();

    }
}
