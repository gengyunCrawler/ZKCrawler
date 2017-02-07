package com.gy.wm.plugins.newsExportPlugin.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.gy.wm.dao.ParserDao;
import com.gy.wm.model.CrawlData;
import com.gy.wm.service.PageParser;
import com.gy.wm.util.AlphabeticRandom;
import com.gy.wm.util.JedisPoolUtils;
import com.gy.wm.util.MD5;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by tijun on 2016/10/8.
 *
 * @author TijunWang
 * @version 1.0
 */
public class GenericParser implements PageParser {
    private ParserConfig config = null;
    private final ParserDao parserDao = new ParserDao();
    private final List<String> contentLinkRegexs = new ArrayList<>();
    private final List<String> columnRegexs = new ArrayList<>();
    private final List<String> articleBlockXpaths = new ArrayList<>();
    private final String ALI_OSS_URL = PropertyResourceBundle.getBundle("config").getString("ALI_OSS_URL");

    @Override
    public List<CrawlData> parse(CrawlData crawlData) {
        List<CrawlData> crawlDataList = new ArrayList<>();
        //obtain crawler config from MySQL,one task one config(json),so for a task ,it just accesses database once
        ParserEntity entity = null;
        if (config == null) {
            entity = parserDao.find(crawlData.getTid());
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
            //是栏目页，如果可以通过Xpath取得所需文章链接块（或称列表），采取此法
            List<String> urls;
            if(articleBlockXpaths.size() !=0)   {
                urls = getArticleBockByXpath(crawlData);
                if(urls.size()==0) {
                    urls = new Html(crawlData.getHtml()).xpath("//a/@href").all();
                }
            }else   {
                urls = new Html(crawlData.getHtml()).xpath("//a/@href").all();
            }
            String domain = "";
            String[] arr = crawlData.getRootUrl().split("/");
            domain = arr[0] + "//" + arr[2];
            for (String url : urls) {
                if (isContentHtml(url)) {
                    CrawlData data = new CrawlData();

                    data.setUrl(url);
                    data.setTid(crawlData.getTid());
                    data.setFromUrl(crawlData.getUrl());
                    data.setRootUrl(crawlData.getRootUrl());
                    data.setSourceTypeId(crawlData.getSourceTypeId());
                    data.setTags(crawlData.getTags());
                    data.setCategories(crawlData.getCategories());
                    data.setSourceRegion(crawlData.getSourceRegion());
                    data.setTag(crawlData.getTag());
                    data.setFetched(false);

                    crawlDataList.add(data);
                }
            }
        }
        //parsing data from html
        if (isContentHtml(crawlData.getUrl())) {
            crawlDataList.add(parseData(new Html(crawlData.getHtml()), crawlData, entity, config.getFields()));
        }

        return crawlDataList;
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
    private CrawlData parseData(Html html, CrawlData crawlData, ParserEntity entity, List<HtmlField> htmlFields) {


        crawlData.setTid(crawlData.getTid());

        crawlData.setHtml(html.toString());
        //对于翻页的url,有查看全文链接
        if(entity.getViewAll() !=null && ! crawlData.isMultiPage())  {
            List<String> viewAllXpathList = (List<String>) JSON.parseObject(entity.getViewAll()).get("xpath");
            List<String> viewAllRegexList = (List<String>) JSON.parseObject(entity.getViewAll()).get("regex");
            Selectable selectable = null;
            List<String> viewAllUrlList = null;
            for (String viewAllXpath : viewAllXpathList) {
                selectable = new Html(crawlData.getHtml()).xpath(viewAllXpath+"/@href");
                if(selectable.get() != null)  {
                    break;
                }
            }
            if(selectable.get() !=null)  {
                Selectable selectableByRegex = null;
                for (String viewAllRegex : viewAllRegexList) {
                    selectableByRegex = selectable.regex(viewAllRegex);
                    if(selectableByRegex != null) {
                        viewAllUrlList = selectableByRegex.all();
                        break;
                    }
                }
                if(selectableByRegex.get() != null)   {
                    crawlData.setUrl(viewAllUrlList.get(0));
                    crawlData.setMultiPage(true);
                    crawlData.setHtml(null);

                    return crawlData;
                }else   {
                    try {
                        throw new Exception("可以viewAll拿到'查看全文'，但是不能通过配置的Regex进行筛选，请检查"+crawlData.getUrl());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }else   {
                try {
                    throw new Exception("不能viewAll拿到'查看全文'，请检查"+crawlData.getUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        crawlData.setDepthfromSeed(crawlData.getDepthfromSeed() + 1);
        crawlData.setFetched(true);
        crawlData.setCrawlTime(new Date());

        //put all fields into mup
        Map<String, Object> fieldMap = new HashMap<>();
        for (HtmlField htmlField : htmlFields) {
            String fieldValue = byXpaths(html, htmlField.getXpaths());

            if (fieldValue != null) {
                //deal img label
                if (fieldValue.contains("<img")) {
                    Html newContentHtml = new Html(fieldValue);

                    List<String> imgSrcs = newContentHtml.xpath("//img/@src").all();
                    String domain = "";
                    String[] arr = crawlData.getRootUrl().split("/");
                    domain = arr[0] + "//" + arr[2];
                    int end = crawlData.getUrl().lastIndexOf("/");
                    String preUrl = crawlData.getUrl().substring(0, end + 1);
                    //fix imag url
                    fieldValue = imgUrlPrefix(fieldValue, domain, preUrl, imgSrcs);
                    //put img src to redis for download img
                    fieldValue = imgDealWithRedis(fieldValue, crawlData);
                }

                //for the export of duocai ,set boolean true for "isContentHtml" attribute
                if (htmlField.getFieldName().equals("content")) {
                    //   fieldValue=byXpaths(html,htmlField.getXpaths());
                    String content = clearText4label(fieldValue);
                    crawlData.setTextPTag(content);

                    fieldMap.put("textPTag", content);
                }
                Html fieldHtml = new Html(fieldValue);
                List<String> fieldValues = fieldHtml.xpath("//*/text()").all();
                StringBuffer buffer = new StringBuffer();
                for (String value : fieldValues) {
                    buffer.append(value);
                }
                fieldValue = buffer.toString();

                //remove elements that is needn't
                List<String> excludesXpaths = htmlField.getExcludeXpaths();
                if (excludesXpaths != null && excludesXpaths.size() > 0) {
                    fieldValue = byExcludesXpaths(fieldValue, excludesXpaths);

                }
            }

            fieldMap.put(htmlField.getFieldName(), fieldValue);
            Map<String, String> tagMap = new HashMap<>();
            tagMap.put("column", crawlData.getTag());
            fieldMap.put("tag", tagMap);


            if (htmlField.getFieldName().equals("title")) {
                crawlData.setTitle(fieldValue == null ? "" : fieldValue);
            } else if (htmlField.getFieldName().equals("content")) {
                crawlData.setText(fieldValue == null ? "" : fieldValue.replaceAll("\\u00A0{1,}|\\u3000{1,}|\\u0020{1,}", "\n"));
            } else if (htmlField.getFieldName().equals("author")) {
                crawlData.setAuthor(fieldValue == null ? "" : fieldValue);
            } else if (htmlField.getFieldName().equals("sourceName")) {
                crawlData.setSourceName(fieldValue == null ? "" : fieldValue);
            } else if (htmlField.getFieldName().equals("publishTime")) {
                crawlData.setPublishTime(fieldValue == null ? "" : fieldValue);
            }

        }

        crawlData.setCrawlerdata(fieldMap);

        //对裁剪字段进行精确处理
        CrawlData croped_crawlerData = new FieldCroperHandler().accessFieldCroper(crawlData);

        return croped_crawlerData;
    }

    /**
     * judge the url which is the final content html
     *
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
     * 通过Xpath轴的ancestor取得对应栏目url下面的文章块
     * @param crawlData
     * @return
     */
    private List<String> getArticleBockByXpath(CrawlData crawlData){
        List<String> urls = null;
        for (String articleBlockXpath : articleBlockXpaths) {
            urls = new Html(crawlData.getHtml()).xpath(articleBlockXpath+"//a/@href").all();
            if(urls !=null) {
                return urls;
            }
        }
        return urls;
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
                if (selectable.toString() != null) {
                    List<String> contents = selectable.all();
                    StringBuffer buffer = new StringBuffer();
                    for (String content : contents) {
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
     *
     * @param contentHtml
     * @param xpaths
     * @return
     */
    private String byExcludesXpaths(String contentHtml, List<String> xpaths) {
        String regex = ">\\s*<";
        Html html = new Html(contentHtml);
        for (String xpath : xpaths) {
            Selectable selectable = html.xpath(xpath);
            if (selectable != null) {
                if (selectable.toString() != null) {
                    List<String> contents = selectable.all();
                    for (String content : contents) {
                        content = content.replace("\n", "").replace("\t", "").replaceAll(regex, "><");
                        contentHtml = html.xpath("/html/body/*").toString().replaceAll(regex, "><").replace(content, "");
                    }
                }
            }
        }

        return contentHtml;
    }

    public String imgDealWithRedis(String content, CrawlData crawlData) {
        Html html = new Html(content);
        List<String> imgSrcs = html.xpath("//img/@src").all();
        imgConvert(crawlData.getTid(), crawlData.getUrl(), imgSrcs);
        for (String url : imgSrcs) {
            content = replaceWithOSS(content, url, ALI_OSS_URL, crawlData.getTid());
        }

        // 这里给文章的缩略图字段添加第一张图片地址.
        if (imgSrcs.size() > 0) {
            String orgUrl = imgSrcs.get(0);
            int start = orgUrl.lastIndexOf(".");

            String suffixName = orgUrl.substring(start, orgUrl.length());
            String imgUrl = "";
            if (!suffixName.contains("/")) {
                imgUrl = ALI_OSS_URL + "/" + crawlData.getTid() + "/" + MD5.generateMD5(orgUrl) + suffixName;
            } else {
                imgUrl = ALI_OSS_URL + "/" + crawlData.getTid() + "/" + MD5.generateMD5(orgUrl) + suffixName.replace("/", "");
            }
            crawlData.setImgUrl(imgUrl);
        }

        return content;
    }

    /**
     * fix img src--->url missing domain
     *
     * @param content
     * @param domain
     * @param preUrl
     * @param srcs
     * @return
     */
    private String imgUrlPrefix(String content, String domain, String preUrl, List<String> srcs) {
        for (String oldUrl : srcs) {
            String fixedUrl = singleImgUrlPrefix(domain, preUrl, oldUrl);
            content = content.replace(oldUrl, fixedUrl);

        }
        return content;
    }

    /**
     * deal with 3 condition with url missing domain
     *
     * @param domain
     * @param url
     * @return
     * @Param preUrl
     */
    private String singleImgUrlPrefix(String domain, String preUrl, String url) {
        //1. url start with '/'
        //2. url start with '../../........'
        Preconditions.checkNotNull(domain, "preffix couldn't be null");
        Preconditions.checkNotNull(url, "url couldn't be null");
        if (url.equals("")) {
            return "";
        }
        if (url.startsWith("http")) {
            return url;
        }
        if (url.startsWith("/")) {
            url = domain + url;
        } else if (url.startsWith("../")) {
            int i = CharMatcher.anyOf(url).countIn("../");
            for (int j = 0; j <= i; j++) {
                int end = preUrl.lastIndexOf("/");
                preUrl = preUrl.substring(0, end);
            }
            url = preUrl + "/" + url.replace("../", "");
        } else {//not start with http
            url = preUrl + url;
        }
        //3. url start with '' nothing but not with http
        return url;
    }


    /**
     * replace the orgUrl with ossUrl for img label
     *
     * @param content
     * @param orgUrl
     * @param ossUrl
     * @return
     */
    private String replaceWithOSS(String content, String orgUrl, String ossUrl, String taskId) {
        String domain = "";
        String[] arr = orgUrl.split("/");
        if (arr.length > 2) {
            domain = arr[0] + "//" + arr[2];
            int start = orgUrl.lastIndexOf(".");
            String suffixName = orgUrl.substring(start, orgUrl.length());
            String url = "";
            if (!suffixName.contains("/")) {
                url = ossUrl + "/" + taskId + "/" + MD5.generateMD5(orgUrl) + suffixName;
            } else {
                url = ossUrl + "/" + taskId + "/" + MD5.generateMD5(orgUrl) + suffixName.replace("/", "");
            }
            content = content.replace(orgUrl, url);
        }

        return content;
    }

    public String generateRowKey(String taskId) {
        return taskId + "|" + new Date().getTime() + "|" + AlphabeticRandom.randomStringOfLength(5);
    }

    /**
     * 转换img的src属性，如果是绝对地址的话，直接使用
     * 否则，通过规则把相对地址转换成绝对地址
     */
    public void imgConvert(String taskId, String url, List<String> srcList) {
        JedisPoolUtils jedisPoolUtils = null;
        try {
            jedisPoolUtils = new JedisPoolUtils();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JedisPool pool = jedisPoolUtils.getJedisPool();
        Jedis imgJedis = pool.getResource();
        imgJedis.select(2);

        //redis中存储被替换的img的src地址的list srcurls
        String[] linkArray = srcList.toArray(new String[srcList.size()]);

        imgJedis.sadd("ImgSrcOf:" + taskId, linkArray);

    }

    public String clearText4label(String html) {
        html = CharMatcher.WHITESPACE.collapseFrom(html, ' ');
        // html = CharMatcher.WHITESPACE.collapseFrom(html,' ');
        html = html.replaceAll("<style.*?>.*?</style>", "");
        html = html.replaceAll("<div.*?>", "");
        html = html.replace("</div>", "");
        html = html.replaceAll("<script.*?>.*?</script>", "");
        html = html.replaceAll("<!--.*?-->", "");
        html = html.replaceAll("<iframe.*?>.*?</iframe>", "");
        // 把 p 标签里面的各种样式去掉以及 strong 标签去掉。
        html = html.replaceAll("<p.*?(align=\"center\").*?>", "<p align=\"center\">").replaceAll("<strong.*?>|</strong>|<span.*?>|</span>|<font.*?>|</font>", "");
        return html;
    }
}