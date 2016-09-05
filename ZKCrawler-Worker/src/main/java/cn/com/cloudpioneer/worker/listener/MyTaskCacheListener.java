package cn.com.cloudpioneer.worker.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * Created by Administrator on 2016/9/5.
 */
public class MyTaskCacheListener implements TreeCacheListener{

    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {


        try {

            switch (treeCacheEvent.getType()) {
                case NODE_ADDED:
                    System.out.println("================>>  Event is: " + treeCacheEvent.getType());
                    System.out.println("================>>  Event Data is: " + treeCacheEvent.toString());
                    ChildData data = treeCacheEvent.getData();
                    if (data!=null)
                        System.out.println("================>>  Path: " + data.getPath());

                    break;
                case NODE_UPDATED:
                    break;
                case NODE_REMOVED:
                    break;
                case CONNECTION_LOST:
                    break;
                case CONNECTION_RECONNECTED:
                    break;
                case CONNECTION_SUSPENDED:
                    break;
                case INITIALIZED:
                    System.out.println("================>>  Event is: " + treeCacheEvent.getType());
                    break;
            }

        } catch (Exception e) {

        }

    }
}
