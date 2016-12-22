package com.gy.wm.dao;

import com.gy.wm.mapper.FieldCroperEntityMapper;
import com.gy.wm.model.FieldCroperEntity;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.Reader;

/**
 * <类详细说明:字段精确提取规则dao>
 *
 * @Author： Huanghai
 * @Version: 2016-11-28
 **/
@Repository
public class FieldCroperEntityDao {
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

    /**
     * 插入字段抽取规则
     * @param fieldCroperEntity
     * @return
     */
    public int insertRules(FieldCroperEntity fieldCroperEntity) {
        SqlSession sqlSession= sqlSessionFactory.openSession();
        FieldCroperEntityMapper fieldCroperEntityMapper =sqlSession.getMapper(FieldCroperEntityMapper.class);
        int count= fieldCroperEntityMapper.insertRules(fieldCroperEntity);
        sqlSession.commit();
        sqlSession.close();
        return count;
    }

    /**
     * 通过任务Id查找对应精确字段提取规则
     * @return
     */
    public FieldCroperEntity findById(String tid)  {
        SqlSession sqlSession= sqlSessionFactory.openSession();
        FieldCroperEntityMapper fieldCroperEntityMapper =sqlSession.getMapper(FieldCroperEntityMapper.class);
        FieldCroperEntity fieldCroperEntity = fieldCroperEntityMapper.findById(tid);
        sqlSession.commit();
        sqlSession.close();
        return fieldCroperEntity;
    }
}