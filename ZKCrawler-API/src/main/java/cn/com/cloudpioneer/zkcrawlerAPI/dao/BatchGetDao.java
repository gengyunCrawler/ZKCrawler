package cn.com.cloudpioneer.zkcrawlerAPI.dao;

import cn.com.cloudpioneer.zkcrawlerAPI.model.BatchGetInfo;
import cn.com.cloudpioneer.zkcrawlerAPI.model.BatchGetLog;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Created by TianyuanPan on 11/9/16.
 */

@Component
public class BatchGetDao {

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


    public List<BatchGetInfo> findAllGetInfo() {

        SqlSession session = sqlSessionFactory.openSession();

        BatchGetMapper mapper = session.getMapper(BatchGetMapper.class);
        List<BatchGetInfo> batchGetInfoList = mapper.findAllGetInfo();
        session.commit();
        session.close();

        return batchGetInfoList;
    }

    public List<BatchGetInfo> findAllGetInfoByNextSign(String nextSign) {

        SqlSession session = sqlSessionFactory.openSession();

        BatchGetMapper mapper = session.getMapper(BatchGetMapper.class);
        List<BatchGetInfo> batchGetInfoList = mapper.findAllGetInfoByNextSign(nextSign);
        session.commit();
        session.close();

        return batchGetInfoList;
    }


    public int updateGetInfos(List<BatchGetInfo> batchGetInfoList) {

        if (batchGetInfoList == null || batchGetInfoList.size() == 0)
            return 0;
        SqlSession session = sqlSessionFactory.openSession();
        int size = 0;

        BatchGetMapper mapper = session.getMapper(BatchGetMapper.class);

        for (BatchGetInfo item : batchGetInfoList)
            size += mapper.updateGetInfo(item);

        session.commit();
        session.close();

        return size;
    }


    public int updateGetInfo(BatchGetInfo batchGetInfo) {

        SqlSession session = sqlSessionFactory.openSession();
        int size = 0;

        BatchGetMapper mapper = session.getMapper(BatchGetMapper.class);

        size += mapper.updateGetInfo(batchGetInfo);

        session.commit();
        session.close();

        return size;
    }


    public List<BatchGetLog> findGetLogsByIdTaskAndNextSign(String idTask, String nextSign) {

        SqlSession session = sqlSessionFactory.openSession();

        BatchGetMapper mapper = session.getMapper(BatchGetMapper.class);
        List<BatchGetLog> batchGetLogs = mapper.findGetLogsByIdTaskAndNextSign(idTask, nextSign);
        session.commit();
        session.close();

        return batchGetLogs;
    }


    public int insertGetLog(BatchGetLog log) {

        SqlSession session = sqlSessionFactory.openSession();
        int size = 0;
        BatchGetMapper mapper = session.getMapper(BatchGetMapper.class);
        size += mapper.insertGetLog(log);
        session.commit();
        session.close();

        return size;
    }


    public int insertGetLogs(List<BatchGetLog> logs) {

        if (logs == null || logs.size() == 0)
            return 0;

        SqlSession session = sqlSessionFactory.openSession();
        int size = 0;
        BatchGetMapper mapper = session.getMapper(BatchGetMapper.class);
        for (BatchGetLog log : logs)
            size += mapper.insertGetLog(log);
        session.commit();
        session.close();

        return size;
    }

}
