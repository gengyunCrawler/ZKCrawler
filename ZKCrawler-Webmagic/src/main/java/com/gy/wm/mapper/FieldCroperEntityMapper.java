package com.gy.wm.mapper;

import com.gy.wm.model.FieldCroperEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;


/**
 * <类详细说明:字段精确提取规则>
 *
 * @Author： Huanghai
 * @Version: 2016-11-28
 **/
public interface FieldCroperEntityMapper {

    @Insert("INSERT INTO field_croper(tid,cropRule) VALUES(#{tid},#{cropRule})")
    int insertRules(FieldCroperEntity fieldCroperEntity);

    @Select("SELECT * FROM field_croper WHERE tid=#{tid}")
    FieldCroperEntity findById(String tid);
}