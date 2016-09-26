package com.gy.wm.plugins.newsExportPlugin;

import com.gy.wm.model.CrawlData;
import com.gy.wm.service.PageParser;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.util.List;

/**
 * <类详细说明：新闻导出解析插件>
 *
 * @Author：Huanghai
 * @Version: 2016-09-26
 **/
public class NewsExport implements PageParser{
    @Override
    public List<CrawlData> parse(CrawlData crawlData) {
        return null;
    }
}

