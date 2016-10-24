package com.gy.wm.service;


import org.apache.commons.httpclient.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * <类详细说明>
 *
 * @Author： Huanghai
 * @Version: 2016-10-18
 **/
public class ProxyTest {
    public static String proxyIP [] = {"120.55.180.55"};
    public static int proxyPort []  = {80};

    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {
        HttpHost target = new HttpHost("localhost", 443, "https");
        HttpHost proxy = new HttpHost("127.0.0.1", 8080, "http");

        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();
        HttpGet request = new HttpGet("/");
        request.setConfig(config);

        System.out.println("Executing request " + request.getRequestLine() + " to " + target + " via " + proxy);

        CloseableHttpResponse response = httpclient.execute(target, request);
        try {
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            EntityUtils.consume(response.getEntity());
        } finally {
            response.close();
        }
    } finally {
        httpclient.close();
    }
}
