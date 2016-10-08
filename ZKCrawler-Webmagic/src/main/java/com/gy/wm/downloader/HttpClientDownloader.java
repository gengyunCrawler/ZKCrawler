package com.gy.wm.downloader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.UrlUtils;

import java.io.IOException;

/**
 * Created by Tijun on 2016/9/26.
 */
public class HttpClientDownloader implements Downloader {

    public Page download(Request request, Task task) {
        CloseableHttpClient client= HttpClients.custom().build();
        HttpGet get=new HttpGet(request.getUrl());
        HttpResponse response= null;
        try {
            response = client.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content= null;
        try {
            content = EntityUtils.toString(response.getEntity());
            System.out.println("html:---------------"+content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page page=new Page();
        page.setRawText("");
        page.setRawText(content);
        page.setHtml(new Html(""));
        page.setHtml(new Html(UrlUtils.fixAllRelativeHrefs(content, request.getUrl())));
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return page;

    }

    @Override
    public void setThread(int threadNum) {

    }
}
