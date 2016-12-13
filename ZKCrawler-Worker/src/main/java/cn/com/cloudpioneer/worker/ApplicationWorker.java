package cn.com.cloudpioneer.worker;

import cn.com.cloudpioneer.worker.app.Worker;
import org.apache.curator.retry.ExponentialBackoffRetry;
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
public class ApplicationWorker {

    /**
     * zookeeper 的链接字符串，从配置文件中获得。
     */
    private static String zkHostPort;


    /**
     * 初始化 worker。
     */
    private static Worker worker;

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

        createPidFile();

        zkHostPort = ResourceBundle.getBundle("config").getString("ZK_CONNECTION_STRING");

        worker = Worker.initializeWorker(zkHostPort, new ExponentialBackoffRetry(1000, 5));

        /**
         * 启动 worker。
         */
        worker.workerStart();

    }



    private static void createPidFile() {

        String pidFile;

        try {

            pidFile = PropertyResourceBundle.getBundle("config").getString("PID_FILE");

        } catch (Exception e) {

            pidFile = "./ZKCrawler-Maser.pid.txt";
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


