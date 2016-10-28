package com.gy.wm.dbpipeline.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gy.wm.model.CrawlData;
import com.gy.wm.model.CrawlDataMapper;
import com.gy.wm.util.HDFSUtils;
import com.gy.wm.util.RandomStringCreator;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TijunWang on 2016/10/25.
 */
public class HDFSPipeline implements Pipeline {

    private static Map<String,String> tagMap = new HashMap<>();

    private String dfsPath;

    static {
        tagMap.put("column","黔西南州人民政府网-义龙试验区");
        tagMap.put("heat","8");
    }
    public HDFSPipeline(String dfsPath){
        this.dfsPath=dfsPath;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        CrawlData crawlData = resultItems.get("crawlerData");
        JSONObject object= JSON.parseObject(crawlData.getJsonData());
        object.put("tag",tagMap);
        object.put("id", RandomStringCreator.getString());
        crawlData.setJsonData("\n"+object.toJSONString());
        this.insertToDFS(crawlData);
    }

    public void insertToDFS(CrawlData crawlData)   {
       String path=dfsPath+"/"+fileName() + ".txt";
        String newsPath = dfsPath + "/" + "news.txt";
        try {
            if (!HDFSUtils.fileIsExist(path)){
                HDFSUtils.write(path,crawlData.getJsonData());
            }else {
                HDFSUtils.writeByAppend(path,crawlData.getJsonData());
            }

            if (!HDFSUtils.fileIsExist(newsPath)){
                HDFSUtils.write(newsPath,crawlData.getJsonData());
            }else {
                HDFSUtils.writeByAppend(newsPath,crawlData.getJsonData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private String fileName(){
return  "news-"+new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
    }
}
