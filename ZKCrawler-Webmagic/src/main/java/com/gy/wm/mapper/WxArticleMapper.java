package com.gy.wm.mapper;

import com.gy.wm.model.WxArticle;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * Created by Tijun on 2016/12/15.
 */
@Repository
public interface WxArticleMapper {
    @Insert("insert into wxArticle(id,title,content,wxName,url,wxBiz,tag,author,publishTime) values(#{id},#{title},#{content},#{wxName},#{url},#{wxBiz},#{tag},#{author},#{publishTime})")
    void insert(WxArticle wxArticle);
}
