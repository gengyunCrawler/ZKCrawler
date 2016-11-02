package cn.com.cloudpioneer.zkcrawlerAPI.model.elastic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 11/1/16.
 */
public class DataResultEntity implements ResultEntity {

    private class CmsDataWithScore {

        private String docId;
        private double score;
        private Object data;

        public CmsDataWithScore() {
            score = 0;
            data = new Object();
        }

        public CmsDataWithScore(String docId, double score, Object data) {

            this.docId = docId;
            this.score = score;
            this.data = data;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }
    }

    private final boolean status = true;
    private long total = 0;
    private int size = 0;
    private List<CmsDataWithScore> result;


    public DataResultEntity() {

        result = new ArrayList<>();
    }

    public void dataResultBuilder(String json) {

        JSONObject jsonObject = JSONObject.parseObject(json);

        total = jsonObject.getJSONObject("hits").getLongValue("total");
        if (total == 0) {
            return;
        }

        JSONArray jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");
        size = jsonArray.size();
        for (int i = 0; i < jsonArray.size(); i++) {

            result.add(
                    new CmsDataWithScore(
                            jsonArray.getJSONObject(i).getString("_id"),
                            jsonArray.getJSONObject(i).getDouble("_score"),
                            jsonArray.getJSONObject(i).getJSONObject("_source")
                    )
            );
        }


    }


    @Override
    public String getResultJsonString() {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("status", this.status);
        jsonObject.put("total", this.total);
        jsonObject.put("size", this.size);
        jsonObject.put("result", this.result);

        return jsonObject.toJSONString();
    }
}
