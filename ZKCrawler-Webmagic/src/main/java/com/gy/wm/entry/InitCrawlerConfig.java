package com.gy.wm.entry;

import com.gy.wm.plugins.wholesitePlugin.analysis.BaseTemplate;
import com.gy.wm.util.BloomFilter;

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


    public InitCrawlerConfig(String appname, int recalldepth, String templatesDir, String clickregexDir, String protocolDir, String postregexDir)   {

        //读取模板
        listTemplate = new ArrayList<>();
        String str;
        File files = new File(templatesDir);
        File[] templateFiles = files.listFiles();
        List<File> fileList = new ArrayList<>();
        for(File templateFile : templateFiles)    {
            List<String> tokens = new ArrayList<>();
            String domain = new String();
            try {
                InputStream in = new FileInputStream(templateFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                domain = reader.readLine();
                while ((str = reader.readLine()) != null) tokens.add(str);
                reader.close();
                listTemplate.add(new BaseTemplate(domain,tokens));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setListTemplate(listTemplate);

    }
}
