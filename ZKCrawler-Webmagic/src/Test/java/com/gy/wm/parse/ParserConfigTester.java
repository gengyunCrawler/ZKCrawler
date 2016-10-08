package com.gy.wm.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.jna.platform.win32.OaIdl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class ParserConfigTester
{
    @Test
    public void testConfig(){
        List<UrlPattern> urlPatterns=new ArrayList<>();
        List<HtmlField> fileds=new ArrayList<>();
        urlPatterns.add(new UrlPattern("http://www.qnz.com.cn/news/newslist-0-\\d.shtml","COLUMN_REGEX"));
        urlPatterns.add(new UrlPattern("http://www.qnz.com.cn/news/newsshow-\\d.shtml","CONTENT_LINK_REGEX"));
        HtmlField filed1=new HtmlField();
        filed1.setFieldName("title");
        List<String> list1=new ArrayList<>();
        list1.add("/html/body/table[5]/tbody/tr/td/table/tbody/tr/td/table[3]/tbody/tr/td[1]/table[2]/tbody/tr/td/text()");
        list1.add("//table[@class='bk4']/tbody/tr/td/table[3]/tbody/tr/td");
        filed1.setXpaths(list1);


        HtmlField filed2=new HtmlField();
        filed2.setFieldName("content");
        List<String> list2=new ArrayList<>();
        list2.add("//td[@class='newstext']");
        filed2.setXpaths(list2);

        fileds.add(filed1);
        fileds.add(filed2);
        ParserConfig config=new ParserConfig();
        config.setFileds(fileds);
        config.setId(3434);
        config.setTaskId("task222");
        config.setUrlPatterns(urlPatterns);
        String s=JSON.toJSONString(config);
        System.out.printf(s);

    }

    @Test
    public void test4Conig(){
        String configJson="{\n" +
                "    \"fileds\": [\n" +
                "        {\n" +
                "            \"fieldName\": \"title\",\n" +
                "            \"muti\": false,\n" +
                "            \"occur\": \"occur\",\n" +
                "            \"regex\": \"regex\",\n" +
                "            \"xpath\": \"//div\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"fieldName\": \"title\",\n" +
                "            \"muti\": false,\n" +
                "            \"occur\": \"occur\",\n" +
                "            \"xpath\": \"//div\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"id\": 3434,\n" +
                "    \"taskId\": \"task222\",\n" +
                "    \"urlPatterns\": [\n" +
                "        {\n" +
                "            \"regex\": \"www.baidu.com/\\\\w+\",\n" +
                "            \"type\": \"colum\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"regex\": \"www.baidu.com/\\\\d+\",\n" +
                "            \"type\": \"article\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        System.out.printf(configJson);
        ParserConfig config= JSONObject.parseObject(configJson,ParserConfig.class);
        System.out.printf(JSON.toJSONString(config));
    }
}
