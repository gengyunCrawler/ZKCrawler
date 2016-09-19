package cn.com.cloudpioneer.taskclient.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * Created by TianyuanPan on 9/20/16.
 */
public class WorkersCacheListener implements TreeCacheListener {
    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {

    }
}
