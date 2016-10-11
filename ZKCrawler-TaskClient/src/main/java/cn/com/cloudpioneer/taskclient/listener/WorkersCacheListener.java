package cn.com.cloudpioneer.taskclient.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.log4j.Logger;

/**
 * Created by TianyuanPan on 9/20/16.
 */
public class WorkersCacheListener implements TreeCacheListener {

    private static final Logger LOGGER = Logger.getLogger(WorkersCacheListener.class);

    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {

        switch (treeCacheEvent.getType()) {
            case NODE_ADDED:
                LOGGER.info("====> Workers Listener, NODE_ADDED event.");
                break;
            case NODE_UPDATED:
                LOGGER.info("====> Workers Listener, NODE_UPDATED event.");
                break;
            case NODE_REMOVED:
                LOGGER.info("====> Workers Listener, NODE_REMOVED event.");
                break;
            case INITIALIZED:
                LOGGER.info("====> Workers Listener, INITIALIZED event.");
                break;
            case CONNECTION_SUSPENDED:
                LOGGER.info("====> Workers Listener, CONNECTION_SUSPENDED event.");
                break;
            case CONNECTION_LOST:
                LOGGER.info("====> Workers Listener, CONNECTION_LOST event.");
                break;
            case CONNECTION_RECONNECTED:
                LOGGER.info("====> Workers Listener, CONNECTION_RECONNECTED event.");
                break;
            default:
                break;
        }

    }
}
