package cn.com.cloudpioneer.master.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

/**
 * Created by Tijun on 2016/9/1.
 */
public class WorkersCacheListener implements PathChildrenCacheListener
{
    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception
    {

    }
}
