package com.gy.wm.service;

import com.alibaba.fastjson.JSON;
import com.gy.wm.dao.ParserDao;
import com.gy.wm.plugins.newsExportPlugin.parse.HtmlField;
import com.gy.wm.plugins.newsExportPlugin.parse.ParserConfig;
import com.gy.wm.plugins.newsExportPlugin.parse.ParserEntity;
import com.gy.wm.plugins.newsExportPlugin.parse.UrlPattern;
import com.gy.wm.util.RandomStringCreator;
import com.gy.wm.util.RandomUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/10/24.
 */
public class GenerateConfig {

    @Test
    public void testConfig() {
        List<UrlPattern> urlPatterns = new ArrayList<>();
        List<HtmlField> fileds = new ArrayList<>();
        urlPatterns.add(new UrlPattern("http://news.ifeng.com/listpage/\\d+/\\d+/\\d+/\\w+.shtml", "COLUMN_REGEX"));

        urlPatterns.add(new UrlPattern("http://news.ifeng.com/a/\\d+/\\d+_\\d+.shtml", "CONTENT_LINK_REGEX"));




        HtmlField title = new HtmlField();
        title.setFieldName("title");
        List<String> list1 = new ArrayList<>();
        list1.add("//h1[@id='artical_topic']");





        title.setXpaths(list1);


        HtmlField content = new HtmlField();
        content.setFieldName("content");
        List<String> list2 = new ArrayList<>();
        list2.add("//div[@id='main_content']");

        List<String> exPaths=new ArrayList<>();
        exPaths.add("//div[@id='embed_hzh_div']");

        content.setExcludeXpaths(exPaths);


        content.setXpaths(list2);



       HtmlField sourceName = new HtmlField();
        List<String> list4 = new ArrayList<>();
        list4.add("//p[@class='p_time']/span[3]/span[@class='ss03']/a");


        sourceName.setXpaths(list4);
        sourceName.setFieldName("sourceName");


        HtmlField author = new HtmlField();
        List<String> list5 = new ArrayList<>();
        list5.add("//span[@class='a_author']");

        HtmlField inforBar = new HtmlField();
        List<String> list6 = new ArrayList<>();
        list6.add("//div[@class='ep-source cDGray']");
        list6.add("//div[@class='source']");
        inforBar.setFieldName("infoBar");
        inforBar.setXpaths(list6);


        author.setXpaths(list5);
        author.setFieldName("author");
        fileds.add(title);
        fileds.add(content);
        fileds.add(sourceName);
        fileds.add(author);

        ParserConfig config = new ParserConfig();
        config.setFields(fileds);
        config.setId(3434);
        config.setTaskId("task222");
        config.setUrlPatterns(urlPatterns);
        String s = JSON.toJSONString(config);
        System.out.printf(s);
        ParserDao dao = new ParserDao();
        com.gy.wm.plugins.newsExportPlugin.parse.ParserEntity entity = new ParserEntity();
        entity.setConfig(s);
        entity.setTid("http://news.ifeng.com/");
        dao.insert(entity);

    }
    @Test
    public void testUrlMatch(){
        String url="http://[a-z|A-Z]+.sohu.com/\\d+/n\\d+.shtml";
        Pattern pattern = Pattern.compile(url);
      Matcher matcher = pattern.matcher("http://.sohu.com/20161025/n471221370.shtml");
        System.out.println(matcher.find());
    }
}
