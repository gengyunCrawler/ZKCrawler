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

    //通过json传递
    public static String postJSON(String url, String jsonString) {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        String response = null;
        try {
            StringEntity params =new StringEntity(JSONUtil.object2JacksonString(jsonString));
            postMethod.setRequestHeader("content-type", "application/json");
            postMethod.setRequestEntity((RequestEntity) params);
            int status= httpClient.executeMethod(postMethod);
            response  = postMethod.getResponseBodyAsString();
        }catch (Exception ex) {
            // handle exception here
            ex.printStackTrace();
        } finally {
            if (postMethod != null)
                postMethod.releaseConnection();
        }
        return response;
    }

    public static void main(String[] args) {
        String url = "http://localhost:10080/api/getHbaseData";
        String url_json = "http://localhost:10080/api/testPostJSON";
        //传递参数
        Map<String,String> params = new HashMap<>();
        params.put("taskId1","c5b475b03652d36b5fdfe97022be0240");
        params.put("starRow1","c5b475b03652d36b5fdfe97022be0240|20161102013959|52a7c");
        params.put("size1","100");
        //传递JSON
        JSONObject postJSON = new JSONObject();
        postJSON.put("id","AE86");
        String jsonStr = JSONUtil.object2JacksonString(postJSON);

        String result = postMethod(url,params);
        String result_post = postJSON(url, jsonStr);
        System.out.println("post返回结果：" + result);
        System.out.println("post JSON的结果是：" + result_post);
    }
}
