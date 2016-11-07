package cn.com.cloudpioneer.zkcrawlerAPI.model.elastic;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TianyuanPan on 11/1/16.
 */
public class TagsInfo {

    private String name;
    private String addTime;

    public TagsInfo(String name) {

        this.name = name;
        this.addTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public TagsInfo() {

        this.name = "";
        this.addTime = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
