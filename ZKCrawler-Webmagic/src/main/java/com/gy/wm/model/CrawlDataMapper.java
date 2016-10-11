package com.gy.wm.model;

import org.apache.ibatis.annotations.Insert;

/**
 * <类详细说明:CrawlData的mapper类，使用mybatis进行在映射器上写sql>
 *
 * @Author： Huanghai
 * @Version: 2016-09-12
 **/
public interface CrawlDataMapper {
    @Insert("INSERT INTO crawlerdata_zktest(tid,url,statusCode,pass,type,rootUrl,fromUrl,text,html,title,startTime,crawlTime,publishTime,depthfromSeed,count,tag,fetched,author,sourceName) VALUES " +
            "(#{tid},#{url},#{statusCode},#{pass},#{type},#{rootUrl},#{fromUrl},#{text},#{html},#{title},#{startTime},#{crawlTime},#{publishTime},#{depthfromSeed},#{count},#{tag},#{fetched},#{author},#{sourceName})")
    public void saveToMysql(CrawlData crawlData);
}
