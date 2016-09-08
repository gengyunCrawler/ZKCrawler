package cn.com.cloudpioneer.worker;

import cn.com.cloudpioneer.worker.app.Worker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by TianyuanPan on 6/29/16.
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
        Worker.worker_main(args);

    }

}


