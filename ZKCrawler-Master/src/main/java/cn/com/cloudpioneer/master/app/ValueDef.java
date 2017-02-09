package cn.com.cloudpioneer.master.app;

/**
 * Created by TianyuanPan on 2/8/2017.
 */
public final class ValueDef {

    /**
     * zookeeper 三个角色 角色TaskCLient 角色Master 角色Worker 的根节点。
     */
    public static final String PATH_ROOT_TASKS = "/zkcrawler_tasks";
    public static final String PATH_ROOT_MASTER = "/zkcrawler_master";
    public static final String PATH_ROOT_WORKERS = "/zkcrawler_workers";
}
