package cn.com.cloudpioneer.worker.mapper;

import cn.com.cloudpioneer.worker.model.TaskConfigEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */

@Repository
public interface TaskConfigEntityMapper {

    @Select("SELECT * FROM taskconfig WHERE idTask = #{id}")
    List<TaskConfigEntity> findTaskConfigsById(@Param("id") String id);

}
