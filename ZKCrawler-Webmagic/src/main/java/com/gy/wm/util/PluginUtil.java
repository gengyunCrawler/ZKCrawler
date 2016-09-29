package com.gy.wm.util;

import com.gy.wm.model.CrawlData;
import com.gy.wm.service.CustomPageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * <类详细说明:通过反射取得具体插件,并执行解析方法>
 *
 * @Author： Huanghai
 * @Version: 2016-09-14
 **/
public class PluginUtil {
    private static final Logger LOG= LoggerFactory.getLogger(PluginUtil.class);
    private static final String PARSE_PLUGIN_NAME = ResourceBundle.getBundle("config").getString("parsePluginName");
    private static final String DOWNLOAD_PLUGIN_NAME = ResourceBundle.getBundle("config").getString("donwloadPluginName");

    public List<CrawlData> excutePluginParse(CrawlData crawlData) throws Exception{
        Class c = Class.forName(PARSE_PLUGIN_NAME);
        Object object = c.newInstance();
        //得到方法
        Method methlist[] = c.getDeclaredMethods();
        for(int i=0; i<methlist.length; i++)    {
            Method m = methlist[i];
        }
        //获取的方法对象，假设方法名为parse,参数是CrawlData
        Method pse = c.getMethod("parse",new Class[]{CrawlData.class});
        //获取参数Object
        Object[] arguments = new Object[]   {crawlData};
        //执行方法
        List<CrawlData> crawlDataList = new ArrayList<>();
        crawlDataList = (List<CrawlData>) pse.invoke(object,arguments);
        LOG.info("crawlDataList size：" + crawlDataList.size());
        return crawlDataList;
    }

    public Spider excutePluginDownload(String tid,String domain) throws Exception{
        Class c = Class.forName(DOWNLOAD_PLUGIN_NAME);
        Object object = c.newInstance();
        return Spider.create(new CustomPageProcessor(tid, domain)).setDownloader((Downloader) object);
    }
}
