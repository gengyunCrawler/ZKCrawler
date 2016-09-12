package com.gy.wm.plugins.topicPlugin.analysis;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/11/12.
 */
public class BaseAnalysisURL
{
    String url = null;
    String title = null;
    Date date = null;
    String html = null;
    String text;

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    BaseAnalysisURL(String url, String title, Date date, String html)
    {
        this.url = url;
        this.title = title;
        this.date = date;
        this.html = html;
    }

    public String getHtml()
    {
        return html;
    }

    public void setHtml(String html)
    {
        this.html = html;
    }

    public Date getDate()
    {
        return date;
    }

    public String getDateTime()
    {
        if (date==null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String str = sdf.format(date);
        return str;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getUrl()
    {
        return url;
    }
}
