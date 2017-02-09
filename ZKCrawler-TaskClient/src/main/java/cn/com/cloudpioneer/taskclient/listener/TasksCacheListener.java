package cn.com.cloudpioneer.taskclient.listener;

import cn.com.cloudpioneer.taskclient.app.TaskClient;
import cn.com.cloudpioneer.taskclient.app.ValueDef;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Created by TianyuanPan on 9/20/16.
 */
public class TasksCacheListener implements TreeCacheListener {

    private static final Logger LOGGER = Logger.getLogger(TasksCacheListener.class);


    private boolean isThisNodeUpdated(String nodePath, String nodeName) {

        Pattern taskStatusPath = Pattern.compile(ValueDef.ROOT_PATH_TASKS + "/task-.*/status");
        Pattern taskPath = Pattern.compile(ValueDef.ROOT_PATH_TASKS + "/task-.*");
        Pattern workerPath = Pattern.compile(ValueDef.ROOT_PATH_TASKS + "/task-.*/worker-.*");

        switch (nodeName) {
            case "task":
                return taskPath.matcher(nodePath).matches();
            case "status":
                return (taskPath.matcher(nodePath).matches() && taskStatusPath.matcher(nodePath).matches());
            case "worker":
                return (taskPath.matcher(nodePath).matches() && workerPath.matcher(nodePath).matches());
            default:
                return false;
        }

    }

    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {

        String nodePath;
        TaskClient taskClient = TaskClient.getThisTaskClient();

        switch (treeCacheEvent.getType()) {
            case NODE_ADDED:
                nodePath = treeCacheEvent.getData().getPath();
                LOGGER.info("====> Tasks Listener, NODE_ADDED event.");
                LOGGER.info("====> node path: " + nodePath);
                break;
            case NODE_UPDATED:
                nodePath = treeCacheEvent.getData().getPath();
                LOGGER.info("====> Tasks Listener, NODE_UPDATED event.");
                LOGGER.info("====> node path: " + nodePath);

                if (isThisNodeUpdated(nodePath, "status") &&
                        taskClient.taskUpdateProcess(nodePath)) {
                    taskClient.tasksCreator();
                }


                break;
            case NODE_REMOVED:
                nodePath = treeCacheEvent.getData().getPath();
                LOGGER.info("====> Tasks Listener, NODE_REMOVED event.");
                LOGGER.info("====> node path: " + nodePath);
                if (isThisNodeUpdated(nodePath, "task")) {
                    taskClient.tasksCreator();
                }
                break;
            case INITIALIZED:
                LOGGER.info("====> Tasks Listener, INITIALIZED event.");
                break;
            case CONNECTION_SUSPENDED:
                LOGGER.info("====> Tasks Listener, CONNECTION_SUSPENDED event.");
                break;
            case CONNECTION_LOST:
                LOGGER.info("====> Tasks Listener, CONNECTION_LOST event.");
                break;
            case CONNECTION_RECONNECTED:
                LOGGER.info("====> Tasks Listener, CONNECTION_RECONNECTED event.");
                break;
            default:
                break;
        }

    }
}
