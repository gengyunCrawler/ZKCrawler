package com.gy.wm.mapper;

import com.gy.wm.plugins.newsExportPlugin.parse.ParserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Tijun on 2016/9/30.
 * @author TijunWang
 */
public interface ParserMapper {
    @Select("SELECT id,tid,config,viewAll,nextKey FROM article_parse_config WHERE tid=#{tid}")
    ParserEntity find(@Param("tid") String tid);
    @Insert("INSERT INTO  article_parse_config(tid,config,viewAll,nextKey) VALUES(#{tid},#{config},#{viewAll}#{nextKey})")
    void  insert(ParserEntity entity);
}
