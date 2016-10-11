package com.gy.wm.vo;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <类详细说明:任务参数，需要读取路径取得的参数，如tempalte路径下的
 * templates,已全部转换成字符串存到数据库中>
 *
 * @Author： Huanghai
 * @Version: 2016-09-20
 **/
@Component
public class Param {
    //tags为保留字段
    List<String> tags;

    List<String> configs;

    List<String> regexFilter;

    List<String> templates;

    List<String> suffixFilter;

    List<String> seedUrls;

    List<String> clickRegex;

    List<String> protocolFilter;

    List<String> downloader;

    List<String> urlParser;

    List<String> pageParser;

    List<String> proxy;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getConfigs() {
        return configs;
    }

    public void setConfigs(List<String> configs) {
        this.configs = configs;
    }

    public List<String> getRegexFilter() {
        return regexFilter;
    }

    public void setRegexFilter(List<String> regexFilter) {
        this.regexFilter = regexFilter;
    }

    public List<String> getTemplates() {
        return templates;
    }

    public void setTemplates(List<String> templates) {
        this.templates = templates;
    }

    public List<String> getSuffixFilter() {
        return suffixFilter;
    }

    public void setSuffixFilter(List<String> suffixFilter) {
        this.suffixFilter = suffixFilter;
    }

    public List<String> getSeedUrls() {
        return seedUrls;
    }

    public void setSeedUrls(List<String> seedUrls) {
        this.seedUrls = seedUrls;
    }

    public List<String> getClickRegex() {
        return clickRegex;
    }

    public void setClickRegex(List<String> clickRegex) {
        this.clickRegex = clickRegex;
    }

    public List<String> getProtocolFilter() {
        return protocolFilter;
    }

    public void setProtocolFilter(List<String> protocolFilter) {
        this.protocolFilter = protocolFilter;
    }

    public List<String> getDownloader() {
        return downloader;
    }

    public void setDownloader(List<String> downloader) {
        this.downloader = downloader;
    }

    public List<String> getUrlParser() {
        return urlParser;
    }

    public void setUrlParser(List<String> urlParser) {
        this.urlParser = urlParser;
    }

    public List<String> getPageParser() {
        return pageParser;
    }

    public void setPageParser(List<String> pageParser) {
        this.pageParser = pageParser;
    }

    public List<String> getProxy() {
        return proxy;
    }

    public void setProxy(List<String> proxy) {
        this.proxy = proxy;
    }
}
