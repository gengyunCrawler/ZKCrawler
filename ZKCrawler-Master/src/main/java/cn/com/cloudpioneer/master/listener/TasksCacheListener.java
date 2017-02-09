package cn.com.cloudpioneer.master.listener;

import cn.com.cloudpioneer.master.app.ValueDef;
import cn.com.cloudpioneer.master.utils.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Created by Tijun on 2016/9/1.
 */
public class TasksCacheListener implements TreeCacheListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TasksCacheListener.class);
    /**
     * 匹配 task 节点下的 worker 节点的正则表达式。
     */
    private final static Pattern TASK_WORKER = Pattern.compile(ValueDef.PATH_ROOT_TASKS + "/task-.*/worker-.*");

    String child = null;

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent treeCacheEvent) throws Exception {
        switch (treeCacheEvent.getType()) {

            case NODE_ADDED:

                child = treeCacheEvent.getData().getPath();
                LOGGER.info("===> taskCacheListener NODE_ADDED Event.");
                LOGGER.info("Event Path:" + child);
                //先判增加的节点是task或者task-*/worker?
                //节点为/tasks/task-*/worker-* 类型
                if (TASK_WORKER.matcher(child).matches()) {
                    String[] arr = child.split("/");
                    String taskId = arr[2];
                    String workerId = arr[3];
                    CuratorUtils.task4worker(client, taskId, workerId);

                }

                break;

            case NODE_REMOVED:
                child = treeCacheEvent.getData().getPath();
                LOGGER.info("===> taskCacheListener NODE_ADDED Event.");
                LOGGER.info("===> Event Path: " + child);
                //先判增加的节点是task或者task-*/worker?
                //节点为/tasks/task-*/worker-* 类型
                if (TASK_WORKER.matcher(child).matches()) {
                    String[] arr = child.split("/");
                    String worker = ValueDef.PATH_ROOT_WORKERS + "/" + arr[3];
                    String task = arr[2];
                    LOGGER.info("===> removing znode: " + worker + "/" + task);
                    CuratorUtils.deletePathAndChildren(client, worker + "/" + task);
                }
                break;

            case NODE_UPDATED:
                LOGGER.info("update");
                break;
            default:

                break;
        }
    }



}
