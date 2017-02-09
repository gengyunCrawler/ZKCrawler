package cn.com.cloudpioneer.taskclient;

import cn.com.cloudpioneer.taskclient.app.TaskClient;
import cn.com.cloudpioneer.taskclient.utils.JedisPoolUtil;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.PropertyResourceBundle;
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
    private static String zkHostPort;
    /**
     * 初始化 task client。
     */
    private static TaskClient taskClient;


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

        createPidFile();

        /**
         * 初始化 Redis 工具
         */
        String redisHost = ResourceBundle.getBundle("config").getString("REDIS_HOST");
        int redisPort = Integer.parseInt(ResourceBundle.getBundle("config").getString("REDIS_PORT"));
        JedisPoolUtil.initJedisPool(redisHost, redisPort);

        /**
         * 获取主机配置。
         */
        zkHostPort = ResourceBundle.getBundle("config").getString("ZK_CONNECTION_STRING");

        /**
         * 初始化taskClient角色.
         */
        taskClient = TaskClient.initializeTaskClient(zkHostPort, new RetryNTimes(5, 1000), null, null, null, null);

        /**
         * 启动 task client。
         */
        taskClient.startTaskClient();


    }


    private static void createPidFile() {

        String pidFile;

        try {

            pidFile = PropertyResourceBundle.getBundle("config").getString("PID_FILE");

        } catch (Exception e) {

            pidFile = "./ZKCrawler-TaskClient.pid.txt";
        }

        try {

            String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0] + "\n";

            File file = new File(pidFile);
            OutputStream os = new FileOutputStream(file);
            os.write(pid.getBytes(), 0, pid.getBytes().length);

            os.flush();
            os.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}


