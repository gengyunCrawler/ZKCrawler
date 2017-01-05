package com.gy.wm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.PropertyResourceBundle;

/**
 * <SpringBootApplication,启动Springboot提供API服务>
 *
 * @Author： Hunanghai
 * @Version: 2016-09-08
 **/
@SpringBootApplication
@EnableAsync
public class ApplicationWebmagic {

    private static void createPidFile() {

        String pidFile;

        try {

            pidFile = PropertyResourceBundle.getBundle("config").getString("PID_FILE");

        } catch (Exception e) {

            pidFile = "./ZKCrawler-Webmagic.pid.txt";
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

    public static void main(String[] args) {
        SpringApplication.run(ApplicationWebmagic.class, args);
        createPidFile();
    }

}
