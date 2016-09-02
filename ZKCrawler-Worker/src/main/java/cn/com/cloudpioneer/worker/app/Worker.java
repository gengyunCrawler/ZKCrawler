package cn.com.cloudpioneer.worker.app;

import cn.com.cloudpioneer.worker.utils.RandomUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Administrator on 2016/9/1.
 */
public class Worker implements Closeable {

    private static final String MY_STATUS = "alive";
    private static final String TASKS_PATH = "/tasks";
    private static final String WORKERS_PATH = "/workers";

    private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);

    private String myId;
    private CuratorFramework client;
    private final PathChildrenCache myTasksCache;


    PathChildrenCacheListener myTaskCacheListener = new PathChildrenCacheListener() {
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) {
            /*if(event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                try{
                   // assignTask(event.getData().getPath().replaceFirst("/tasks/", ""),
                   //         event.getData().getData());
                } catch (Exception e) {
                   // LOG.error("Exception when assigning task.", e);
                }
            }*/

            System.out.println("================>>  Event is: " + event.getType());
            System.out.println("================>>  Event Data is: " + event.toString());
            System.out.println("================>>  Path: " + event.getData().getPath());
        }
    };

    private String getMyId() {

        return RandomUtils.getRandomString(10);
    }


    private void register() throws Exception {


        // 去 workers 节点下注册 worker 节点，短暂类型（persistent）
        client.create().withMode(CreateMode.PERSISTENT).forPath(WORKERS_PATH + "/" + myId, myId.getBytes());

        // 去 assigns 节点下注册 woker 节点，持久类型（ephemeral）
        client.create().withMode(CreateMode.EPHEMERAL).forPath(WORKERS_PATH + "/" + myId + "/status", MY_STATUS.getBytes());
    }

    public Worker(String hostPort, RetryPolicy retryPolicy) {

        myId = "worker-" + getMyId();
        LOGGER.info("Worker constructing, hostPort:" + hostPort);
        LOGGER.info("my work id: " + myId);
        this.client = CuratorFrameworkFactory.newClient(hostPort, retryPolicy);
        this.myTasksCache = new PathChildrenCache(this.client, WORKERS_PATH + "/" + myId, true);

    }


    public void startZK() {

        LOGGER.info("starting zookeeper client.");
        client.start();
        LOGGER.info("zookeeper client started.");
    }

    public void bootsrap() {

        try {
            register();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("register znode Exception");
        }

    }

    public void runForWorker() {

        myTasksCache.getListenable().addListener(myTaskCacheListener);
        try {
            myTasksCache.start();
        } catch (Exception e) {
            LOGGER.warn("myTasksCache starting Exception");
            e.printStackTrace();

        }

    }


    @Override
    public void close() throws IOException {

        LOGGER.info("closing worker");
        myTasksCache.close();
        client.close();
        LOGGER.info("worker closed.");

    }

    public static void main(String[] args) {

        Worker worker  = null;
        try {
            worker = new Worker("88.88.88.110:2181", new ExponentialBackoffRetry(1000, 5));
            worker.startZK();
            worker.bootsrap();
            worker.runForWorker();
            while (true) {

                    Thread.sleep(10000);

            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                worker.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
