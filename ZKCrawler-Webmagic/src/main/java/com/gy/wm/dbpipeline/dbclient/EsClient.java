package com.gy.wm.dbpipeline.dbclient;

import com.alibaba.fastjson.JSON;
import com.gy.wm.model.CrawlData;
import com.gy.wm.util.ConfigUtils;
import com.gy.wm.util.HttpUtils;
import com.gy.wm.util.RandomUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TianyuanPan on 5/9/16.
 */
public class EsClient {

    private String hostname;
    private int port;
    private String indexName;
    private String typeName;


    private List<CrawlData> dataList;

    private String requestUrl;


    public EsClient() {

        this.hostname = ConfigUtils.getResourceBundle().getString("ES_HOSTNAME");
        this.port = Integer.parseInt(ConfigUtils.getResourceBundle().getString("ES_PORT"));
        this.indexName = ConfigUtils.getResourceBundle().getString("ES_INDEX_NAME");
        this.typeName = ConfigUtils.getResourceBundle().getString("ES_TYPE_NAME");

        this.requestUrl = "http://" + this.hostname + ":" +
                this.port + "/" + this.indexName + "/" + this.typeName + "/";

        this.dataList = new ArrayList<>();

    }


    public Object getConnection() {

        return null;
    }

    public void closeConnection() {


    }


    public int doSetInsert() {

        int count = 0;

        for (int i = 0; i < dataList.size(); ++i) {

            String dataJson = JSON.toJSONString(dataList.get(i));

            try {

                //this.doPut(this.requestUrl + RandomUtils.getRandomString(50) + "_" + new Date().getTime(), dataJson);
                HttpUtils.doPut(this.requestUrl + RandomUtils.getRandomString(50) + "_" + new Date().getTime(), dataJson);
                ++count;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        this.dataList.clear();

        return count;
    }

    public int doSetInsert(String url, String data) {

        try {


            HttpUtils.doPut(url, data);


        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }

        return 1;
    }

    public void add(CrawlData data) {

        this.dataList.add(data);
    }

    public String getRequestUrl() {

        return requestUrl;
    }

}

