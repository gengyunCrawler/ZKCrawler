package com.gy.wm.plugins.topicPlugin.analysis;

import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 15-12-22.
 */
public class WholesiteAnalysis implements Serializable {
    private List<BaseTemplate> baseTemplates;

    private WholeSiteAnalysisNavigation wholeSiteAnalysisNavigation;

    public WholesiteAnalysis(List<BaseTemplate> baseTemplates) {
        this.baseTemplates = baseTemplates;
        this.wholeSiteAnalysisNavigation =  new WholeSiteAnalysisNavigation();
    }

    /*public PairFlatMapFunction<Tuple2<Text, Crawldb>, Text, Crawldb> analysis() {
        PairFlatMapFunction<Tuple2<Text, Crawldb>, Text, Crawldb> result = new PairFlatMapFunction<Tuple2<Text, Crawldb>, Text, Crawldb>() {
            @Override
            public Iterable<Tuple2<Text, Crawldb>> call(Tuple2<Text, Crawldb> tuple2) throws Exception {
                List<Tuple2<Text, Crawldb>> ans = new ArrayList<Tuple2<Text, Crawldb>>();
                List<BaseAnalysisURL> baseAnalysisURLList = new ArrayList<>();


                //获取url
                String url = tuple2._1().toString();
                Crawldb oldCrawldb = tuple2._2();

                String rootUrl = oldCrawldb.getRootUrl();
                String html = oldCrawldb.getHtml();
                String title = oldCrawldb.getTitle();
                long date = oldCrawldb.getPublishtime();
                long depth = oldCrawldb.getDepthfromSeed();
                boolean tag = oldCrawldb.isTag();
                boolean fetched = oldCrawldb.isFetched();

                BaseAnalysisURL oldUrl = new BaseAnalysisURL(url, title, date, html);
                try {
                    baseAnalysisURLList = wholeSiteAnalysisNavigation.getUrlList(url, html);
                    for (BaseAnalysisURL baseAnalysisURL : baseAnalysisURLList) {
                        Crawldb newCrawdb = new Crawldb();
                        Text newtext = new Text();
                        newtext.set(baseAnalysisURL.getUrl());
                        newCrawdb.setUrl(baseAnalysisURL.getUrl());
                        newCrawdb.setTitle(baseAnalysisURL.getTitle());
                        newCrawdb.setRootUrl(rootUrl);
                        newCrawdb.setDepthfromSeed(depth + 1);
                        newCrawdb.setFromUrl(url);
                        newCrawdb.setPublishtime(baseAnalysisURL.getDate());
                        newCrawdb.setFetched(false);
                        ans.add(new Tuple2<Text, Crawldb>(newtext, newCrawdb));
                    }
                    oldCrawldb.setFetched(true);
                    oldCrawldb.setTag(false);
                    oldCrawldb.setText(wholeSiteAnalysisNavigation.getText());

                    ans.add(new Tuple2<Text, Crawldb>(new Text(oldCrawldb.getUrl()), oldCrawldb));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return ans;
            }
        };

        return result;
    }*/


}