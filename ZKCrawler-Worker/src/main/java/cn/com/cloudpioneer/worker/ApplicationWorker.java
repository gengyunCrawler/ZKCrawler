package cn.com.cloudpioneer.worker;

import cn.com.cloudpioneer.worker.app.Worker;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ResourceBundle;

/**
 * Created by TianyuanPan on 6/29/16.
 * <p>
 * 这是 worker 的执行入口，也是 spring boot 的入口。
 */


@SpringBootApplication
public class ApplicationWorker {

    /**
     * zookeeper 的链接字符串，从配置文件中获得。
     */
    private static String zkHostPort = ResourceBundle.getBundle("config").getString("ZK_CONNECTION_STRING");


    /**
     * 初始化 worker。
     */
    private static Worker worker = Worker.initializeWorker(zkHostPort, new ExponentialBackoffRetry(1000, 5));

    /**
     * 主函数。
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {


        /**
         * 启动 spring boot 服务。
         */
        SpringApplication.run(ApplicationWorker.class, args);

        /**
         * 启动 worker。
         */
        worker.workerStart();

    }

}


