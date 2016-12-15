package com.gy.wm.wx;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.IncompleteArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2016/12/15.
 */
public class DownloadUrl{
    public static List<String> downWxUrls(int year,int month) throws IOException {
        String rootUrl = DownloadUrl.getWxRootUrl();
        CloseableHttpClient client = DownloadUrl.getInitHttpClient();
        String url = rootUrl+"/wx/artilceUrls/"+year+"/"+month;
        HttpGet get = new HttpGet(url);
       CloseableHttpResponse  response = client.execute(get);
        String json= EntityUtils.toString(response.getEntity());
        return JSONObject.parseArray(json,String.class);
    }

    public static List<String> downloadWxUrlByCurrentMonth(){
        return null;
    }
    private static CloseableHttpClient getInitHttpClient(){
        return HttpClients.custom().setDefaultCookieStore(null).build();
    }
    private static String getWxRootUrl(){
      String rootUrl =  PropertyResourceBundle.getBundle("config").getString("WX_ROOT_URL");
        if (rootUrl==null||rootUrl.equals("")){
            throw new IncompleteArgumentException("RootUrl must be indicated in config.properties");
        }
        return rootUrl;
    }
}
