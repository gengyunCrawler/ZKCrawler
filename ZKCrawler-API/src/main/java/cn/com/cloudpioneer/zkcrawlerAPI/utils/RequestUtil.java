package cn.com.cloudpioneer.zkcrawlerAPI.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * http请求工具类
 * @Author Huanghai
 * @Version 2016-11-2 09:27:34
 */
public class RequestUtil {
    //通过参数
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
        String url = "http://localhost:10080/api/batchHbaseDataGet";

        Map<String,String> params = new HashMap<>();
        params.put("nextSign","0");
        params.put("size","100");

        String result = postMethod(url,params);
        System.out.println("post返回结果：\n" + result);

        /*String url = "http://localhost:10080/api/getHbaseData";
        Map<String,String> params = new HashMap<>();
        params.put("taskId","0ec153c8c4dae69ae48420426f3750f6");
        params.put("startRow","0ec153c8c4dae69ae48420426f3750f6|");
        params.put("size","100");

        String result = postMethod(url,params);
        System.out.println("post返回结果：" + result);*/

    }
}
