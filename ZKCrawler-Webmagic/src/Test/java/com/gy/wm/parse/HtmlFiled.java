package com.gy.wm.parse;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class HtmlFiled {
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


    public List<String> getXpath() {
        return xpaths;
    }

    public void setXpath(List<String> xpath) {
        this.xpaths = xpath;
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
