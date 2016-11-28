package com.gy.wm.mapper;

import com.gy.wm.model.CrawlData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * <类详细说明:CrawlData的mapper类，使用mybatis进行在映射器上写sql>
 *
 * @Author： Huanghai
 * @Version: 2016-09-12
 **/
public interface CrawlDataMapper {
    @Insert("INSERT INTO crawlerdata_duocai(tid,url,statusCode,pass,type,rootUrl,fromUrl,text,html,title,startTime,crawlTime,publishTime,depthfromSeed,count,tag,fetched,author,sourceName,parsedData) VALUES " +
            "(#{tid},#{url},#{statusCode},#{pass},#{type},#{rootUrl},#{fromUrl},#{text},#{html},#{title},#{startTime},#{crawlTime},#{publishTime},#{depthfromSeed},#{count},#{tag},#{fetched},#{author},#{sourceName},#{parsedData})")
    public void saveToMysql(CrawlData crawlData);

  /*@Insert("INSERT INTO zk_test(url,data) values(#{url},#{parsedData})")
  public void saveToMysql(CrawlData crawlData);*/

/*    @Insert("INSERT INTO crawlerdata(tid,url,statusCode,pass,type,rootUrl,fromUrl,text,html,title,startTime,crawlTime,publishTime,depthfromSeed,count,tag,fetched,author,sourceName,jsonData) VALUES " +
            "(#{tid},#{url},#{statusCode},#{pass},#{type},#{rootUrl},#{fromUrl},#{text},#{html},#{title},#{startTime},#{crawlTime},#{publishTime},#{depthfromSeed},#{count},#{tag},#{fetched},#{author},#{sourceName},#{jsonData})")
    public void saveToMysql(CrawlData crawlData);*/

    @Select("SELECT * FROM  crawlerdata_features_dep WHERE seqeueID=#{seqeueID}")
    public CrawlData findCrawlDataById(int seqeueID);
}
