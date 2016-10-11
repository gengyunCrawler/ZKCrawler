package com.gy.wm.plugins.newsExportPlugin.parse;

import com.alibaba.fastjson.JSONObject;
import com.gy.wm.dao.ParserDao;
import com.gy.wm.model.CrawlData;
import com.gy.wm.service.PageParser;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tijun on 2016/10/8.
 * @author TijunWang
 */
public class CommonParser implements PageParser {
    private ParserConfig config = null;
    private ParserDao parserDao = new ParserDao();
    private List<String> contentLinkRegexs = new ArrayList<>();
    private List<String> columnRegexs = new ArrayList<>();
    private  Pattern pattern=Pattern.compile("(\\w+.*://\\w+.*)/(\\w+.*)");

    @Override
    public List<CrawlData> parse(CrawlData crawlData) {
        System.out.println(crawlData.toString());
        List<CrawlData> crawlDatas = new ArrayList<>();
        //obtain crawler config from MySQL ,one task one config(json),so for a task ,it just accesses database once
        if (config == null) {
            ParserEntity entity = parserDao.find(crawlData.getTid());
            if (entity == null) {
                try {
                    throw new Exception("ERROR:couldn't find config by tid='" + crawlData.getTid() + "'");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            config = JSONObject.parseObject(entity.getConfig(), ParserConfig.class);
            //grouped urlPatterns
            for (UrlPattern pattern : config.getUrlPatterns()) {
                if (pattern.getType().equals(ParserConfig.HTML_LINK_REGEX)) {
                    contentLinkRegexs.add(pattern.getRegex());
                }
                if (pattern.getType().equals(ParserConfig.URL_PARTTERN_TYPE_COLUMN_REGEX)) {
                    columnRegexs.add(pattern.getRegex());
                }
            }

        }
        //discovery links
        //judge current page class(links page or content page)
        if (isColumnHtml(crawlData.getUrl())) {
            List<String> urls = new Html(crawlData.getHtml()).xpath("//a/@href").all();
            for (String url : urls) {
                if (isContentHtml(url)) {
                    System.out.printf("url---:" + url);
                    CrawlData data = new CrawlData();
                    data.setUrl(url);
                    data.setTid(crawlData.getTid());
                    data.setFromUrl(crawlData.getUrl());
                    data.setRootUrl(crawlData.getUrl());
                    data.setFetched(false);
                    crawlDatas.add(data);
                }
            }
        }
        //parsing data from html
        if (isContentHtml(crawlData.getUrl())) {
            crawlDatas.add(parseData(new Html(crawlData.getHtml()), crawlData, config.getFileds()));
        }

        return crawlDatas;
    }

    /**
     * Parsing data from html and result in crawlerData,this is just version 1.0,but at version 2.0
     * we needn't to know which field is,we just put all field in a mup,and then stored in mysql,hbase and other systems
     *
     * @param html
     * @param crawlData
     * @param htmlFields
     * @return CrawlerData
     * @version 1.0
     */
    private CrawlData parseData(Html html, CrawlData crawlData, List<HtmlField> htmlFields) {


        String title=null;
        //包含html格式的文章段落
        String contentHtml=null;
        //来源和作者字段
        String author=null;

        String sourceName=null;
        String source=null;


        for (HtmlField htmlField:htmlFields){
            if (htmlField.getFieldName().equals("title")){
                title=byXpaths(html,htmlField.getXpaths());
            }

            if (htmlField.getFieldName().equals("content")){
                contentHtml=byXpaths(html,htmlField.getXpaths());
                Html newContentHtml=new Html("<html>"+contentHtml+"</html>");
                List<String> imgSrcs=newContentHtml.xpath("//img/@src").all();
                String domain="";
               String []arr= crawlData.getRootUrl().split("/");
                domain=arr[0]+"//"+arr[2];
               contentHtml= imgUrlPrefix(contentHtml,imgSrcs,domain);


            }if (htmlField.getFieldName().equals("source")){
                source =byXpaths(html,htmlField.getXpaths());
                if (source!=null){
                    String [] arr=source.split(" ");
                    for (String content:arr){
                        if (content.contains("作者")){
                            author=content.split("：")[1];
                        }
                        if (content.contains("来源")){
                            sourceName=content.split("：")[1];
                        }
                    }
                }
            }
        }

        crawlData.setTid(crawlData.getTid());
        crawlData.setTitle(title);
        crawlData.setHtml(html.toString());
        crawlData.setDepthfromSeed(crawlData.getDepthfromSeed() + 1);
        crawlData.setFetched(true);
        crawlData.setCrawlTime(new Date());
        crawlData.setText(contentHtml);
        crawlData.setAuthor(author);
        crawlData.setSourceName(sourceName);

        return crawlData;
    }

    /**
     * judge the url which is the final content html
     * @param url
     * @return
     */
    private boolean isContentHtml(String url) {
        for (String urlRegex : contentLinkRegexs) {
            if (url.matches(urlRegex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * judge the url which is a column page url
     *
     * @param url
     * @return
     */
    private boolean isColumnHtml(String url) {
        for (String urlRegex : columnRegexs) {
            if (url.matches(urlRegex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * get data from html by xpathes
     *
     * @param html
     * @param xpaths
     * @return
     */
    private String byXpaths(Html html, List<String> xpaths) {
        for (String xpath : xpaths) {
            Selectable selectable = html.xpath(xpath);
            System.out.println("SELECTABLE:"+selectable.toString());
            if (selectable != null) {
                if (selectable.toString()!=null){
                    return selectable.toString();
                }
            }
        }
        return null;
    }

    private String imgUrlPrefix(String contentHtml,List<String> imgSrcs,String domain){
        for (String url:imgSrcs){
            if (url.startsWith("/")){
              contentHtml= contentHtml.replace(url,domain+url);
            }
            }
        return contentHtml;

    }
}