package cn.com.cloudpioneer.zkcrawlerAPI.model.elastic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 11/1/16.
 */
public class QueryAllEntity implements QueryEntity {
    private String ruler = "must";
    private int from = 0;
    private int size = 10;
    private List<String> keywords;


    public QueryAllEntity() {
        keywords = new ArrayList<>();
    }

    public QueryAllEntity(String json) {

        keywords = new ArrayList<>();
        contextBuilder(json);
    }

    public void contextBuilder(String json) {

        JSONObject jsonObject = JSONObject.parseObject(json);
        ruler = jsonObject.getString("ruler");
        from = jsonObject.getIntValue("from");
        size = jsonObject.getIntValue("size");
        JSONArray jsonArray = jsonObject.getJSONArray("keywords");

        for (int i = 0; i < jsonArray.size(); i++)
            keywords.add(jsonArray.getString(i));

    }

    @Override
    public String getEntityString() {
        return null;
    }

}
