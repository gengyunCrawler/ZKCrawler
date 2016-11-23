package com.gy.wm.dao;

import com.gy.wm.entry.Crawl;
import com.gy.wm.entry.TaskConfig;
import com.gy.wm.mapper.TaskConfigMapper;
import com.gy.wm.model.CrawlData;
import com.gy.wm.model.CrawlDataMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

/**
 * <类详细说明>
 *
 * @Author： Huanghai
 * @Version: 2016-11-23
 **/
@Repository
public class CrawlDataDao {

    private static SqlSessionFactory sqlSessionFactory = SQLSessionFactory.getSqlSessionFactory();

    /**
     * 添加CrawlData
     * @param crawlData
     */
    public void insertCrawlData(CrawlData crawlData)   {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        CrawlDataMapper mapper = sqlSession.getMapper(CrawlDataMapper.class);
        mapper.saveToMysql(crawlData);
        sqlSession.commit();
        sqlSession.close();
    }

    /**
     * 通过Id查询CrawlData
     * @param seqeueID
     * @return
     */
    public CrawlData findCrawlDataById(int seqeueID){

        SqlSession sqlSession = sqlSessionFactory.openSession();
        CrawlDataMapper mapper = sqlSession.getMapper(CrawlDataMapper.class);
        CrawlData crawlData = mapper.findCrawlDataById(seqeueID);
        sqlSession.close();
        return crawlData;

    }
}
