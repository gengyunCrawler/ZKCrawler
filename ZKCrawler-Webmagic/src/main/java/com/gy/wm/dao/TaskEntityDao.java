package com.gy.wm.dao;

import com.gy.wm.mapper.TaskEntityMapper;
import com.gy.wm.model.TaskEntity;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

/**
 * <类详细说明>
 *
 * @Author： HuangHai
 * @Version: 2017-01-13
 **/
@Repository
public class TaskEntityDao {
    private static SqlSessionFactory sqlSessionFactory = SQLSessionFactory.getSqlSessionFactory();

    public TaskEntity findById(String id)   {
        SqlSession session = sqlSessionFactory.openSession();
        TaskEntityMapper taskEntityMapper  = session.getMapper(TaskEntityMapper.class);
        TaskEntity taskEntity = taskEntityMapper.findById(id);
        session.commit();
        session.close();
        return taskEntity;
    }
}
