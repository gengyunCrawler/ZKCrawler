package cn.com.cloudpioneer.taskclient.mapper;

import cn.com.cloudpioneer.taskclient.entity.TaskEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Tianjinjin on 2016/9/2.
 */
@Repository
public interface TaskEntityMapper {
    //查询所有数据
    List<TaskEntity> findAll();

    //添加数据
    Integer insertTaskEntity(TaskEntity taskEntity);

    //对任务进行软删除，既把deleteFlag字段设置为false
    Integer deleteTaskEntity(String id);

    //通过id从数据库读取task实体
    TaskEntity findById(String id);

    //通过类型从数据库读取task实体
    List<TaskEntity> findByType(int type);

    //通过上一次（最近一次）抓取的时间从数据库读取task实体
    List<TaskEntity> findByTimeLastCrawl(Date timeLastCrawl);

    //若爬取任务没有完成，把数据库的timeLastCrawl字段更新为最新的这次
    void updateTimeLastCrawl(@Param("id") String id, @Param("newTime") Date newTime);

    //更新数据
    void updateTaskEntityById(TaskEntity taskEntity);


}
