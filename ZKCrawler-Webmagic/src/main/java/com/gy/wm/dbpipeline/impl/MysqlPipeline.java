package com.gy.wm.dbpipeline.impl;

import com.gy.wm.model.CrawlData;
import com.gy.wm.model.CrawlDataMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by TianyuanPan on 5/4/16.
 */

public class MysqlPipeline implements Pipeline {
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

    @Override
    public void process(ResultItems resultItems, Task task) {
        CrawlData crawlData = resultItems.get("crawlerData");
        insertTosql(crawlData);
    }

    public void insertTosql(CrawlData crawlData)   {
        SqlSession session = sqlSessionFactory.openSession();
        CrawlDataMapper mapper = session.getMapper(CrawlDataMapper.class);
        mapper.saveToMysql(crawlData);
        session.commit();
        session.close();
    }

}
