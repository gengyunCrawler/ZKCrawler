package com.gy.wm.plugins.topicPlugin.analysis;

import com.gy.wm.model.CrawlData;
import com.gy.wm.plugins.topicPlugin.urljudge.HtmlSort;
//import com.gy.wm.util.TimeJudger;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 15-12-22.
 */
public class TextAnalysis implements Serializable {
    private List<BaseTemplate> baseTemplates;

    private AnalysisArticle analysisArticle;

    public TextAnalysis(List<BaseTemplate> baseTemplates) {
        this.baseTemplates = baseTemplates;
        this.analysisArticle = new AnalysisArticle();
    }

    public List<CrawlData> analysisHtml(CrawlData crawlData)   {
        List<CrawlData> crawlDataList = new ArrayList<>();
        List<BaseAnalysisURL> baseAnalysisURLList = new ArrayList<>();
        //初始化
        AnalysisNavigation analysisNavigation = new AnalysisNavigation();

        String url = crawlData.getUrl();
        String html = crawlData.getHtml();
        String tid = crawlData.getTid();
        int pass = crawlData.getPass();
        String type = crawlData.getType();
        String startTime = crawlData.getStartTime();
        long depthfromSeed = crawlData.getDepthfromSeed();

        String title = "";
        Date date = null;

        BaseAnalysisURL oldUrl = new BaseAnalysisURL(url, title, date, html);

        //网页分类
        int sort = HtmlSort.getHtmlSort(url, html);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //导航解析
        if(sort ==1)    {
            try {
                baseAnalysisURLList = analysisNavigation.getUrlList(url,html);
                for(BaseAnalysisURL baseAnalysisURL : baseAnalysisURLList)  {
                    CrawlData newCrawlData = new CrawlData();
                    newCrawlData.setTid(tid);
                    newCrawlData.setPass(pass);
                    newCrawlData.setType(type);
                    newCrawlData.setStartTime(startTime);
                    newCrawlData.setDepthfromSeed(depthfromSeed + 1);
                    newCrawlData.setRootUrl(url);
                    newCrawlData.setFromUrl(url);
                    newCrawlData.setUrl(baseAnalysisURL.getUrl());
                    newCrawlData.setTitle(baseAnalysisURL.getTitle());
                    newCrawlData.setPublishTime(baseAnalysisURL.getDate());

                    newCrawlData.setCrawlTime(new Date(System.currentTimeMillis()));
                    newCrawlData.setHtml(baseAnalysisURL.getHtml());
                    newCrawlData.setText(baseAnalysisURL.getText());
                    newCrawlData.setFetched(false);
                    newCrawlData.setTag(false);

                    crawlDataList.add(newCrawlData);
                }
                crawlData.setFetched(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            //文章页解析
            try {
                oldUrl = analysisArticle.analysisArticle(oldUrl, baseTemplates);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            crawlData.setTitle(oldUrl.getTitle());
            if(crawlData.getPublishTime() == null)  {
                //crawlData.setPublishTime(TimeJudger.validTime(oldUrl.getDate(),sdf));
            }
            crawlData.setCrawlTime(new Date(System.currentTimeMillis()));
            crawlData.setText(oldUrl.getText());
            crawlData.setHtml(oldUrl.getHtml());
            crawlData.setFetched(true);
            crawlData.setTag(true);

            crawlDataList.add(crawlData);
        }
        return crawlDataList;
    }

    public static void main(String[] args) {
        System.out.println(Long.parseLong(null));
    }

}
