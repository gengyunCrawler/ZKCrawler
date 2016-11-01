package cn.com.cloudpioneer.zkcrawlerAPI;

import cn.com.cloudpioneer.zkcrawlerAPI.service.HbaseHandleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * <类详细说明:HBase操作单元测试>
 *
 * @Author： Huanghai
 * @Version: 2016-11-01
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationAPI.class)
@WebAppConfiguration
public class HbaseHandleTest {
    @Autowired
    private HbaseHandleService hbaseHandleService;

    @Test
    public void createTableTest()   throws Exception{
        hbaseHandleService.createTable();
    }

    @Test
    public void utilTest()  {
//        hbaseHandleService.generateRowKey("http://www.gygov.gov.cn");
        System.out.println("ok");
    }
}
