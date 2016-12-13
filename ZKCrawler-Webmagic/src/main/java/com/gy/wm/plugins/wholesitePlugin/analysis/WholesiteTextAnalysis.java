package com.gy.wm.plugins.wholesitePlugin.analysis;

import com.gy.wm.model.CrawlData;
import com.gy.wm.service.PageParser;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 15-12-22.
 */
public class WholesiteTextAnalysis implements Serializable, PageParser {

    private WholeSiteAnalysis wholeSiteAnalysis;

    public void setWholeSiteAnalysis(WholeSiteAnalysis wholeSiteAnalysis) {
        this.wholeSiteAnalysis = wholeSiteAnalysis;
    }

    //format
    private final static SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss");

    public WholesiteTextAnalysis(WholeSiteAnalysis wholeSiteAnalysis) {
        this.wholeSiteAnalysis = wholeSiteAnalysis;
    }

    public WholesiteTextAnalysis() {
    }

    public List<CrawlData> parse(CrawlData crawlData) {
        this.setWholeSiteAnalysis(new WholeSiteAnalysis());
        List<CrawlData> crawlDataList = new ArrayList<>();

        String html = crawlData.getHtml();

        if (html != null || html.length() > 0) {
            String rootUrl = crawlData.getRootUrl();
            long depth = crawlData.getDepthfromSeed();
            String fromUrl = crawlData.getUrl();
            try {
                List<BaseURL> baseURLList = wholeSiteAnalysis.getUrlList(fromUrl, html);
                for (int i = 0; i < baseURLList.size(); i++) {
                    //CrawlData newCrawlData = createNewCrawlData(baseURLList.get(i),rootUrl,depth,fromUrl,crawlData.getPass(),crawlData.getTid(),crawlData.getStartTime());
                    CrawlData newCrawlData = createNewCrawlData(baseURLList.get(i), crawlData);
                    crawlDataList.add(newCrawlData);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            crawlData.setText(wholeSiteAnalysis.getText());
        }
        crawlData.setFetched(true);
        crawlData.setCrawlTime(new Date());
        crawlDataList.add(crawlData);
        return crawlDataList;
    }

    private CrawlData createNewCrawlData(BaseURL baseURL, String rootURL, long depth, String fromUrl, int pass, String tid, String startTime) {
        CrawlData crawlData = new CrawlData();
        if (baseURL != null) {
            String url = baseURL.getUrl();
            crawlData.setTid(tid);
            crawlData.setStartTime(startTime);
            crawlData.setRootUrl(rootURL);
            crawlData.setDepthfromSeed(depth + 1);
            crawlData.setFromUrl(fromUrl);
            crawlData.setPublishTime(sdf.format(new Date(baseURL.getDate())));
            crawlData.setUrl(url);
            crawlData.setPass(pass);
            crawlData.setTitle(baseURL.getTitle());
            crawlData.setFetched(false);
        }
        return crawlData;
    }

    private CrawlData createNewCrawlData(BaseURL baseURL, CrawlData parent) {
        CrawlData crawlData = new CrawlData();
        if (baseURL != null) {
            String url = baseURL.getUrl();
            crawlData.setTid(parent.getTid());
            crawlData.setStartTime(parent.getStartTime());
            crawlData.setRootUrl(parent.getRootUrl());
            crawlData.setDepthfromSeed(parent.getDepthfromSeed() + 1);
            crawlData.setFromUrl(parent.getUrl());
            crawlData.setPublishTime(sdf.format(new Date(baseURL.getDate())));
            crawlData.setUrl(url);
            crawlData.setPass(parent.getPass());
            crawlData.setTitle(baseURL.getTitle());
            crawlData.setType(parent.getType());
            crawlData.setCategories(parent.getCategories());
            crawlData.setTags(parent.getTags());
            crawlData.setSourceTypeId(parent.getSourceTypeId());
            crawlData.setTag(parent.getTag());
            crawlData.setFetched(false);
        }
        return crawlData;
    }

}
