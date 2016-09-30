package com.gy.wm.mapper;

import com.gy.wm.parse.CrawlerParserEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Administrator on 2016/9/30.
 */
public interface CrawlerParserMapper {
    @Select("SELECT id,tid,config FROM crawlerConfig WHERE tid=#{tid}")
    CrawlerParserEntity find(@Param("tid") String tid);
}
