package com.gy.wm.service;

import com.alibaba.fastjson.JSON;
import com.gy.wm.dao.ParserDao;
import com.gy.wm.plugins.newsExportPlugin.parse.HtmlField;
import com.gy.wm.plugins.newsExportPlugin.parse.ParserConfig;
import com.gy.wm.plugins.newsExportPlugin.parse.ParserEntity;
import com.gy.wm.plugins.newsExportPlugin.parse.UrlPattern;
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
        urlPatterns.add(new UrlPattern("http://news.qq.com/newsgn/gdxw/gedixinwen.htm", "COLUMN_REGEX"));
        urlPatterns.add(new UrlPattern("http://news.qq.com/newsgn/zhxw/shizhengxinwen.htm", "COLUMN_REGEX"));
        urlPatterns.add(new UrlPattern("http://news.qq.com/newssh/shwx/shehuiwanxiang.htm", "COLUMN_REGEX"));
        urlPatterns.add(new UrlPattern("http://roll.news.qq.com/", "COLUMN_REGEX"));
        urlPatterns.add(new UrlPattern("http://news.qq.com/a/\\d.*/\\d.*.htm", "CONTENT_LINK_REGEX"));


        HtmlField title = new HtmlField();
        title.setFieldName("title");
        List<String> list1 = new ArrayList<>();
        list1.add("//div[@class='qq_article']/div[@class='hd']/h1");

        title.setXpaths(list1);


        HtmlField content = new HtmlField();
        content.setFieldName("content");
        List<String> list2 = new ArrayList<>();
        list2.add("//div[@id='Cnt-Main-Article-QQ']");

        //设置为包含标签
        content.setContainsHtml(true);
        content.setXpaths(list2);



       HtmlField sourceName = new HtmlField();
        List<String> list4 = new ArrayList<>();

        list4.add("//span[@class='a_source']/a");

        sourceName.setXpaths(list4);
        sourceName.setFieldName("sourceName");

        HtmlField author = new HtmlField();
        List<String> list5 = new ArrayList<>();

        list5.add("//span[@class='a_author']");

        author.setXpaths(list5);
        author.setFieldName("author");
        fileds.add(title);
        fileds.add(content);
        fileds.add(sourceName);
        fileds.add(author);

        ParserConfig config = new ParserConfig();
        config.setFileds(fileds);
        config.setId(3434);
        config.setTaskId("task222");
        config.setUrlPatterns(urlPatterns);
        String s = JSON.toJSONString(config);
        System.out.printf(s);
        ParserDao dao = new ParserDao();
        com.gy.wm.plugins.newsExportPlugin.parse.ParserEntity entity = new ParserEntity();
        entity.setConfig(s);
        entity.setId(2);
        entity.setTid("news.qq.com");
        dao.insert(entity);

    }
    @Test
    public void testUrlMatch(){
        String url="http://news.qq.com/a/\\d.*/\\d.*.htm";
        Pattern pattern = Pattern.compile(url);
      Matcher matcher = pattern.matcher("http://news.qq.com/a/20161023/020617.htm");
        System.out.println(matcher.find());
    }
}
