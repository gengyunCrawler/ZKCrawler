package com.gy.wm.plugins.newsExportPlugin;

import com.gy.wm.model.CrawlData;
import com.gy.wm.service.PageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tijun on 2016/9/26.
 */
public class QNRParser implements PageParser {
    private final Logger LOG= LoggerFactory.getLogger(QNRParser.class);
    //黔都在线
    private static final String QNR_ARTICLE_REGEX="http://www.qnz.com.cn/news/newsshow-\\d*.shtml";
    private static final  String QNR_COLUMN_REGEX="http://www.qnz.com.cn/news/newslist-0-\\d*.shtml";
    //贵阳市
//    private static final  String QNR_COLUMN_REGEX="http://www.gygov.gov.cn/col/col10683/index.html";
//    private static final String QNR_ARTICLE_REGEX="http://www.gygov.gov.cn/art/\\d*/\\d*/\\d*/art.*.html";
    @Override
    public List<CrawlData> parse(CrawlData crawlData) {
        //match the url list
        List<CrawlData> crawlDatas=new ArrayList<>();
        List<String> urls=new ArrayList<>();

        if (crawlData.getUrl().matches(QNR_COLUMN_REGEX)){
            urls=new Html(crawlData.getHtml()).xpath("//a/@href").all();
            for (String url:urls){
                if (url.matches(QNR_ARTICLE_REGEX)){
                    System.out.println(url);
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

        if (crawlData.getUrl().matches(QNR_ARTICLE_REGEX)){
                crawlDatas.add(parseData(new Html(crawlData.getHtml()),crawlData));
        }
        return crawlDatas;
    }
    private CrawlData parseData(Html html,CrawlData crawlData){
        //黔都在线
        //标题
        String title=html.xpath("//div[@class='Title_h1']/h1/text()").toString();
        //包含html格式的文章段落
        String contentHtml=html.xpath("//div[@id='content_main']").toString();
        //来源和作者字段
        String basicInfo=html.xpath("").toString();
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
}
