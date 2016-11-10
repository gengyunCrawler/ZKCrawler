package cn.com.cloudpioneer.zkcrawlerAPI.model;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by TianyuanPan on 11/9/16.
 */
public class BatchResult {

    private boolean status;
    private String  nextSign;
    private int     size;
    private List<CrawlData> data = new ArrayList<>();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getNextSign() {
        return nextSign;
    }

    public void setNextSign(String nextSign) {
        this.nextSign = nextSign;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<CrawlData> getData() {
        return data;
    }

    public void setData(List<CrawlData> data) {
        this.data = data;
    }

    public void setData(CrawlData[] data) {
        Collections.addAll(this.data,data);
    }

    public CrawlData addData(CrawlData data){

        this.data.add(data);
        return data;
    }

    public List<CrawlData> addData(List<CrawlData> data){

        this.data.addAll(data);
        return this.data;
    }

    public String toJSONString(){

        return JSONObject.toJSONString(this);
    }

}
