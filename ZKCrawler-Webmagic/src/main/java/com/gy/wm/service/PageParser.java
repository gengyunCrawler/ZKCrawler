package com.gy.wm.service;

import com.gy.wm.model.CrawlData;

import java.util.List;

/**
 * <类详细说明：使用反射获得真正解析插件的对象，并执行解析方法>
 *
 * @Author： Huanghai
 * @Version: 2016-09-13
 **/
public interface PageParser {

    /**
     * 解析方法，不同的解析有不同的具体实现
     */
    public List<CrawlData> parse(CrawlData crawlData);
}
