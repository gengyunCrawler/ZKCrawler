package com.gy.wm.plugins.testPlugin;

import com.gy.wm.model.CrawlData;
import com.gy.wm.service.PageParser;
import com.mysql.jdbc.log.Log;
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
    private static final String QNR_ARTICLE_REGEX="http://news.sohu.com/\\d*/n\\d*.shtml";

    private static final  String QNR_COLUMN_REGEX="http://www.qnz.com.cn/news/newslist-0-\\d*.shtml";
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
                    crawlDatas.add(data);
                }
            }
        }

        if (crawlData.getUrl().matches(QNR_ARTICLE_REGEX)){
                crawlDatas.add(parseData(new Html(crawlData.getHtml()),crawlData));
        }
        return crawlDatas;
    }
    private CrawlData parseData(Html html,CrawlData crawlData1){
        CrawlData crawlData=new CrawlData();
        String title=html.xpath("//div[@class='Title_h1']/h1/").toString();
        String contentHtml=html.xpath("//div[@id='MyContent']/table/tbody/tr[1]/td/div").toString();
        crawlData.setTitle(title);
        crawlData.setHtml(contentHtml);
        LOG.info("title:"+title);
        LOG.info("content:"+contentHtml);
        crawlData.setFetched(true);
        crawlData.setDepthfromSeed(1);
        crawlData.setCrawlTime(new Date());
        crawlData.setFromUrl(crawlData1.getUrl());
        crawlData.setText(html.smartContent().get());

        crawlData.setTid(crawlData1.getTid());
        return crawlData;

    }
}
