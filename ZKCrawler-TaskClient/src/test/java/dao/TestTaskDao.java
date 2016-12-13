package dao;

import cn.com.cloudpioneer.taskclient.dao.TaskDao;
import cn.com.cloudpioneer.taskclient.model.TaskEntity;
import cn.com.cloudpioneer.taskclient.model.TaskStatusItem;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.List;

/**
 * Created by TianyuanPan on 12/6/2016.
 */
public class TestTaskDao {


    @Test
    public void testDao() {
        TaskDao dao = new TaskDao();

        List<TaskEntity> entities = dao.findAllValidByStatus(TaskStatusItem.TASK_STATUS_COMPLETED);

        System.out.println("size: " + entities.size());
        System.out.println(JSON.toJSONString(entities));
    }
}
