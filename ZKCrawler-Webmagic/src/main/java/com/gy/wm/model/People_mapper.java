package com.gy.wm.model;

import org.apache.ibatis.annotations.*;

/**
 * <类详细说明>
 *
 * @Author： Huanghai
 * @Version: 2016-08-30
 **/
public interface People_mapper {

    @Insert("INSERT INTO people (id,name) VALUES (#{id}, #{name})")
    public void insert(People crawlData);
}
