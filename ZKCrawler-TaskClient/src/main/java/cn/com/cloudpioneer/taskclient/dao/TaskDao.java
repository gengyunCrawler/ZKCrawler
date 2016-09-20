package cn.com.cloudpioneer.taskclient.dao;

import cn.com.cloudpioneer.taskclient.mapper.TaskEntityMapper;
import cn.com.cloudpioneer.taskclient.model.TaskEntity;
import cn.com.cloudpioneer.taskclient.model.TaskStatusItem;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class TaskDao {

    private static Reader reader;
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    public List<TaskEntity> findAll() {

        SqlSession session = sqlSessionFactory.openSession();

        TaskEntityMapper mapper = session.getMapper(TaskEntityMapper.class);
        List<TaskEntity> entities = mapper.findAll();
        session.commit();
        session.close();

        return entities;
    }

    public List<TaskEntity> findAllValid() {

        SqlSession session = sqlSessionFactory.openSession();

        TaskEntityMapper mapper = session.getMapper(TaskEntityMapper.class);
        List<TaskEntity> entities = mapper.findAllValid();
        session.commit();
        session.close();

        return entities;
    }


    public List<TaskEntity> findAllValidByStatus(int status) {

        SqlSession session = sqlSessionFactory.openSession();

        TaskEntityMapper mapper = session.getMapper(TaskEntityMapper.class);
        List<TaskEntity> entities = mapper.findAllValidByStatus(status);
        session.commit();
        session.close();

        return entities;
    }


    public Integer insertTaskEntity(TaskEntity entity) {
        SqlSession session = sqlSessionFactory.openSession();
        TaskEntityMapper mapper = session.getMapper(TaskEntityMapper.class);
        Integer integer = mapper.insertTaskEntity(entity);
        session.commit();
        session.close();
        return integer;
    }

    public Integer updateTaskEntityById(TaskEntity taskEntity) {

        SqlSession session = sqlSessionFactory.openSession();
        TaskEntityMapper mapper = session.getMapper(TaskEntityMapper.class);
        Integer integer = mapper.updateTaskEntityById(taskEntity);
        session.commit();
        session.close();
        return integer;
    }

    public static void main(String[] args) {
        TaskDao dao = new TaskDao();

        List<TaskEntity> entities = dao.findAllValidByStatus(TaskStatusItem.TASK_STATUS_COMPLETED);

        System.out.println("size: " + entities.size());
        System.out.println(JSON.toJSONString(entities));
    }

}
