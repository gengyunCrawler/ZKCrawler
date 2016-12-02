package com.gy.wm.plugins.newsExportPlugin.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.gy.wm.dao.ParserDao;
import com.gy.wm.model.CrawlData;
import com.gy.wm.service.PageParser;
import com.gy.wm.util.AlphabeticRandom;
import com.gy.wm.util.JedisPoolUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.selector.SmartContentSelector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private PropertyResourceBundle properties = (PropertyResourceBundle) PropertyResourceBundle.getBundle("config");



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
            String domain="";
            String []arr= crawlData.getRootUrl().split("/");
            domain=arr[0]+"//"+arr[2];
            for (String url : urls) {
                if (isContentHtml(url)) {
                    CrawlData data = new CrawlData();
                    data.setUrl(url);
                    data.setTid(crawlData.getTid());
                    data.setFromUrl(crawlData.getUrl());
                    data.setRootUrl(crawlData.getUrl());
                    data.setTag(crawlData.getTag());
                    data.setFetched(false);
                    data.setTag(crawlData.getTag());
                    crawlDatas.add(data);
                }
            }
        }
        //parsing data from html
        if (isContentHtml(crawlData.getUrl())) {
            crawlDatas.add(parseData(new Html(crawlData.getHtml()), crawlData, config.getFields()));
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

        String docId = generateRowKey(crawlData.getTid());
        //设置文章
        crawlData.setDocId(docId);
        crawlData.setTid(crawlData.getTid());

        crawlData.setHtml(html.toString());
        crawlData.setDepthfromSeed(crawlData.getDepthfromSeed() + 1);
        crawlData.setFetched(true);
        crawlData.setCrawlTime(new Date());

        //put all fields into mup
        Map<String,Object> fieldMap=new HashMap<>();
        for (HtmlField htmlField:htmlFields){
            String fieldValue=byXpaths(html,htmlField.getXpaths());

            if (fieldValue!=null){

                if (fieldValue.contains("<img")){
                    Html newContentHtml=new Html(fieldValue);

                    List<String> imgSrcs=newContentHtml.xpath("//img/@src").all();
                    String domain="";
                    String []arr= crawlData.getRootUrl().split("/");
                    domain=arr[0]+"//"+arr[2];
                    int end = crawlData.getUrl().lastIndexOf("/");
                    String preUrl = crawlData.getUrl().substring(0,end+1);
                    String aliOSSUrl = properties.getString("ALI_OSS_URL");
                    //fix imag url
                    fieldValue = imgUrlPrefix(fieldValue,domain,preUrl,imgSrcs);
                    fieldValue =  imgDealWithRedis(aliOSSUrl,fieldValue,crawlData);
                }

                if (htmlField.isContainsHtml()==false ){
                    //for the export of duocai ,set boolean true for "isContentHtml" attribute
                    if(htmlField.getFieldName().equals("content"))  {
                     //   fieldValue=byXpaths(html,htmlField.getXpaths());
                    }else   {
                        Html fieldHtml = new Html(fieldValue);
                        List<String> fieldValues = fieldHtml.xpath("//*/text()").all();
                        StringBuffer buffer = new StringBuffer();
                        for (String value:fieldValues){
                            buffer.append(value);
                        }
                        fieldValue=buffer.toString();
                    }
                }
                //remove elements that is needn't
                List<String> excludesXpaths=htmlField.getExcludeXpaths();
                if (excludesXpaths!=null&&excludesXpaths.size()>0){
                    fieldValue=byExcludesXpaths(fieldValue,excludesXpaths);

                }
            }

            fieldMap.put(htmlField.getFieldName(),fieldValue);
            Map<String,String> tags = new HashMap<>();
            tags.put("column",crawlData.getTag());
            fieldMap.put("tag",tags);

            if (htmlField.getFieldName().equals("title"))
                crawlData.setTitle(fieldValue);
            else if (htmlField.getFieldName().equals("content"))
                crawlData.setText(fieldValue);
            else if (htmlField.getFieldName().equals("author"))
                crawlData.setAuthor(fieldValue);
            else if (htmlField.getFieldName().equals("sourceName"))
                crawlData.setSourceName(fieldValue);
        }

        crawlData.setCrawlerdata(fieldMap);

        //对html中img标签的处理，下载图片

        return crawlData;
    }

    /**
     * judge the url which is the final content html
     * @param url
     * @return
     */
    private boolean isContentHtml(String url) {

        for (String urlRegex : contentLinkRegexs) {
            Pattern pattern = Pattern.compile(urlRegex);
            if (pattern.matcher(url).find()) {
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
            Pattern pattern = Pattern.compile(urlRegex);
            if (pattern.matcher(url).matches()) {
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
                    List<String> contents=selectable.all();
                    StringBuffer buffer=new StringBuffer();
                    for (String content:contents){
                        buffer.append(content);
                    }
                    return buffer.toString();
                }
            }
        }
        return null;
    }

    /**
     * remove elements that matches xpathes
     * @param contentHtml
     * @param xpaths
     * @return
     */
    private String byExcludesXpaths(String contentHtml, List<String> xpaths) {
        String regex=">\\s*<";
        Html html = new Html(contentHtml);
        for (String xpath : xpaths) {
            Selectable selectable = html.xpath(xpath);
            if (selectable != null) {
                if (selectable.toString() != null){
                    List<String> contents=selectable.all();
                        for (String content:contents){
                            content=content.replace("\n","").replace("\t","").replaceAll(regex,"><");
                            contentHtml=html.xpath("/html/body/*").toString().replaceAll(regex,"><").replace(content,"");
                           }
                        }
                }
            }

        return contentHtml;
    }

    public String imgDealWithRedis(String ossUrl,String content,CrawlData crawlData){
        Html html = new Html(content);
        List<String> imgSrcs=html.xpath("//img/@src").all();
        imgConvert(crawlData.getTid(), crawlData.getUrl(), imgSrcs);
        for (String url:imgSrcs){
           content = replaceWithOSS(content,url,ossUrl);
        }

       return  content;
    }

    /**
     *
     * @param content
     * @param domain
     * @param preUrl
     * @param srcs
     * @return
     */
    private String imgUrlPrefix(String content,String domain,String preUrl,List<String> srcs){
        for (String oldUrl : srcs){
            String fixedUrl = singleImgUrlPrefix(domain,preUrl,oldUrl);
             content = content.replace(oldUrl,fixedUrl);

        }
        return  content;
    }
    /**
     * deal with 3 condition with url missing domain
     * @param domain
     * @param url
     * @Param preUrl
     * @return
     */
    private String singleImgUrlPrefix(String domain,String preUrl,String url){
        //1. url start with '/'
        //2. url start with '../../........'
        Preconditions.checkNotNull(domain,"preffix couldn't be null");
        Preconditions.checkNotNull(url,"url couldn't be null");
        if (url.startsWith("http")){
            return url;
        }
        if (url.startsWith("/")){
            url = domain + url;
        }else  if (url.startsWith("../")){
          url =domain+"/"+url.replace("../","");
        }else {//not start with http
            url = preUrl+url;
        }
        //3. url start with '' nothing but not with http
        return  url;
    }


    /**
     * replace the orgUrl with ossUrl for img lable
     * @param content
     * @param orgUrl
     * @param ossUrl
     * @return
     */
    private String replaceWithOSS(String content,String orgUrl,String ossUrl){
        String domain="";
        String []arr= orgUrl.split("/");
        if (arr.length>2){
            domain=arr[0]+"//"+arr[2];
           String url = ossUrl+"/"+ orgUrl.replace(domain,"");
            content.replace(orgUrl,url);
        }

       return content;
    }

    public String generateRowKey(String taskId)    {
        return taskId+"|"+new Date().getTime()+"|"+ AlphabeticRandom.randomStringOfLength(5);
    }

    /**
     转换img的src属性，如果是绝对地址的话，直接使用
     *否则，通过规则把相对地址转换成绝对地址
     */
    public void imgConvert(String taskId, String url, List<String> srcList)   {
        JedisPoolUtils jedisPoolUtils = null;
        try {
            jedisPoolUtils = new JedisPoolUtils();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JedisPool pool = jedisPoolUtils.getJedisPool();
        Jedis jedis = pool.getResource();
        Jedis imgJedis = pool.getResource();
        imgJedis.select(1);

        //redis中存储被替换的img的src地址的list srcurls

      //  List<String> srcList = new ArrayList<>();
        String srcurls = JSON.toJSONString(srcList);

        imgJedis.hset("ImgSrcOf:"+taskId, url, srcurls);
    }
}