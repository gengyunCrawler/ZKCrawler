package com.gy.wm.mapper;

import com.gy.wm.ApplicationWebmagic;
import com.gy.wm.dao.TaskEntityDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * <类详细说明>
 *
 * @Author： HuangHai
 * @Version: 2017-01-13
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationWebmagic.class)
@WebAppConfiguration
public class TaskEntityMapperTest {
    @Autowired
    private TaskEntityDao taskEntityDao;

    @Test
    public void testFindById()  {
        String id ="21236056e8a995b6f95c675a7d7aa44f";
        System.out.println(taskEntityDao.findById(id).getDownloader());
    }
}



