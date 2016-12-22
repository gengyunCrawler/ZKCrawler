package com.gy.wm.mapper;

import com.gy.wm.model.WxArticle;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Tijun on 2016/12/15.
 */
@Repository
public interface WxArticleMapper {
    @Insert("insert into wxArticle(id,title,content,wxName,url,wxBiz,tag,author,publishTime) values(#{id},#{title},#{content},#{wxName},#{url},#{wxBiz},#{tag},#{author},#{publishTime})")
    void insert(WxArticle wxArticle);
    @Select("select url from wxArticle_copy where content is null")
    List<String> findNullContent();
    @Update("update wxArticle_copy set content = #{content} where url=#{url}")
    void updateArticle(@Param("content")String content, @Param("url")String url);

}
