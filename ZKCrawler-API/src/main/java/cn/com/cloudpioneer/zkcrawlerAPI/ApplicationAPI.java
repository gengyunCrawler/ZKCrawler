package cn.com.cloudpioneer.zkcrawlerAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.PropertyResourceBundle;

/**
 * Created by TianyuanPan on 6/29/16.
 */

@SpringBootApplication
public class ApplicationAPI {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationAPI.class, args);
        createPidFile();
    }

    private static void createPidFile() {

        String pidFile;

        try {

            pidFile = PropertyResourceBundle.getBundle("config").getString("PID_FILE");

        } catch (Exception e) {

            pidFile = "./ZKCrawler-dataApi.pid.txt";
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


