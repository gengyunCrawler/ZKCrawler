package com.gy.wm.dbpipeline.impl;

import com.gy.wm.model.CrawlData;
import com.gy.wm.model.CrawlDataMapper;
import com.gy.wm.util.HDFSUtils;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by TijunWang on 2016/10/25.
 */
public class HDFSPipeline implements Pipeline {

    private String dfsPath;

    public HDFSPipeline(String dfsPath){
        this.dfsPath=dfsPath;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        CrawlData crawlData = resultItems.get("crawlerData");
        this.insertToDFS(crawlData);
    }

    public void insertToDFS(CrawlData crawlData)   {
       String path=dfsPath+"/"+crawlData.getTid()+".txt";
        try {
            if (!HDFSUtils.fileIsExist(path)){
                HDFSUtils.write(path,crawlData.getJsonData());
            }else {
                HDFSUtils.writeByAppend(path,crawlData.getJsonData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
