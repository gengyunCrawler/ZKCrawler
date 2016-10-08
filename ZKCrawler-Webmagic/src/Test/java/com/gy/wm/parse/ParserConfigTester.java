package com.gy.wm.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
        urlPatterns.add(new UrlPattern("www.baidu.com/\\w+","colum"));
        urlPatterns.add(new UrlPattern("www.baidu.com/\\d+","article"));
        HtmlField filed1=new HtmlField();
        filed1.setFieldName("title");

        filed1.setOccur("occur");
        filed1.setRegex("regex");

        HtmlField filed2=new HtmlField();
        filed2.setFieldName("title");

        filed2.setOccur("occur");
        filed2.setRegex(null);

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
