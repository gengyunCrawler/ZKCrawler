package com.gy.wm.parse;

/**
 * Created by Administrator on 2016/9/30.
 */
public class HtmlFiled {
    private String fieldName;
    private String xpath;
    private String regex;
    private boolean muti;
    private String occur;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public boolean isMuti() {
        return muti;
    }

    public void setMuti(boolean muti) {
        this.muti = muti;
    }

    public String getOccur() {
        return occur;
    }

    public void setOccur(String occur) {
        this.occur = occur;
    }
}
