package com.gy.wm.mapper;

import com.gy.wm.model.TaskConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Tijun on 2016/11/14.
 * @author TijunWang
 */
@Repository
public interface TaskConfigMapper {

    @Select("SELECT id,idTask,confType,confValue,addDate FROM taskconfig WHERE idTask=#{idTask}")
    TaskConfig findByIdTask(@Param("idTask") String idTask);

    @Select("SELECT id,idTask,confType,confValue,addDate FROM taskconfig WHERE idTask>#{id}-1")
    List<TaskConfig> findListIdStart(int id);
}
