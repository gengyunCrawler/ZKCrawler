package com.gy.wm.plugins.newsExportPlugin.parse;

import java.util.List;

/**
 *
 * Created by Tijun on 2016/9/30.
 * @author  TijunWang
 */
public class HtmlField {
    private String fieldName;
    private List<String> xpaths;
    private String regex;
    private String occur;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }


    public List<String> getXpaths() {
        return xpaths;
    }

    public void setXpaths(List<String> xpaths) {
        this.xpaths = xpaths;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }



    public String getOccur() {
        return occur;
    }

    public void setOccur(String occur) {
        this.occur = occur;
    }
}
