package com.gy.wm.plugins.topicPlugin.analysis;

/**
 * Created by hadoop on 2015/11/9.
 */

public class BaseURL  {

    private String url=null;
    private int statcode;
    private String rootUrl=null;
 //   private BaseWebPage webPage=null;
    private String fromUrl=null;
    private String text=null;
    private String html=null;
    private String title=null;
    private long crawltime;
    private long publishtime;
    private long depthfromSeed;
    private boolean tag;
    private long count;


    public BaseURL(String url,String title,String fromUrl,long date,long depthfromSeed)
    {

    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public int getStatcode()
    {
        return statcode;
    }

    public void setStatcode(int statcode)
    {
        this.statcode = statcode;
    }

    public String getRootUrl()
    {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl)
    {
        this.rootUrl = rootUrl;
    }



    public String getFromUrl()
    {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl)
    {
        this.fromUrl = fromUrl;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getHtml()
    {
        return html;
    }

    public void setHtml(String html)
    {
        this.html = html;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public long getCrawltime()
    {
        return crawltime;
    }

    public void setCrawltime(long crawltime)
    {
        this.crawltime = crawltime;
    }

    public long getPublishtime()
    {
        return publishtime;
    }

    public void setPublishtime(long publishtime)
    {
        this.publishtime = publishtime;
    }

    public long getDepthfromSeed()
    {
        return depthfromSeed;
    }

    public void setDepthfromSeed(long depthfromSeed)
    {
        this.depthfromSeed = depthfromSeed;
    }

    public boolean isTag()
    {
        return tag;
    }

    public void setTag(boolean tag)
    {
        this.tag = tag;
    }

    public long getCount()
    {
        return count;
    }

    public void setCount(long count)
    {
        this.count = count;
    }



}
