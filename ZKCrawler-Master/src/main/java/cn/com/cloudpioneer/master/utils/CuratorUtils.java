package cn.com.cloudpioneer.master.utils;

import cn.com.cloudpioneer.master.app.ValueDef;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
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
     * 节点是否有子节点。
     *
     * @param client   客户端
     * @param nodePath 节点路径
     * @return 返回 true 为有子节点，否则返回 false。
     * @throws Exception
     */
    public static boolean isHaveChildren(CuratorFramework client, String nodePath) throws Exception {

        List<String> children = client.getChildren().forPath(nodePath);

        if (children == null || children.size() == 0)
            return false;

        return true;

    }


    /**
     * 检查 parent 节点是否有指定的节点 specificChild。
     *
     * @param client        客户端。
     * @param parent        父节点路径。
     * @param specificChild 指定的子节点名称。
     * @return 节点 parent 中有指定的子节点 specificChild 返回 true，否则返回 false。
     * @throws Exception
     */
    public static boolean isHaveSpecificChild(CuratorFramework client, String parent, String specificChild) throws Exception {

        Stat stat = client.checkExists().forPath(parent + "/" + specificChild);

        if (stat != null) {
            return true;
        }

        return false;

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


    /**
     * 将task复制到相应的worker目录下
     *
     * @param taskId
     * @param workerId
     */
    public static void task4worker(CuratorFramework client, String taskId, String workerId) {

        try {
            //task下的配置信息
            byte[] taskData = client.getData().forPath(ValueDef.PATH_ROOT_TASKS + "/" + taskId);
            //判断worker是否存在
            Stat stat = client.checkExists().forPath(ValueDef.PATH_ROOT_WORKERS + "/" + workerId);
            if (stat != null) {
                //将任务挂载到worker下面

                String task4workerPath = ValueDef.PATH_ROOT_WORKERS.concat("/").concat(workerId).concat("/").concat(taskId);

                if (!CuratorUtils.isNodeExist(client, task4workerPath)) {
                    client.create().withMode(CreateMode.PERSISTENT).forPath(task4workerPath, taskData);
                }

            } else {
                //当任务分配要挂载的worker不存在时删除具体任务下的worker
                client.delete().forPath(ValueDef.PATH_ROOT_TASKS.concat("/").concat(taskId).concat("/").concat(workerId));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
