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
        urlPatterns.add(new UrlPattern("http://www.zunyi.gov.cn/sy/\\w+/", "COLUMN_REGEX"));

        urlPatterns.add(new UrlPattern("http://www.zunyi.gov.cn/sy/\\w+/\\d+/(\\w|_)*.html", "CONTENT_LINK_REGEX"));
        //标题
        HtmlField title = new HtmlField();
        List<String> list1 = new ArrayList<>();
        list1.add("//div[@class='middletext']/p[@class='middle_title']");
        title.setXpaths(list1);
        title.setFieldName("title");
        //正文
        //设置为包含标签
//        content.setContainsHtml(true);
        HtmlField content = new HtmlField();
        List<String> list2 = new ArrayList<>();
        list2.add("//div[@class='TRS_Editor']");
        content.setXpaths(list2);
        content.setFieldName("content");

        //设置来源
        HtmlField sourceName = new HtmlField();
        List<String> list4 = new ArrayList<>();
        list4.add("//p[@class='middlebiaoti left'][2]");
        sourceName.setXpaths(list4);
        sourceName.setFieldName("sourceName");

        //设置作者
        HtmlField author = new HtmlField();
        List<String> list5 = new ArrayList<>();
        list5.add("//p[@class='middlebiaoti left'][3]");
        author.setXpaths(list5);
        author.setFieldName("author");

        //设置属性块整块提取
        HtmlField infoBar = new HtmlField();
        List<String> list6 = new ArrayList<>();
        author.setFieldName("infoBar");

        fileds.add(title);
        fileds.add(content);
        fileds.add(sourceName);
        fileds.add(author);
        fileds.add(infoBar);

        ParserConfig config = new ParserConfig();
        config.setFields(fileds);
        config.setId(2227);
        config.setTaskId("http://www.zunyi.gov.cn/");
        config.setUrlPatterns(urlPatterns);
        String s = JSON.toJSONString(config);
        System.out.printf(s);
        ParserDao dao = new ParserDao();
        com.gy.wm.plugins.newsExportPlugin.parse.ParserEntity entity = new ParserEntity();
        entity.setConfig(s);
        entity.setTid("http://www.zunyi.gov.cn/");
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
