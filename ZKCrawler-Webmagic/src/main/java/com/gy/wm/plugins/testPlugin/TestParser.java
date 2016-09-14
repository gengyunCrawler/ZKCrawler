package com.gy.wm.plugins.testPlugin;

import com.gy.wm.service.PagePaser;

/**
 * <类详细说明>
 *
 * @Author： Huanghai
 * @Version: 2016-09-13
 **/
public class TestParser implements PagePaser{
    @Override
    public void parse() {
        System.out.println("before warnnig");
    }
}
