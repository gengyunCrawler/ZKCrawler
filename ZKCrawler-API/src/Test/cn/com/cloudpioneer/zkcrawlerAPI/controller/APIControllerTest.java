package cn.com.cloudpioneer.zkcrawlerAPI.controller;

import cn.com.cloudpioneer.zkcrawlerAPI.ApplicationAPI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * <类详细说明:用于API的测试>
 *
 * @Author： Huanghai
 * @Version: 2016-11-02
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationAPI.class)
@WebAppConfiguration
public class APIControllerTest {
    @Autowired
    private APIController apiController;

    @Test
    public void getHbaseDataTest()  {
        String taskId = "c5b475b03652d36b5fdfe97022be0240";
        String startRow = "c5b475b03652d36b5fdfe97022be0240|20161102013959|52a7c";
        String size = "5";
        apiController.getHbaseData(taskId,startRow,size);
    }
}
