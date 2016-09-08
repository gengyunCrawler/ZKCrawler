package com.gy.wm.model.rdb;

import com.gy.wm.model.CrawlData;
import com.gy.wm.model.InsertSqlModel;
import com.gy.wm.util.CrawlerDataUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 5/20/16.
 */
public class FengBirdModel implements RdbModel{

    private String topicTaskID;
    private String title;
    private long   labelTime; // long time
    private String url;
    private String fromUrl;
    private String rootUrl;
    private long   crawlTime;  // long time
    private int    deleteflag;

    public FengBirdModel() {

        this.deleteflag = 0;
    }

    public FengBirdModel(CrawlData crawlData) {

        this.topicTaskID = crawlData.getTid();
        this.title = crawlData.getTitle();
        this.labelTime = crawlData.getPublishTime();
        this.url = crawlData.getUrl();
        this.fromUrl = crawlData.getFromUrl();
        this.rootUrl = crawlData.getRootUrl();
        this.crawlTime = crawlData.getCrawlTime();
        this.deleteflag = 0;

    }

    public String getTopicTaskID() {
        return topicTaskID;
    }

    public void setTopicTaskID(String topicTaskID) {
        this.topicTaskID = topicTaskID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getLabelTime() {
        return labelTime;
    }

    public void setLabelTime(long labelTime) {
        this.labelTime = labelTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public long getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(long crawlTime) {
        this.crawlTime = crawlTime;
    }

    public int getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(int deleteflag) {
        this.deleteflag = deleteflag;
    }


    @Override
    public Object setThisModelFields(CrawlData data) {

        List<Map<String, Object>> fieldList = CrawlerDataUtils.getCrawlerDataUtils(data).getAttributeInfoList();

        for (int i = 0; i < fieldList.size(); ++i) {

            String field = (String) fieldList.get(i).get("name");

            switch (field) {
                case "tid":
                    this.setTopicTaskID((String) fieldList.get(i).get("value"));
                    break;
                case "url":
                    this.setUrl((String) fieldList.get(i).get("value"));
                    break;
                case "crawlTime":
                    this.setCrawlTime((long) fieldList.get(i).get("value"));
                    break;
                case "publishTime":
                    this.setLabelTime((long) fieldList.get(i).get("value"));
                    break;
                case "title":
                    this.setTitle((String) fieldList.get(i).get("value"));
                    break;
                case "rootUrl":
                    this.setRootUrl((String) fieldList.get(i).get("value"));
                    break;
                case "fromUrl":
                    this.setFromUrl((String) fieldList.get(i).get("value"));
                    break;
                default:
                    break;
            }

        }

        return this;

    }

    @Override
    public InsertSqlModel insertSqlModelBuilder(String tableName) {

        InsertSqlModel model = new InsertSqlModel(tableName);

        List<Map<String, Object>> fieldList = CrawlerDataUtils.getCrawlerDataUtils(this).getAttributeInfoList();

        for (int i = 0; i < fieldList.size(); ++i) {

            String type = (String) fieldList.get(i).get("type");

            switch (type) {
                case "long":
                    String dateString = "'" +
                            new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
                                    .format(new Date((long) fieldList.get(i).get("value"))) + "'";

                    model.addKeyValue((String) fieldList.get(i).get("name"), dateString);
                    break;
                case "int":
                    model.addKeyValue((String) fieldList.get(i).get("name"), fieldList.get(i).get("value"));
                    break;
                default:
                    model.addKeyValue((String) fieldList.get(i).get("name"), "'" + fieldList.get(i).get("value") + "'");
                    break;
            }


        }
        return model;
    }
}
