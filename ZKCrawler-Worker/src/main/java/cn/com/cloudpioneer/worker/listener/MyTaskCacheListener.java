package cn.com.cloudpioneer.worker.listener;

import cn.com.cloudpioneer.worker.app.GetTaskConfigs;
import cn.com.cloudpioneer.worker.app.Worker;
import cn.com.cloudpioneer.worker.model.TaskModel;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by TianyuanPan on 2016/9/5.
 * <p>
 * 类 MyTaskCacheListener 实现 zookeeper 中的 TreeCacheListener,
 * 用于监听节点的变化情况，worker 将根据这些变化事件来完成相应的工作。
 * 事件的处理在方法 childEvent 中进行。
 */
public class MyTaskCacheListener implements TreeCacheListener {

    /**
     * 日志成员。
     */
    private static final Logger LOGGER = Logger.getLogger(MyTaskCacheListener.class);


    /**
     * 重载其接口的方法，此方法处理节点事件。
     * 这是个回调方法。
     * 几个要处理的事件如下：
     * <p>
     * 节点增加事件（NODE_ADDED）：
     * --------------------------- 先取得增加的路径，看是否是任务 task 节点增加，若是，则找到 task 的路径，
     * 并读取任务信息，然后启动任务。
     * <p>
     * 节点移除事件（NODE_REMOVED）：
     * ---------------------------- 先取得移除的路径，看是否是任务 task 节点，若是，进行任务的停止操作。
     *
     * @param curatorFramework
     * @param treeCacheEvent
     * @throws Exception
     */
    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {

        /**
         * 获取 worker。
         */
        Worker worker = Worker.getThisWorker();

        try {

            String treeChildren = treeCacheEvent.getData().getPath();
            String taskPath = Worker.ROOT_PATH_TASKS;

            switch (treeCacheEvent.getType()) {

                case NODE_ADDED:
                    Pattern taskInMyWorker = Pattern.compile(Worker.ROOT_PATH_WORKERS + "/" + worker.getWorkerId() + "/task-.*");

                    LOGGER.info("znode add event, treeChildren: " + treeChildren);


                    if (taskInMyWorker.matcher(treeChildren).matches()) {

                        taskPath += "/" + treeChildren.split("/")[3];
                        LOGGER.info("get the task path: " + taskPath);
                        byte[] taskData = worker.getZnodeData(taskPath);

                        if (taskData == null) {
                            LOGGER.error("get task data from znode error,  task will not start, return this Event Handler.");
                            return;
                        }

                        TaskModel taskModel = new TaskModel(taskPath, taskData);
                        taskModel.getEntity().setTimeStart(new Date());
                        taskModel.getEntity().setTimeLastCrawl(new Date());
                        GetTaskConfigs getTaskConfigs = new GetTaskConfigs();
                        taskModel.setConfigs(GetTaskConfigs.configsParser(getTaskConfigs.findConfigs(taskModel.getEntity().getId())));
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
                    LOGGER.info("node removed event， treeChildren: " + treeChildren);
                    Pattern taskRemove = Pattern.compile(Worker.ROOT_PATH_WORKERS + "/" + worker.getWorkerId() + "/task-.*");
                    if (taskRemove.matcher(treeChildren).matches()) {

                        taskPath += "/" + treeChildren.split("/")[3];
                        LOGGER.info("get the task path: " + taskPath);
                        byte[] taskData = worker.getZnodeData(taskPath);

                        if (taskData == null) {
                            LOGGER.error("get task data from znode error, task will not stop, return this Event Handler.");
                            return;
                        }

                        TaskModel taskModel = new TaskModel(taskPath, taskData);
                        if (!worker.containsTask(taskModel.getEntity().getId())) {
                            LOGGER.info("==> the removed task not in myTasks, return this Event Handler.");
                            return;
                        }
                        worker.myTaskWriteBack(taskModel.getEntity().getId());
                        worker.removeTaskInRunning(taskModel);

                    } else
                        LOGGER.info("task remove in my worker not match .............");
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
