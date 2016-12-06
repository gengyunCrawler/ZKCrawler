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

    private static SqlSessionFactory sqlSessionFactory = SQLSessionFactory.getSqlSessionFactory();

    public ParserEntity find(String tid){

        SqlSession session= sqlSessionFactory.openSession();
        ParserMapper mapper= session.getMapper(ParserMapper.class);
        ParserEntity entity =   mapper.find(tid);
        session.close();
        return  entity;
    }

    public void insert(ParserEntity entity){

        SqlSession session= sqlSessionFactory.openSession();
        ParserMapper mapper= session.getMapper(ParserMapper.class);
        mapper.insert(entity);
        session.commit();
        session.close();

    }

}
