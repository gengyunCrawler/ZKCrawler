package com.gy.wm.util;

import com.gy.wm.entry.Crawl;
import com.gy.wm.model.CrawlData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * <类详细说明:通过反射取得具体解析插件,并执行解析方法>
 *
 * @Author： Huanghai
 * @Version: 2016-09-14
 **/
public class PluginUtil {
    private static final Logger LOG= LoggerFactory.getLogger(PluginUtil.class);
    private static final String PLUGIN_NAME = ResourceBundle.getBundle("config").getString("pluginName");

    public static List<CrawlData> excutePluginParse(CrawlData crawlData) throws Exception{
        Class c = Class.forName(PLUGIN_NAME);
        Object object = c.newInstance();
        //得到方法
        Method methlist[] = c.getDeclaredMethods();
        for(int i=0; i<methlist.length; i++)    {
            Method m = methlist[i];
        }
        //获取的方法对象，假设方法的参数是一个CrawlData,method名为parse
        Method pse = c.getMethod("parse",new Class[]{CrawlData.class});
        //获取参数Object
        Object[] arguments = new Object[]   {crawlData};
        //执行方法
        List<CrawlData> crawlDataList = new ArrayList<>();
        crawlDataList = (List<CrawlData>) pse.invoke(object,arguments);
        LOG.info("cralDataList size: " + crawlDataList.size());
        return crawlDataList;
    }

    public static void main(String[] args) {
        CrawlData cd = new CrawlData();
        try {
            excutePluginParse(cd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
