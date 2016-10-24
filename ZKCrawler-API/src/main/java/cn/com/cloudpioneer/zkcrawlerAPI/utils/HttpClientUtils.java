package cn.com.cloudpioneer.zkcrawlerAPI.utils;


import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/6.
 */
public class HttpClientUtils {

    //是否启用代理
    public static boolean isProxy = false;
    public static String proxyHost = "";
    public static int proxyPort;

    public static int connectionTimeOut = 6000;
    public static int requestTimeOut = 6000;
    public static int readTimeOut = 30000;

    private static final Logger LOGGER = Logger.getLogger(HttpClientUtils.class);

    /**
     * @param url
     * @param jsonData
     * @return
     */
    public static String jsonPostRequest(String url, String jsonData) {

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(readTimeOut)
                .setConnectTimeout(connectionTimeOut)
                .setConnectionRequestTimeout(requestTimeOut)
                .build();


        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig).build();

        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        StringEntity stringEntity;

        try {
            stringEntity = new StringEntity(jsonData, "UTF-8");
            postRequest.setEntity(stringEntity);
            LOGGER.info("executing request: " + postRequest.getURI());
            CloseableHttpResponse response = httpClient.execute(postRequest);

            try {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    LOGGER.info("Response Conde: " + response.getStatusLine().getStatusCode());
                    return EntityUtils.toString(responseEntity, "UTF-8");
                } else
                    return null;

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;

            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 关闭连接,释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * @param url
     * @param data
     * @return
     */
    public static String getRequest(String url, Map<String, String> data) {

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(readTimeOut)
                .setConnectTimeout(connectionTimeOut)
                .setConnectionRequestTimeout(requestTimeOut)
                .build();


        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig).build();

        String queryParam = "?";
        for (Map.Entry item : data.entrySet()) {
            queryParam += item.getKey() + "=" + item.getKey() + "&";
        }

        HttpGet getRequest = new HttpGet(url + queryParam);
        try {
            LOGGER.info("executing request " + getRequest.getURI());
            CloseableHttpResponse response = httpClient.execute(getRequest);
            try {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {

                    LOGGER.info("Response Conde: " + response.getStatusLine().getStatusCode());
                    return EntityUtils.toString(responseEntity, "UTF-8");

                } else
                    return null;

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;

            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 关闭连接,释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(HttpClientUtils.jsonPostRequest("http://localhost:8080/startTask", "{\"id\":\"454564846\",\"idUser\":\"4520\"}"));
    }

}
