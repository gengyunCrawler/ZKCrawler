package com.gy.wm.service;

import us.codecraft.webmagic.Page;

/**
 * <类详细说明：使用动态代理来实现解析接口>
 *
 * @Author： Huanghai
 * @Version: 2016-09-13
 **/
public interface PagePaser {

    /**
     * 解析方法，不同的解析有不同的具体实现
     */
    public void parse();
}
