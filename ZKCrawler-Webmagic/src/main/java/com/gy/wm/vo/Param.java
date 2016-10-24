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

    List<String> configs;

    List<String> regexFilter;

    List<String> templates;

    List<String> suffixFilter;

    List<String> seedUrls;

    List<String> clickRegex;

    List<String> protocolFilter;

    List<String> proxy;


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

    public List<String> getProxy() {
        return proxy;
    }

    public void setProxy(List<String> proxy) {
        this.proxy = proxy;
    }
}
