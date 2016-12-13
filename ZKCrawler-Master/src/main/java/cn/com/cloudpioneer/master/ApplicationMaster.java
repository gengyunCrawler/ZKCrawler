package cn.com.cloudpioneer.master;

import cn.com.cloudpioneer.master.app.CuratorMaster;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        zkHostPort = ResourceBundle.getBundle("config").getString("ZK_CONNECTION_STRING");
        master = CuratorMaster.initializeMaster(zkHostPort, new ExponentialBackoffRetry(1000, 5), null);
        master.startMaster();
    }

}
