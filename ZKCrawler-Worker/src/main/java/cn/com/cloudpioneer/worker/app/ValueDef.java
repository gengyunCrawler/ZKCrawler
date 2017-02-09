package cn.com.cloudpioneer.worker.app;

/**
 * Created by TianyuanPan on 2/8/2017.
 */
public final class ValueDef {

    // worker 中 MysTasks 用到的 Redis 数据库索引号。
    public static final int REDIS_INDEX_MY_TASKS_TABLE = 3;


    /**
     * ROOT_PATH_TASKS 是 zookeeper 中的任务根节点，节点是永久类型（persistent）。
     */
    public static final String ROOT_PATH_TASKS = "/zkcrawler_tasks";

    /**
     * ROOT_PATH_WORKERS 是 zookeeper 中挂载 worker 的根节点，节点是永久类型（persistent）。
     */
    public static final String ROOT_PATH_WORKERS = "/zkcrawler_workers";

    /**
     * ROOT_PATH_LOCK 是任务锁节点的根节点，节点是永久类型（persistent）。
     */
    public static final String ROOT_PATH_LOCK = "/zkcrawler_lock-4-tasks";
}
