package com.gy.wm.mapper;

import com.gy.wm.model.TaskEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <类详细说明:对taskEntity实体的操作>
 *
 * @Author： HuangHai
 * @Version: 2017-01-13
 **/
@Repository
public interface TaskEntityMapper {
    @Select("SELECT * FROM task WHERE id=#{id}")
    TaskEntity findById(@Param("id") String id);
}
