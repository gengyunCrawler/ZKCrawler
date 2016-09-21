package cn.com.cloudpioneer.taskclient.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.log4j.Logger;

/**
 * Created by TianyuanPan on 9/20/16.
 */
public class TasksCacheListener implements TreeCacheListener {

    private static final Logger LOGGER = Logger.getLogger(TasksCacheListener.class);

    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {

        String nodePath = null;

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
                break;
            case NODE_REMOVED:
                nodePath = treeCacheEvent.getData().getPath();
                LOGGER.info("====> Tasks Listener, NODE_REMOVED event.");
                LOGGER.info("====> node path: " + nodePath);
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
