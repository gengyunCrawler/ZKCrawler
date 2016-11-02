package cn.com.cloudpioneer.zkcrawlerAPI.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * http请求工具类
 * @Author Huanghai
 * @Version 2016-11-2 09:27:34
 */
public class RequestUtil {
    public static String postMethod(String url, Map<String, String> params) {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        String response = null;
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        for (Map.Entry<String, String> entry : params.entrySet()) {
        	postMethod.addParameter(entry.getKey(), entry.getValue());
        }
        try {
            int status = httpClient.executeMethod(postMethod);
            System.out.println("**************************状态码：***************************" + status);
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
        String url = "http://localhost:10080/api/getHbaseData";
        Map<String,String> params = new HashMap<>();
        params.put("taskId","c5b475b03652d36b5fdfe97022be0240");
        params.put("startRow","c5b475b03652d36b5fdfe97022be0240|20161102013959|52a7c");
        params.put("size","100");

        String result = postMethod(url,params);
        System.out.println("post返回结果：" + result);
    }
}
