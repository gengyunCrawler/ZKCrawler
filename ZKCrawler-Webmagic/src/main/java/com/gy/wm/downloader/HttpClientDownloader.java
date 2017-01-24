package com.gy.wm.downloader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
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
import java.util.ArrayList;
import java.util.Random;

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

    public static int getRandomNum()   {
        int min = 600;
        int max = 1500;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1)+min;
        return s;
    }

    public static void main(String[] args) throws Exception{

        ArrayList<String> list =new ArrayList<>();
        list.add("http://2.xiao8web.com/MicroSiteBuilderWeb/display/5351477360917086?from=singlemessage");
        list.add("http://2.xiao8web.com/MicroSiteBuilderWeb/display/5331477362487140");
        list.add("http://2.xiao8web.com/MicroSiteBuilderWeb/display/2521477361849626");
        list.add("http://dcwz.gog.cn/player.html?id=814741633848311808");
        list.add("http://3.xiao8web.com/MicroSiteDisplay/display/4701478165337570");
        for (String url : list) {
            int num = getRandomNum();
            System.out.println("得到的随机数："+num);
            for(int i=0; i<num; i++) {
                HttpGet get=new HttpGet(url);
                HttpResponse response= null;
                CloseableHttpClient client = null;
                try {
                    client= HttpClients.custom().build();
                    response = client.execute(get);
                    System.out.println("当前访问链接："+url+"\t"+response.getStatusLine().getStatusCode()+" 访问计数："+(i+1));
                    Thread.sleep(500);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    client.close();
                }
            }
        }
    }
}
