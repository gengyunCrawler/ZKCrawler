package cn.com.cloudpioneer.master;

import cn.com.cloudpioneer.master.app.CuratorMaster;
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
 * Created by Tijun on 2016/9/8.
 */
@SpringBootApplication
public class ApplicationMaster {
    /**
     * zookeeper 的链接字符串，从配置文件中获得。
     */
    private static String zkHostPort;

    /**
     * master 角色实体
     */
    private static CuratorMaster master;


    /**
     * 入口函数
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        SpringApplication.run(ApplicationMaster.class, args);
        createPidFile();
        zkHostPort = ResourceBundle.getBundle("config").getString("ZK_CONNECTION_STRING");
        master = CuratorMaster.initializeMaster(zkHostPort, new ExponentialBackoffRetry(1000, 5), null);
        master.startMaster();
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
