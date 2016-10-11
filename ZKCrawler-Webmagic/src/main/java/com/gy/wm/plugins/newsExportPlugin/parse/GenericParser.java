package com.gy.wm.plugins.newsExportPlugin.parse;

import com.alibaba.fastjson.JSONObject;
import com.gy.wm.dao.ParserDao;
import com.gy.wm.model.CrawlData;
import com.gy.wm.service.PageParser;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.selector.SmartContentSelector;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tijun on 2016/10/8.
 * @author TijunWang
 * @version 1.0
 */
public class GenericParser implements PageParser {
    private ParserConfig config = null;
    private ParserDao parserDao = new ParserDao();
    private List<String> contentLinkRegexs = new ArrayList<>();
    private List<String> columnRegexs = new ArrayList<>();
    private  Pattern pattern=Pattern.compile("(\\w+.*://\\w+.*)/(\\w+.*)");

    @Override
    public List<CrawlData> parse(CrawlData crawlData) {
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
     * Parsing data from html and result in crawlerData,
     * we needn't to know which field is,we just put all field in a mup,and then stored in mysql,hbase and other systems
     *
     * @param html
     * @param crawlData
     * @param htmlFields
     * @return CrawlerData
     * @version 1.0
     */
    private CrawlData parseData(Html html, CrawlData crawlData, List<HtmlField> htmlFields) {



        crawlData.setTid(crawlData.getTid());

        crawlData.setHtml(html.toString());
        crawlData.setDepthfromSeed(crawlData.getDepthfromSeed() + 1);
        crawlData.setFetched(true);
        crawlData.setCrawlTime(new Date());

        //put all fields into mup
        Map<String,String> fieldMap=new HashMap<>();
        for (HtmlField htmlField:htmlFields){
            String fieldValue=byXpaths(html,htmlField.getXpaths());
            if (fieldValue!=null){
                if (fieldValue.contains("<img")){
                    Html newContentHtml=new Html(fieldValue);

                    List<String> imgSrcs=newContentHtml.xpath("//img/@src").all();
                    String domain="";
                    String []arr= crawlData.getRootUrl().split("/");
                    domain=arr[0]+"//"+arr[2];
                    fieldValue= imgUrlPrefix(fieldValue,imgSrcs,domain);
                }

            }

            fieldMap.put(htmlField.getFieldName(),fieldValue);
        }

        crawlData.setCrawlerdata(fieldMap);

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
            if (selectable != null) {
                if (selectable.toString()!=null){
                    return selectable.toString();
                }
            }
        }
        return null;
    }

    /**
     * fix img src--->url missing domain
     * @param contentHtml
     * @param imgSrcs
     * @param domain
     * @return
     */
    private String imgUrlPrefix(String contentHtml,List<String> imgSrcs,String domain){
        for (String url:imgSrcs){
            if (url.startsWith("/")){
                contentHtml= contentHtml.replace(url,domain+url);
            }
        }
        return contentHtml;

    }
}