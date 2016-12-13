package com.gy.wm.model;

import com.alibaba.fastjson.JSONArray;
import com.gy.wm.model.config.SeedsConfig;
import com.gy.wm.model.config.SeedsInfo;
import org.junit.Test;

import java.util.List;

/**
 * Created by TianyuanPan on 12/7/2016.
 */
public class TestSeedsConfig {


    @Test
    public void test(){

        /*SeedsInfo url1, url2, url3;
        url1 = new SeedsInfo("http://example01.com/", "TEST-01");
        url2 = new SeedsInfo("http://example02.com/", "TEST-02");
        url3 = new SeedsInfo("http://example03.com/", "TEST-03");

        List<SeedsInfo> seedUrlList = new ArrayList<>();
        seedUrlList.add(url1);
        seedUrlList.add(url2);
        seedUrlList.add(url3);*/

        SeedsConfig urlsConfig = new SeedsConfig();
        //urlsConfig.setSeedsInfoList(seedUrlList);

       // System.out.println(urlsConfig.toJSONString());

        String s = "[{\"url\":\"http://url-01\",\"sourceName\":\"source-name-01\",\"tags\":[{\"tagId\":\"tag-id-01\",\"name\":\"tag-name-01\",\"type\":\"base\",\"addTime\":\"2016-11-17 12:32:00\"},{\"tagId\":\"tag-id-02\",\"name\":\"tag-name-02\",\"type\":\"base\",\"addTime\":\"2016-11-17 12:32:00\"},{\"tagId\":\"tag-id-03\",\"name\":\"tag-name-03\",\"type\":\"base\",\"addTime\":\"2016-11-17 12:32:00\"}],\"categories\":[{\"categoryId\":\"category-id-01\",\"name\":\"name-01\",\"addTime\":\"2016-10-28 15:55:00\"},{\"categoryId\":\"category-id-02\",\"name\":\"name-02\",\"addTime\":\"2016-10-28 15:55:00\"},{\"categoryId\":\"category-id-03\",\"name\":\"name-03\",\"addTime\":\"2016-10-28 15:55:00\"}]},{\"url\":\"http://url-01\",\"sourceName\":\"source-name-01\",\"tags\":[{\"tagId\":\"tag-id-01\",\"name\":\"tag-name-01\",\"type\":\"base\",\"addTime\":\"2016-11-17 12:32:00\"},{\"tagId\":\"tag-id-02\",\"name\":\"tag-name-02\",\"type\":\"base\",\"addTime\":\"2016-11-17 12:32:00\"},{\"tagId\":\"tag-id-03\",\"name\":\"tag-name-03\",\"type\":\"base\",\"addTime\":\"2016-11-17 12:32:00\"}],\"categories\":[{\"categoryId\":\"category-id-01\",\"name\":\"name-01\",\"addTime\":\"2016-10-28 15:55:00\"},{\"categoryId\":\"category-id-02\",\"name\":\"name-02\",\"addTime\":\"2016-10-28 15:55:00\"},{\"categoryId\":\"category-id-03\",\"name\":\"name-03\",\"addTime\":\"2016-10-28 15:55:00\"}]}]";
        List<SeedsInfo> list = JSONArray.parseArray(s, SeedsInfo.class);
        urlsConfig.setSeedsInfoList(list);

        System.out.println("config-1: \n" + urlsConfig.toJSONString());

        urlsConfig = new SeedsConfig(s);

        System.out.println("config-2: \n" + urlsConfig.toJSONString());

    }

}
