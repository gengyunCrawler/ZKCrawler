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

        String s = "[{\"url\":\"http://mil.youth.cn/djbd/\",\"sourceRegion\":\"门户;贵阳\",\"sourceTypeId\":\"X2e1gVvbZB-1481095351540-bYhe4xmviD\",\"sourceName\":\"中青网-军事新闻\",\"tags\":[{\"tagId\":\"tag-id-1\",\"type\":\"base\",\"name\":\"tag-a-1\",\"addTime\":\"2016-11-04 18:03:20\"},{\"tagId\":\"tag-id-2\",\"type\":\"base\",\"name\":\"tag-a-2\",\"addTime\":\"2016-11-04 18:03:20\"}],\"categories\":[{\"categoryId\":\"category-id-1\",\"name\":\"categories-01\",\"addTime\":\"2016-10-28 15:55:00\"},{\"categoryId\":\"category-id-2\",\"name\":\"categories-02\",\"addTime\":\"2016-10-28 15:55:00\"}]},{\"url\":\"http://news.youth.cn/sz/\",\"sourceName\":\"中青网-时政新闻\",\"sourceTypeId\":\"X2e1gVvbZB-1481095351540-bYhe4xmviD\",\"tags\":[{\"tagId\":\"tag-id-1\",\"type\":\"base\",\"name\":\"tag-a-1\",\"addTime\":\"2016-11-04 18:03:20\"},{\"tagId\":\"tag-id-2\",\"type\":\"base\",\"name\":\"tag-a-2\",\"addTime\":\"2016-11-04 18:03:20\"}],\"categories\":[{\"categoryId\":\"category-id-1\",\"name\":\"categories-01\",\"addTime\":\"2016-10-28 15:55:00\"},{\"categoryId\":\"category-id-2\",\"name\":\"categories-02\",\"addTime\":\"2016-10-28 15:55:00\"}]},{\"url\":\"http://news.youth.cn/gn/\",\"sourceName\":\"中青网-国内新闻\",\"sourceTypeId\":\"X2e1gVvbZB-1481095351540-bYhe4xmviD\",\"tags\":[{\"tagId\":\"tag-id-1\",\"type\":\"base\",\"name\":\"tag-a-1\",\"addTime\":\"2016-11-04 18:03:20\"},{\"tagId\":\"tag-id-2\",\"type\":\"base\",\"name\":\"tag-a-2\",\"addTime\":\"2016-11-04 18:03:20\"}],\"categories\":[{\"categoryId\":\"category-id-1\",\"name\":\"categories-01\",\"addTime\":\"2016-10-28 15:55:00\"},{\"categoryId\":\"category-id-2\",\"name\":\"categories-02\",\"addTime\":\"2016-10-28 15:55:00\"}]},{\"url\":\"http://news.youth.cn/gj/\",\"sourceName\":\"中青网-国际新闻\",\"sourceTypeId\":\"X2e1gVvbZB-1481095351540-bYhe4xmviD\",\"tags\":[{\"tagId\":\"tag-id-1\",\"type\":\"base\",\"name\":\"tag-a-1\",\"addTime\":\"2016-11-04 18:03:20\"},{\"tagId\":\"tag-id-2\",\"type\":\"base\",\"name\":\"tag-a-2\",\"addTime\":\"2016-11-04 18:03:20\"}],\"categories\":[{\"categoryId\":\"category-id-1\",\"name\":\"categories-01\",\"addTime\":\"2016-10-28 15:55:00\"},{\"categoryId\":\"category-id-2\",\"name\":\"categories-02\",\"addTime\":\"2016-10-28 15:55:00\"}]},{\"url\":\"http://news.youth.cn/jsxw/\",\"sourceName\":\"中青网-即时新闻\",\"sourceTypeId\":\"X2e1gVvbZB-1481095351540-bYhe4xmviD\",\"tags\":[{\"tagId\":\"tag-id-1\",\"type\":\"base\",\"name\":\"tag-a-1\",\"addTime\":\"2016-11-04 18:03:20\"},{\"tagId\":\"tag-id-2\",\"type\":\"base\",\"name\":\"tag-a-2\",\"addTime\":\"2016-11-04 18:03:20\"}],\"categories\":[{\"categoryId\":\"category-id-1\",\"name\":\"categories-01\",\"addTime\":\"2016-10-28 15:55:00\"},{\"categoryId\":\"category-id-2\",\"name\":\"categories-02\",\"addTime\":\"2016-10-28 15:55:00\"}]}]";
        List<SeedsInfo> list = JSONArray.parseArray(s, SeedsInfo.class);
        urlsConfig.setSeedsInfoList(list);

        System.out.println("config-1: \n" + urlsConfig.toJSONString());

        urlsConfig = new SeedsConfig(s);

        System.out.println("config-2: \n" + urlsConfig.toJSONString());

    }

}
