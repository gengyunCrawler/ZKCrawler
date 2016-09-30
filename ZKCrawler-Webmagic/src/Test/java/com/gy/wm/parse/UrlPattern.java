package com.gy.wm.parse;

/**
 * Created by Administrator on 2016/9/30.
 */
public class UrlPattern {
    private String regex;
    private String type;

    public UrlPattern(){}

    public  UrlPattern(String regex,String type){
        this.regex=regex;
        this.type=type;
    }
    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
