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
    @Select("SELECT id,tid,config FROM crawlerConfig WHERE tid=#{tid}")
    ParserEntity find(@Param("tid") String tid);
    @Insert("insert into crawlerConfig(tid,config) VALUES(#{tid},#{config})")
    void  insert(ParserEntity entity);
}
