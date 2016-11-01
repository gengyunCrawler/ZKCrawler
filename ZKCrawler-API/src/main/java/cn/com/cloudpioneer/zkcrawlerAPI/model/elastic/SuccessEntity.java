package cn.com.cloudpioneer.zkcrawlerAPI.model.elastic;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by TianyuanPan on 11/1/16.
 */
public class SuccessEntity implements ResultEntity {

    private final boolean status = true;
    private String message;

    public SuccessEntity() {

        message = "";
    }

    public SuccessEntity(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getResultJsonString() {
        return JSONObject.toJSONString(this);
    }
}
