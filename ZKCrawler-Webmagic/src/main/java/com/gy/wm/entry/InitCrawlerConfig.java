package com.gy.wm.entry;

import com.gy.wm.plugins.topicPlugin.analysis.BaseTemplate;
import com.gy.wm.util.BloomFilter;
import com.kenai.jaffl.annotations.In;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class InitCrawlerConfig {

    private static  List<String> postRegex;
    private static List<BaseTemplate> listTemplate;
    private static List<String> regexList;
    private static List<String> protocols;
    private static int recalldepth;
    private static BloomFilter sparkBloomFilter;
    private static int depth;

    public List<String> getPostRegex() {
        return postRegex;
    }

    public void setPostRegex(List<String> postRegex) {
        this.postRegex = postRegex;
    }

    public List<BaseTemplate> getListTemplate() {
        return listTemplate;
    }

    public void setListTemplate(List<BaseTemplate> listTemplate) {
        this.listTemplate = listTemplate;
    }

    public List<String> getRegexList() {
        return regexList;
    }

    public void setRegexList(List<String> regexList) {
        this.regexList = regexList;
    }

    public List<String> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<String> protocols) {
        this.protocols = protocols;
    }

    public int getRecalldepth() {
        return recalldepth;
    }

    public void setRecalldepth(int recalldepth) {
        this.recalldepth = recalldepth;
    }

    public static BloomFilter getSparkBloomFilter() {
        return sparkBloomFilter;
    }

    public static void setSparkBloomFilter(BloomFilter sparkBloomFilter) {
        InitCrawlerConfig.sparkBloomFilter = sparkBloomFilter;
    }

    public static int getDepth() {
        return depth;
    }

    public static void setDepth(int depth) {
        InitCrawlerConfig.depth = depth;
    }


    public InitCrawlerConfig(String appname, int depth, List<String> templateList, List<String> clickregexDir, List<String> protocolList, List<String> postregexDir)   {

        //读取模板
        String str;
        if(null != templateList)    {
            for (String template : templateList) {
                List<String> tokens = new ArrayList<>();
                String domain = new String();
                try {
                    InputStream in = new ByteArrayInputStream(template.getBytes("UTF-8"));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    domain = reader.readLine();
                    while ((str = reader.readLine()) != null)   {
                        tokens.add(str);
                    }
                    reader.close();
                    if(null != domain && tokens.size() > 0) {
                        listTemplate.add(new BaseTemplate(domain,tokens));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            setListTemplate(listTemplate);
        }

        //设置爬取深度
        setDepth(depth);
        //读取协议过滤
        //读取后缀过滤

    }

    //测试
    public static void main(String[] args) {
        String str = "http://www.gog.cn/" +
                "\n"+"hello,world!";
        try {
            InputStream in = new ByteArrayInputStream(str.getBytes("UTF-8"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String domain = reader.readLine();
            System.out.println("domain: " +domain);
            while ((str = reader.readLine()) != null)   {
                System.out.println("content: " +str);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
