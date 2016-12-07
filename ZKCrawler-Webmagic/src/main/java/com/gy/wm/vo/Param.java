package com.gy.wm.vo;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <类详细说明:任务参数，需要读取路径取得的参数，如tempalte路径下的
 * templates,已全部转换成字符串存到数据库中>
 *
 * @Author： Huanghai
 * @Version: 2016-09-20
 **/
@Component
public class Param implements Serializable {

    private static final long serialVersionUID = 1L;

    private String undefine;

    private String configs;

    private String regexFilter;

    private String templates;

    private String suffixFilter;

    private String seedUrls;

    private String clickRegex;

    private String protocolFilter;

    private String proxy;

    private String tags;

    private String categories;


    public String getUndefine() {
        return undefine;
    }

    public void setUndefine(String undefine) {
        this.undefine = undefine;
    }

    public String getConfigs() {
        return configs;
    }

    public void setConfigs(String configs) {
        this.configs = configs;
    }

    public String getRegexFilter() {
        return regexFilter;
    }

    public void setRegexFilter(String regexFilter) {
        this.regexFilter = regexFilter;
    }

    public String getTemplates() {
        return templates;
    }

    public void setTemplates(String templates) {
        this.templates = templates;
    }

    public String getSuffixFilter() {
        return suffixFilter;
    }

    public void setSuffixFilter(String suffixFilter) {
        this.suffixFilter = suffixFilter;
    }

    public String getSeedUrls() {
        return seedUrls;
    }

    public void setSeedUrls(String seedUrls) {
        this.seedUrls = seedUrls;
    }

    public String getClickRegex() {
        return clickRegex;
    }

    public void setClickRegex(String clickRegex) {
        this.clickRegex = clickRegex;
    }

    public String getProtocolFilter() {
        return protocolFilter;
    }

    public void setProtocolFilter(String protocolFilter) {
        this.protocolFilter = protocolFilter;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
