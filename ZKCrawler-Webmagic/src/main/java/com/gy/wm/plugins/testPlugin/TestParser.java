package com.gy.wm.plugins.testPlugin;

import com.gy.wm.model.CrawlData;
import com.gy.wm.service.PageParser;
import java.util.ArrayList;
import java.util.List;

/**
 * <类详细说明：解析插件测试类>
 *
 * @Author： Huanghai
 * @Version: 2016-09-13
 **/
public class TestParser implements PageParser {
    @Override
    public List<CrawlData> parse(CrawlData crawlData) {
        List<CrawlData> crawlDatas = new ArrayList<CrawlData>();
        CrawlData cd = new CrawlData();
        cd.setTid("007");
        crawlDatas.add(cd);
        return crawlDatas;
    }
}
