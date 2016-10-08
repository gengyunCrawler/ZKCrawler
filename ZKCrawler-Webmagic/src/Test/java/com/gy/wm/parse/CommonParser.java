package com.gy.wm.parse;

import com.alibaba.fastjson.JSONObject;
import com.gy.wm.dao.ParserDao;
import com.gy.wm.model.CrawlData;
import com.gy.wm.service.PageParser;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tijun on 2016/10/8.
 * @author TijunWang
 */
public class CommonParser implements PageParser {
    private ParserConfig config=null;
    private ParserDao parserDao =new ParserDao();
    private List<String> contentLinkRegexs=new ArrayList<>();
    private List<String> columnRegexs=new ArrayList<>();

    @Override
    public List<CrawlData> parse(CrawlData crawlData) {

        List<CrawlData> crawlDatas=new ArrayList<>();
        //obtain crawler config from MySQL ,one task one config(json),so for a task ,it just accesses database once
        if(config==null){
            ParserEntity entity= parserDao.find(crawlData.getTid());
            if (entity==null){
                try {
                    throw new Exception("ERROR:couldn't find config by tid='"+crawlData.getTid()+"'");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            config= JSONObject.parseObject(entity.getConfig(),ParserConfig.class);

            for(UrlPattern pattern:config.getUrlPatterns()){
                if (pattern.getType().equals(ParserConfig.HTML_LINK_REGEX)){
                    contentLinkRegexs.add(pattern.getRegex());
                }
                if (pattern.getType().equals(ParserConfig.URL_PARTTERN_TYPE_COLUMN_REGEX)){
                    columnRegexs.add(pattern.getRegex());
                }
            }

        }
        //discovery links
        //judge current page class(links page or content page)
        if(isColumnHtml(crawlData.getUrl())){
            List<String> urls=new Html(crawlData.getHtml()).xpath("//a/@href").all();
            for (String url:urls){
                if(isContentHtml(url)){
                    CrawlData data=new CrawlData();
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
        if (isContentHtml(crawlData.getUrl())){
            crawlDatas.add(parseData(new Html(crawlData.getHtml()),crawlData,config.getFileds()));
        }

        return crawlDatas;
    }

    /**
     *Parsing data from html and result in crawlerData
     * @param html
     * @param crawlData
     * @param htmlFields
     * @return  CrawlerData
     */
    private CrawlData parseData(Html html,CrawlData crawlData,List<HtmlField> htmlFields){


        String title=null;
        //包含html格式的文章段落
        String contentHtml=null;
        //来源和作者字段

        for (HtmlField htmlField:htmlFields){
            if (htmlField.getFieldName().equals("title")){
               title=byXpaths(html,htmlField.getXpaths());
            }

            if (htmlField.getFieldName().equals("content")){
                contentHtml=byXpaths(html,htmlField.getXpaths());
            }
        }

        //贵阳市
//        String title=html.xpath("//td[@class='title']").toString();
//        String contentHtml=html.xpath("//td[@id='contents']").toString();

        crawlData.setTid(crawlData.getTid());
        crawlData.setTitle(title);
        crawlData.setHtml(html.toString());
        crawlData.setDepthfromSeed(crawlData.getDepthfromSeed()+1);
        crawlData.setFetched(true);
        crawlData.setCrawlTime(new Date());
        crawlData.setText(contentHtml);

        return crawlData;
    }

    private boolean isContentHtml(String url){
        for (String urlRegex:contentLinkRegexs){
            if (url.matches(urlRegex)){
                return true;
            }
        }
        return false;
    }

    private boolean isColumnHtml(String url){
        for (String urlRegex:columnRegexs){
            if (url.matches(urlRegex)){
                return true;
            }
        }
        return false;
    }
    private String byXpaths(Html html,List<String> xpaths){
        for (String xpath:xpaths){
           Selectable selectable= html.xpath(xpath);
            if (selectable!=null){
                return selectable.toString();
            }
        }
        return null;
    }
}
