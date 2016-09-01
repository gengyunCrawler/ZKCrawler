package cn.com.cloudpioneer.worker.app;

import cn.com.cloudpioneer.worker.utils.RandomUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Administrator on 2016/9/1.
 */
public class Worker implements Closeable {

    private static final String ASSIGNS_PATH = "/assigns";
    private static final String TASKS_PATH = "/tasks";
    private static final String WORKERS_PATH = "/workers";

    private static final Logger LOGGER = Logger.getLogger(Worker.class);

    private String myId;
    private CuratorFramework client;
    private final PathChildrenCache assignsCache;


    private String getMyId() {

        return RandomUtils.getRandomString(10);
    }


    public Worker(String hostPort, RetryPolicy retryPolicy) {

        myId = "worker-" + getMyId();
        LOGGER.info("Worker constructing, hostPort:" + hostPort);
        LOGGER.info("my work id: " + myId);
        this.client = CuratorFrameworkFactory.newClient(hostPort, retryPolicy);
        this.assignsCache = new PathChildrenCache(this.client, ASSIGNS_PATH, true);

    }

    public void startZK() {

        LOGGER.info("starting zookeeper client.");
        client.start();
    }

    public void bootsrap() throws Exception {

        // 去 workers 节点下注册 worker 节点，短暂类型（ephemeral）
        client.create().withMode(CreateMode.EPHEMERAL).forPath(WORKERS_PATH + "/" + myId, myId.getBytes());

        // 去 assigns 节点下注册 woker 节点，持久类型（persistent）
        client.create().withMode(CreateMode.PERSISTENT).forPath(ASSIGNS_PATH + "/" + myId, myId.getBytes());
    }

    @Override
    public void close() throws IOException {

    }
}
