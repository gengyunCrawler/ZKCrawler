package cn.com.cloudpioneer.zkcrawlerAPI.service;

import cn.com.cloudpioneer.zkcrawlerAPI.ApplicationAPI;
import cn.com.cloudpioneer.zkcrawlerAPI.model.CrawlData;
import cn.com.cloudpioneer.zkcrawlerAPI.service.HbaseHandleService;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ResourceBundle;

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
    public void putRecordTest() {
        String taskId =/*handleService.MD5("http://www.gygov.gov.cn/");*/"c5b475b03652d36b5fdfe97022be0240";
        CrawlData crawlData1 = new CrawlData();
        crawlData1.setTid("1234");
        crawlData1.setUrl("http://www.test1.gov.cn/");

        CrawlData crawlData2 = new CrawlData();
        crawlData2.setTid("1234");
        crawlData2.setUrl("http://www.test2.gov.cn/");

        CrawlData crawlData3 = new CrawlData();
        crawlData3.setTid("1234");
        crawlData3.setUrl("http://www.test3.gov.cn/");

        CrawlData crawlData4 = new CrawlData();
        crawlData4.setTid("1234");
        crawlData4.setUrl("http://www.test4.gov.cn/");

        CrawlData crawlData5 = new CrawlData();
        crawlData5.setTid("1234");
        crawlData5.setUrl("http://www.test5.gov.cn/");

        CrawlData crawlData6 = new CrawlData();
        crawlData6.setTid("1234");
        crawlData6.setUrl("http://www.test6.gov.cn/");

        CrawlData crawlData7 = new CrawlData();
        crawlData7.setTid("1234");
        crawlData7.setUrl("http://www.test7.gov.cn/");

        hbaseHandleService.putRecord(crawlData1,taskId);
        hbaseHandleService.putRecord(crawlData2,taskId);
        hbaseHandleService.putRecord(crawlData3,taskId);
        hbaseHandleService.putRecord(crawlData4,taskId);
        hbaseHandleService.putRecord(crawlData5,taskId);
        hbaseHandleService.putRecord(crawlData6,taskId);
    }

    @Test
    public void getHBaseDataTest()  {
        String taskId =/*handleService.MD5("http://www.gygov.gov.cn/");*/"f79ab981a14c819061818ee40f9473a4";
        String result = hbaseHandleService.getHBaseDataTest(taskId,"f79ab981a14c819061818ee40f9473a4|","100");
        System.out.println(result);
    }
}
