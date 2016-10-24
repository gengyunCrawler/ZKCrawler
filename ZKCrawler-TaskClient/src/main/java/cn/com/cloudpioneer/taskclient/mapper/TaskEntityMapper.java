package cn.com.cloudpioneer.taskclient.mapper;

import cn.com.cloudpioneer.taskclient.model.TaskEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * Created by Tianjinjin on 2016/9/2.
 */

public interface TaskEntityMapper {
    //查询所有数据
    @Select("SELECT * FROM tbCrawlerTask")
    List<TaskEntity> findAll();


    @Select("SELECT * FROM tbCrawlerTask WHERE deleteFlag = false")
    List<TaskEntity> findAllValid();


    @Select("SELECT * FROM tbCrawlerTask WHERE deleteFlag = false AND status = #{status}")
    List<TaskEntity> findAllValidByStatus(@Param("status") int status);

    //添加数据
    @Insert("INSERT  INTO  tbCrawlerTask " +
            "(id,idUser,name,remark,type,depthCrawl,depthDynamic,pass,weight," +
            "threads,completeTimes,cycleRecrawl,status,deleteFlag,timeStart," +
            "timeStop,timeLastCrawl,costLastCrawl,scheduleType,workerNumber,createDate," +
            "downloader,parser,tags)" +
            "VALUES(" +
            "#{id},#{idUser},#{name},#{remark},#{type},#{depthCrawl}," +
            "#{depthDynamic},#{pass},#{weight},#{threads},#{completeTimes}," +
            "#{cycleRecrawl},#{status},#{deleteFlag},#{timeStart},#{timeStop}," +
            "#{timeLastCrawl},#{costLastCrawl},#{scheduleType},#{workerNumber},#{createDate}," +
            "#{downloader},#{parser},#{tags}" +
            ")")
    Integer insertTaskEntity(TaskEntity taskEntity);


    //对任务进行软删除，既把deleteFlag字段设置为false
    @Update("UPDATE tbCrawlerTask SET deleteFlag = false WHERE id = #{id}")
    Integer deleteTaskEntity(@Param("id") String id);

    //通过id从数据库读取task实体
    @Select("SELECT  * FROM tbCrawlerTask WHERE id = #{id}")
    TaskEntity findById(String id);

    //通过类型从数据库读取task实体
    @Select("SELECT  * FROM tbCrawlerTask WHERE type = #{type}")
    List<TaskEntity> findByType(int type);

    //通过上一次（最近一次）抓取的时间从数据库读取task实体
    @Select("SELECT  * FROM tbCrawlerTask WHERE timeLastCrawl  BETWEEN #{dateStart} AND #{dateEnd}")
    List<TaskEntity> findByTimeLastCrawl(@Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

    //若爬取任务没有完成，把数据库的timeLastCrawl字段更新为最新的这次
    Integer updateTimeLastCrawl(@Param("id") String id, @Param("newTime") Date newTime);

    //更新数据
    @Update("UPDATE tbCrawlerTask " +
            "SET " +
            "idUser=#{idUser},name=#{name},remark=#{remark},type=#{type}," +
            "depthCrawl=#{depthCrawl},depthDynamic=#{depthDynamic},pass=#{pass}," +
            "weight=#{weight},threads=#{threads},completeTimes=#{completeTimes}," +
            "cycleRecrawl=#{cycleRecrawl},status=#{status},deleteFlag=#{deleteFlag}," +
            "timeStart=#{timeStart},timeStop=#{timeStop},timeLastCrawl=#{timeLastCrawl}," +
            "costLastCrawl=#{costLastCrawl},scheduleType=#{scheduleType},workerNumber=#{workerNumber}," +
            "createDate=#{createDate},downloader=#{downloader},parser=#{parser},tags=#{tags} WHERE id=#{id}")
    Integer updateTaskEntityById(TaskEntity taskEntity);


}
