package com.gy.wm.dbpipeline.impl;

import com.alibaba.fastjson.JSONObject;
import com.gy.wm.mapper.WxArticleMapper;
import com.gy.wm.model.CrawlData;
import com.gy.wm.mapper.CrawlDataMapper;
import com.gy.wm.model.WxArticle;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(MysqlPipeline.class);

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

        if(null != crawlData)   {
            //insertTosql(crawlData);
            insertWxArticle(crawlData);
        }
    }

    public void insertTosql(CrawlData crawlData)   {
        SqlSession session = sqlSessionFactory.openSession();
        CrawlDataMapper mapper = session.getMapper(CrawlDataMapper.class);
        mapper.saveToMysql(crawlData);
        session.commit();
        session.close();
    }
    private void insertWxArticle(CrawlData crawlData){
        JSONObject object = JSONObject.parseObject(crawlData.getParsedData());
        String title = object.getString("title");
        String content = object.getString("content");
        String publishTime = object.getString("publishTime");
        String wxName = object.getString("sourceName");
        String tag = object.getString("tag");
        String author = object.getString("author");
        String url = crawlData.getUrl();
        String wxBiz = crawlData.getUrl().split("==")[0].split("=")[1];

        WxArticle wxArticle = new WxArticle(title,content,wxName,url,wxBiz,tag,author,publishTime);
        insertWxToMysql(wxArticle);


    }
    private void insertWxToMysql(WxArticle wxArticle){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        WxArticleMapper mapper = sqlSession.getMapper(WxArticleMapper.class);
        mapper.insert(wxArticle);
        sqlSession.commit();
        sqlSession.close();
    }

}
