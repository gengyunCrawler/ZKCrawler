package cn.com.cloudpioneer.master.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Tijun on 2016/9/1.
 */
public class CuratorUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CuratorUtils.class);

    /**
     * 删除节点
     *
     * @param path
     * @throws Exception
     */
    public static void deletePath(CuratorFramework client, String path) throws Exception {
        /*
         * Delete assignment
         */
        LOG.info("Deleting assignment: {}", path);
        client.delete().inBackground().forPath(path);
    }

    /**
     * 清除该path下的所有子节点
     *
     * @param path
     * @throws Exception
     */
    public static void deleteChidrenForPath(CuratorFramework client, String path) throws Exception {
        List<String> children = client.getChildren().forPath(path);
        for (String child : children) {
            client.delete().inBackground().forPath(path + "/" + child);
        }
    }

    /**
     * 删除该目录及其子节点
     *
     * @param path
     * @throws Exception
     */
    public static void deletePathAndChildren(CuratorFramework client, String path) throws Exception {
        deleteChidrenForPath(client, path);
        client.delete().inBackground().forPath(path);
    }

    public static boolean isNodeExist(CuratorFramework client, String node) throws Exception {
        Stat stat = client.checkExists().forPath(node);
        if (stat != null) {
            return true;
        }
        return false;
    }
}
