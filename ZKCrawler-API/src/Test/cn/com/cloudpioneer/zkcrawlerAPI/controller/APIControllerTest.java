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
        String taskId = "0ec153c8c4dae69ae48420426f3750f6";
        String startRow = "0ec153c8c4dae69ae48420426f3750f6|";
        String size = "100";
        System.out.println(apiController.getHbaseData(taskId,startRow,size));
    }
}
