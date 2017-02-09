package cn.com.cloudpioneer.taskclient.app;

/**
 * Created by TianyuanPan on 2/8/2017.
 */
public final class ValueDef {

    /**
     * 以下常量为 zookeeper 里的相关根节点。
     */
    public static final String ROOT_PATH_LOCK = "/zkcrawler_lock-4-tasks";
    public static final String ROOT_PATH_TASKS = "/zkcrawler_tasks";
    public static final String ROOT_PATH_WORKERS = "/zkcrawler_workers";
    public static final String ROOT_PATH_CLIENT = "/zkcrawler_task-client";


    // task client 中 MysTasks 用到的 Redis 数据库索引号。
    public static final int REDIS_INDEX_MY_TASKS_TABLE = 3;

}
