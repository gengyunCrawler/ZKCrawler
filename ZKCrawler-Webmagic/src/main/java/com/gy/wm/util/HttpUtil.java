package com.gy.wm.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.util.Map;

/**
 * <类详细说明>
 *
 * @Author： Huanghai
 * @Version: 2016-09-09
 **/
public class HttpUtil {
    public static String postMethod(String url) {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        String response = null;

        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        try {
            int status = httpClient.executeMethod(postMethod);
            response = postMethod.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (postMethod != null)
                postMethod.releaseConnection();
        }
        return response;
    }

    public static void main(String[] args) {
        postMethod("http://localhost:10084/worker/taskWriteBack/{123}");
    }
}
