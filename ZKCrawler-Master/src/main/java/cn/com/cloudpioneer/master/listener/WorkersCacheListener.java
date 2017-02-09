package cn.com.cloudpioneer.master.listener;

import cn.com.cloudpioneer.master.utils.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tijun on 2016/9/1.
 */
public class WorkersCacheListener implements TreeCacheListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkersCacheListener.class);

    String nodePath = "";

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent treeCacheEvent) throws Exception {

        switch (treeCacheEvent.getType()) {
            case NODE_ADDED:
                nodePath = treeCacheEvent.getData().getPath();
                LOGGER.info("===> NODE_ADDED Event, path: " + nodePath);

                /**
                 *  此处需要做一些具体处理，比如清理一些不存在的 worker 节点。
                 */

                break;
            case NODE_UPDATED:
                nodePath = treeCacheEvent.getData().getPath();
                LOGGER.info("NODE_UPDATED:" + nodePath);
                break;
            case NODE_REMOVED:

                nodePath = treeCacheEvent.getData().getPath();

                LOGGER.info("NODE_REMOVED:" + nodePath);
                try {
                    //删除过期的worker节点
                    String path = nodePath.replace("/status", "");
                    LOGGER.info("将删除:" + path);
                    if (CuratorUtils.isNodeExist(client, path)) {
                        CuratorUtils.deletePathAndChildren(client, path);
                    }
                    //
                    LOGGER.info("已将删除:" + path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  Pattern pattern=Pattern.compile(PATH_ROOT_WORKERS.concat("/").concat())
                break;
        }


    }
}
