package cn.com.cloudpioneer.worker.listener;

import cn.com.cloudpioneer.worker.app.Worker;
import cn.com.cloudpioneer.worker.model.TaskModel;
import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/5.
 */
public class MyTaskCacheListener implements TreeCacheListener {

    private static final Logger LOGGER = Logger.getLogger(MyTaskCacheListener.class);


    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {

        Worker worker = Worker.getThisWorker();

        try {

            switch (treeCacheEvent.getType()) {

                case NODE_ADDED:
                    Pattern taskInMyWorker = Pattern.compile(Worker.WORKERS_ROOT_PATH + "/" + worker.getWorkerId() + "/task-.*");
                    String treeChildren = treeCacheEvent.getData().getPath();
                    String taskPath = Worker.TASKS_ROOT_PATH;
                    LOGGER.info("znode add event, treeChildren: " + treeChildren);


                    if (taskInMyWorker.matcher(treeChildren).matches()) {

                        taskPath += "/" + treeChildren.split("/")[3];
                        LOGGER.info("get the task path: " + taskPath);
                        byte[] taskData = worker.getZnodeData(taskPath);

                        if (taskData == null){
                            LOGGER.error("get task data from znode error, task will not start, return this Event Handler.");
                            return;
                        }

                        TaskModel taskModel = new TaskModel(taskPath, taskData);
                        taskModel.getEntity().setTimeStart(new Date());

                        worker.addTaskToRunning(taskModel);

                    } else
                        LOGGER.info("task in my worker not match .............");


                    break;
                case NODE_UPDATED:
                    /* ignore this case */
                    LOGGER.info("node updated event.");
                    break;
                case NODE_REMOVED:
                   /* ignore this case */
                    LOGGER.info("node removed event.");
                    break;
                case CONNECTION_LOST:
                    LOGGER.info("connection lost event.");
                    break;
                case CONNECTION_RECONNECTED:
                    LOGGER.info("connection reconnected event.");
                    break;
                case CONNECTION_SUSPENDED:
                    /* ignore this case */
                    LOGGER.info("connection suspended event.");
                    break;
                case INITIALIZED:
                    /* ignore this case */
                    LOGGER.info("initialized event.");
                    break;
            }

        } catch (Exception e) {

        }

    }
}
