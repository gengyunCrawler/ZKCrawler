import com.gy.wm.Application;
import com.gy.wm.controller.API;
import com.gy.wm.model.TaskEntity;
import com.gy.wm.service.CustomPageProcessor;
import com.gy.wm.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

/**
 * <类详细说明：使用Mockito测试>
 *
 * @Author： Huanghai
 * @Version: 2016-09-13
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public  class MockitoBaseTest {
    @Autowired
    private TaskService taskService;
    @Autowired
    private CustomPageProcessor customPageProcessor;
    @Autowired
    private API api;

    @Test
    public void test() throws Exception{
        customPageProcessor.test();
    }

    @Test
    public void testStartTask() {
        TaskEntity taskEntity =new TaskEntity();
        taskEntity.setDepthCrawl(5);
        taskEntity.setPass(1);
        taskEntity.setId("wholesite20160918135308");
        taskEntity.setTimeStart(new Date(System.currentTimeMillis()));
        taskEntity.setPathSeeds("D:\\testZkCrawler\\seeds.txt");
        taskEntity.setPathProtocolFilter("pending");
        taskEntity.setPathSuffixFilter("pending");
        taskEntity.setType(1);
        taskEntity.setCycleRecrawl(3);
        taskEntity.setPathTemplates("D:\\testZkCrawler\\templates");
        taskEntity.setPathClickRegex("pending");
        taskEntity.setPathConfigs("pending");
        api.startTask(taskEntity);
    }
}
