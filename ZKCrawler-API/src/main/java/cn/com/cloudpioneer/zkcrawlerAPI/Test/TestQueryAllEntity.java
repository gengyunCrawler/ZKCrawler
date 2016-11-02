package cn.com.cloudpioneer.zkcrawlerAPI.Test;

import cn.com.cloudpioneer.zkcrawlerAPI.model.elastic.QueryAllEntity;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by TianyuanPan on 11/1/16.
 */
public class TestQueryAllEntity {

    private static QueryAllEntity queryAllEntity;

    public static void main(String[] args) {

        String json = "{\"ruler\":\"must\",\"from\":0,\"size\":8,\"keywords\":[\"keyword-01\",\"keyword-02\"]}";

        queryAllEntity = new QueryAllEntity();
        queryAllEntity.contextBuilder(json);

        System.out.println("queryAllEntity:\n\t" + JSONObject.toJSONString(queryAllEntity));

    }
}
