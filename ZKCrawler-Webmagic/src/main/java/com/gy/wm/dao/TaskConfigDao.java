package com.gy.wm.dao;

import com.gy.wm.model.TaskConfig;
import com.gy.wm.mapper.TaskConfigMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Tijun on 2016/11/14.
 * @author TijunWang
 */
@Repository
public class TaskConfigDao {

    private static SqlSessionFactory sqlSessionFactory = SQLSessionFactory.getSqlSessionFactory();

    public TaskConfig findTaskConfigByIdTask(String idTask){

        SqlSession sqlSession = sqlSessionFactory.openSession();
        TaskConfigMapper mapper = sqlSession.getMapper(TaskConfigMapper.class);
        TaskConfig config = mapper.findByIdTask(idTask);
        sqlSession.close();
        return config;

    }

    public List<TaskConfig> findByIdStart(int id){
        SqlSession Session = sqlSessionFactory.openSession();
        TaskConfigMapper mapper = Session.getMapper(TaskConfigMapper.class);
        List<TaskConfig> configs = mapper.findListIdStart(id);
        Session.close();
        return configs;

    }
}
