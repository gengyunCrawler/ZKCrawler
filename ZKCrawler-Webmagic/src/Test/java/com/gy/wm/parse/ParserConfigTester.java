package com.gy.wm.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gy.wm.dao.ParserDao;
import com.gy.wm.plugins.newsExportPlugin.parse.*;
import com.gy.wm.plugins.newsExportPlugin.parse.HtmlField;
import com.gy.wm.plugins.newsExportPlugin.parse.ParserEntity;
import org.junit.Test;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.selector.Selectors;
import us.codecraft.webmagic.selector.SmartContentSelector;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/30.
 */
public class ParserConfigTester {


    @Test
    public void testSmartContent() {
        String content = "<div class=\"hhg\">\n" +
                "  来源：黔南热线-黔南日报 \n" +
                " <span class=\"NewsAuthor\">作者：徐朦</span> \n" +
                " <span class=\"NewsDate\">2016-10-11 9:55:50</span>　 \n" +
                " <br> 投稿邮箱：qnrx999@163.com 新闻热线: 0854-8221848 \n" +
                " <span class=\"auth\"><script language=\"javascript\" type=\"text/javascript\">\n" +
                "function ContentSize(size)\n" +
                "{document.getElementById('MyContent').style.fontSize=size+'px';}\n" +
                "</script> 【字体：<a href=\"\">大</a> <a href=\"\">中</a> <a href=\"\">小</a>】 </span> 　 \n" +
                "</div>";
        SmartContentSelector selector = Selectors.smartContent();
        Html html = new Html(content);
        //  HtmlPage htmlPag=new HtmlPage()
        System.out.println(html.xpath("//div").smartContent());


    }

    @Test
    public void testUrl() {
        String url = "http://www.qnz.com.cn/news/newslist-0-12.shtml";
        String[] arr = url.split("/");
        int i = 0;
        for (String a : arr) {
            System.out.println(i + "----" + a);
            i++;
        }
        System.out.println(arr[0] + "/" + arr[2]);

    }

    @Test
    public void testImgSufix() {
        String url = "http://www.qnz.com.cn/news/newslist-0-12.shtml";
        // for (){}
        Pattern pattern = Pattern.compile("(\\w+.*://\\w+.*)/(\\w+.*)?");
        Matcher matcher = pattern.matcher(url);
        while (matcher.find()) {
            System.out.println("0:" + matcher.group(0));
            System.out.println("1:" + matcher.group(1));
            System.out.println("2:" + matcher.group(2));
        }
        String s = "11111<img class src=\"/dap/dxeditor.output.f?pkid=01120316032104947034\" width=62 height=62>测试<img src=\"/192.168.1.4:8080/dap/dxeditor.output.f?pkid=01120316032104947034\">222222<img src=\"/dd\">";
        Pattern pattern1 = Pattern.compile("<img.*src=\"/(.*)\".*>??");
        Matcher matcher1 = pattern1.matcher(s);
        while (matcher1.find()) {
            String src = "http://www.baidu.com";
            System.out.println(matcher1.group());
//            System.out.println(matcher1.group(0));
//            System.out.println(matcher1.group(1));
//          String content=  matcher1.group().replace("src=\"","src=\""+src);
//            System.out.println("content:"+content);

        }

    }

    @Test
    public void testPartSelectable() {
        InputStream is = this.getClass().getResourceAsStream("/selectableTest.html");
        InputStreamReader reader = new InputStreamReader(is);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(is);


    }

    private String byXpaths(Html html, List<String> xpaths) {
        for (String xpath : xpaths) {
            Selectable selectable = html.xpath(xpath);
            if (selectable != null) {
                return selectable.toString();
            }
        }
        return null;
    }

    @Test
    public void testConfig() {
        List<com.gy.wm.plugins.newsExportPlugin.parse.UrlPattern> urlPatterns = new ArrayList<>();
        List<com.gy.wm.plugins.newsExportPlugin.parse.HtmlField> fileds = new ArrayList<>();
        urlPatterns.add(new com.gy.wm.plugins.newsExportPlugin.parse.UrlPattern("http://www.qnz.com.cn/news/newslist-0-\\d*.shtml", "COLUMN_REGEX"));
        urlPatterns.add(new com.gy.wm.plugins.newsExportPlugin.parse.UrlPattern("http://www.qnz.com.cn/news/newsshow-\\d*.shtml", "CONTENT_LINK_REGEX"));
        com.gy.wm.plugins.newsExportPlugin.parse.HtmlField filed1 = new com.gy.wm.plugins.newsExportPlugin.parse.HtmlField();
        filed1.setFieldName("title");
        List<String> list1 = new ArrayList<>();
        list1.add("//div[@class='Title_h1']/h1");
        list1.add("/html/body/table[5]/tbody/tr/td/table/tbody/tr/td/table[3]/tbody/tr/td[1]/table[2]/tbody/tr/td");
        list1.add("//table[@class='bk4']/tbody/tr/td/table[3]/tbody/tr/td");

        filed1.setXpaths(list1);


        com.gy.wm.plugins.newsExportPlugin.parse.HtmlField filed2 = new HtmlField();
        filed2.setFieldName("content");
        List<String> list2 = new ArrayList<>();
        list2.add("//div[@id='content_main']");
        list2.add("//td[@class='newstext']");
        filed2.setXpaths(list2);

        com.gy.wm.plugins.newsExportPlugin.parse.HtmlField field3 = new com.gy.wm.plugins.newsExportPlugin.parse.HtmlField();
        List<String> list = new ArrayList<>();

        list.add("/html/body/table[5]/tbody/tr/td/table/tbody/tr/td/table[3]/tbody/tr/td[1]/table[3]/tbody/tr[1]/td");
        list.add("//div[@class='hhg']");
        field3.setXpaths(list);
        field3.setFieldName("source");


        com.gy.wm.plugins.newsExportPlugin.parse.HtmlField field4 = new com.gy.wm.plugins.newsExportPlugin.parse.HtmlField();
        List<String> list4 = new ArrayList<>();

        list4.add("//div[@class='hhg']/text()");

        field4.setXpaths(list4);
        field4.setFieldName("sourceName");

        com.gy.wm.plugins.newsExportPlugin.parse.HtmlField field5 = new com.gy.wm.plugins.newsExportPlugin.parse.HtmlField();
        List<String> list5 = new ArrayList<>();

        list5.add("//div[@class='hhg']/span[@class='NewsAuthor']/text()");

        field5.setXpaths(list5);
        field5.setFieldName("author");
        fileds.add(filed1);
        fileds.add(filed2);
        fileds.add(field3);
        fileds.add(field4);
        fileds.add(field5);

        com.gy.wm.plugins.newsExportPlugin.parse.ParserConfig config = new com.gy.wm.plugins.newsExportPlugin.parse.ParserConfig();
        config.setFields(fileds);
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
    public void test4Conig() {
        String configJson = "{\n" +
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
        ParserConfig config = JSONObject.parseObject(configJson, ParserConfig.class);
        System.out.printf(JSON.toJSONString(config));
    }

    @Test
    public void testMapper() {
        ParserDao dao = new ParserDao();
        dao.find("");
    }

    @Test
    public void testRegx(){
        String s = "<span class=\"time-source xh-highlight\" id=\"navtimeSource\">2016年10月26日01:39\t\t<span>\n" +
                "<span data-sudaclick=\"media_name\"><a href=\"http://epaper.jinghua.cn/html/2016-10/26/content_342030.htm\" target=\"_blank\" rel=\"nofollow\">京华时报</a></span></span>\n" +
                "\t</span>";
        Html html = new Html(s);
       String ss= html.xpath("//span[@id='navtimeSource']/text()//*/text()").toString();
        System.out.println(ss);
    }
}
