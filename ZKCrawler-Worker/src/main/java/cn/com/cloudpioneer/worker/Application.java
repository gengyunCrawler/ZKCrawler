package cn.com.cloudpioneer.worker;

import cn.com.cloudpioneer.worker.app.Worker;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ResourceBundle;

/**
 * Created by TianyuanPan on 6/29/16.
 */

@SpringBootApplication
public class Application {

    private static String zkHostPort = ResourceBundle.getBundle("config").getString("ZK_CONNECTION_STRING");

    public static void main(String[] args) throws InterruptedException {

        Worker worker = Worker.initializeWorker(zkHostPort, new ExponentialBackoffRetry(1000, 10));
        SpringApplication.run(Application.class, args);
        worker.workerStart();
    }

}


