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

        urlPatterns.add(new UrlPattern("http://www.gaxq.gov.cn/xwdt/\\w+/index.shtml", "COLUMN_REGEX"));



        //urlPatterns.add(new UrlPattern("http://www.anshun.gov.cn/list.jsp?cItemId=172&itemId=7", "COLUMN_REGEX"));
        //urlPatterns.add(new UrlPattern("http://www.anshun.gov.cn/list.jsp?cItemId=173&itemId=7&page=1", "COLUMN_REGEX"));
        //urlPatterns.add(new UrlPattern("http://www.anshun.gov.cn/list.jsp?cItemId=174&itemId=7&page=1", "COLUMN_REGEX"));



        //urlPatterns.add(new UrlPattern("http://news.qq.com/a/\\d.*/\\d.*.htm", "CONTENT_LINK_REGEX"));
        urlPatterns.add(new UrlPattern("http://www.gaxq.gov.cn/xwdt/\\w+/\\d+.shtml", "CONTENT_LINK_REGEX"));


        /**
         * title field
         */
        HtmlField title = new HtmlField();
        List<String> listTitle = new ArrayList<>();
        title.setFieldName("title");

        listTitle.add("html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr[3]/td/table[1]/tbody/tr[1]/td/h1");

        title.setXpaths(listTitle);


        /**
         * content field
         */
        HtmlField content = new HtmlField();
        List<String> listContent = new ArrayList<>();
        content.setFieldName("content");

        listContent.add("html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr[3]/td/table[1]/tbody/tr[3]/td");

        //设置为包含标签
        content.setContainsHtml(true);
        content.setXpaths(listContent);


        /**
         * source field
         */
        HtmlField sourceName = new HtmlField();
        List<String> listSourceName = new ArrayList<>();
        sourceName.setFieldName("sourceName");


        listSourceName.add("html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr[3]/td/table[1]/tbody/tr[1]/td/p");

        sourceName.setXpaths(listSourceName);


        /**
         * author field
         */
        HtmlField author = new HtmlField();
        List<String> listAuthor = new ArrayList<>();
        author.setFieldName("author");

        listAuthor.add("html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr[3]/td/table[1]/tbody/tr[1]/td/p");

        author.setXpaths(listAuthor);





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
        System.out.printf("THE RESULT:\n" + s);
        ParserDao dao = new ParserDao();
        ParserEntity entity = new ParserEntity();
        entity.setConfig(s);
        entity.setId(2);
        entity.setTid("gaxq.gov.cn");
        dao.insert(entity);

    }
    @Test
    public void testUrlMatch(){
        String url="http://www.gaxq.gov.cn/xwdt/\\w+/\\d+.shtml";
        Pattern pattern = Pattern.compile(url);
      Matcher matcher = pattern.matcher("http://www.gaxq.gov.cn/xwdt/ddaa4522d/12560.shtml");
        System.out.println(matcher.find());
    }
}
