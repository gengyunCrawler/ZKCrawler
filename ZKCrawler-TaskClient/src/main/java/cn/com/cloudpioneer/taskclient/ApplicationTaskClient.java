package cn.com.cloudpioneer.taskclient;

import cn.com.cloudpioneer.taskclient.app.TaskClient;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ResourceBundle;

/**
 * Created by TianyuanPan on 6/29/16.
 * <p>
 * 这是 worker 的执行入口，也是 spring boot 的入口。
 */


@SpringBootApplication
public class ApplicationTaskClient {

    /**
     * zookeeper 的链接字符串，从配置文件中获得。
     */
    private static String zkHostPort = ResourceBundle.getBundle("config").getString("ZK_CONNECTION_STRING");



    /**
     * 主函数。
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws Exception {

        /**
         * 启动 spring boot 服务。
         */
        SpringApplication.run(ApplicationTaskClient.class, args);

        /**
         * 初始化 task client。
         */
         TaskClient taskClient = TaskClient.initializeTaskClient(zkHostPort, new RetryNTimes(5, 1000), null, null, null, null);


        /**
         * 启动 task client。
         */
        taskClient.startTaskClient();


    }

}


