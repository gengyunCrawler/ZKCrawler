package cn.com.cloudpioneer.zkcrawlerAPI.dao;

import cn.com.cloudpioneer.zkcrawlerAPI.model.BatchGetInfo;
import cn.com.cloudpioneer.zkcrawlerAPI.model.BatchGetLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by TianyuanPan on 11/9/16.
 */


public interface BatchGetMapper {


    @Select("SELECT * FROM hbase_ddp_batch_get_info")
    List<BatchGetInfo> findAllGetInfo();


    @Select("SELECT * FROM hbase_ddp_batch_get_info WHERE nextSign=#{nextSign}")
    List<BatchGetInfo> findAllGetInfoByNextSign(@Param("nextSign") String nextSign);


    @Update("UPDATE hbase_ddp_batch_get_info SET nextSign=#{nextSign}," +
            "nextRow=#{nextRow},lastSize=#{lastSize},allSize={#allSize}" +
            "updateTime=#{updateTime} WHERE idTask=#{idTask}")
    Integer updateGetInfo(BatchGetInfo batchGetInfo);


    @Select("SELECT * FROM hbase_ddp_batch_get_log WHERE idTask=#{idTask} AND nextSign=#{nextSign}")
    List<BatchGetLog> findGetLogsByIdTaskAndNextSign(@Param("idTask") String idTask, @Param("nextSign") String nextSign);


    @Insert("INSERT INTO hbase_ddp_batch_get_log (idTask,nextSign,logInfo,createTime) VALUES (#{idTask},#{nextSign},#{logInfo},#{createTime})")
    Integer insertGetLog(BatchGetLog log);

}
