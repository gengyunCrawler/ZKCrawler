package com.gy.wm.model;

import org.springframework.stereotype.Component;

/**
 * <类详细说明:如果通过Xpath拿到的字段不准确，通过该实体配置进行字段的裁剪，直到准确再进行推送>
 *
 * @Author： Huanghai
 * @Version: 2016-11-28
 **/
@Component
public class FieldCroperEntity {
    String tid;
    String cropRule;    //提取规则，使用json存储{"soureName":{"preffix":"来源：|新闻来源：|来自：","suffix":"xx"}}

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getCropRule() {
        return cropRule;
    }

    public void setCropRule(String cropRule) {
        this.cropRule = cropRule;
    }
}