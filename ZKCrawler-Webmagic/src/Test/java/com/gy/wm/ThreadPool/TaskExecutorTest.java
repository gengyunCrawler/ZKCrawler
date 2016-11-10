package com.gy.wm.ThreadPool;

import com.gy.wm.ApplicationWebmagic;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.ExecutorService;

/**
 * <类详细说明: TaskExecutor的实现>
 *
 * @Author： Huanghai
 * @Version: 2016-11-04
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationWebmagic.class)
@WebAppConfiguration
public class TaskExecutorTest {
    private ExecutorService executorService;

}
