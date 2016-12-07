package com.gy.wm.model;

import com.alibaba.fastjson.JSONArray;
import com.gy.wm.model.config.SeedUrl;
import com.gy.wm.model.config.SeedUrlsConfig;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 12/7/2016.
 */
public class TestSeedUrlsConfig {


    @Test
    public void test(){

        SeedUrl url1, url2, url3;
        url1 = new SeedUrl("http://example01.com/", "TEST-01");
        url2 = new SeedUrl("http://example02.com/", "TEST-02");
        url3 = new SeedUrl("http://example03.com/", "TEST-03");

        List<SeedUrl> seedUrlList = new ArrayList<>();
        seedUrlList.add(url1);
        seedUrlList.add(url2);
        seedUrlList.add(url3);

        SeedUrlsConfig urlsConfig = new SeedUrlsConfig();
        urlsConfig.setSeedUrls(seedUrlList);

        System.out.println(urlsConfig.toJSONString());

        String s = "[{\"sourceName\":\"TEST-01\",\"url\":\"http://example01.com/\"},{\"sourceName\":\"TEST-02\",\"url\":\"http://example02.com/\"},{\"sourceName\":\"TEST-03\",\"url\":\"http://example03.com/\"}]";
        List<SeedUrl> list = JSONArray.parseArray(s, SeedUrl.class);
        urlsConfig.setSeedUrls(list);

        System.out.println(urlsConfig.toJSONString());

    }

}
