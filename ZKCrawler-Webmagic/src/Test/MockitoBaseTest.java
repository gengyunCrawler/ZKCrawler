import com.gy.wm.Application;
import com.gy.wm.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * <类详细说明：使用Mockito测试>
 *
 * @Author： Huanghai
 * @Version: 2016-09-13
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port:0")
public  class MockitoBaseTest {
    @Autowired
    private WebApplicationContext ctx;
    @Autowired
    private TaskService taskService;
    @Value("${local.server.port}")
    private int port;

    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testGetBean()   {
        taskService = (TaskService) ctx.getBean("taskService");
        System.out.println(taskService.getClass().getName());
    }
}
