package cn.com.cloudpioneer.zkcrawlerAPI.model.elastic;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by TianyuanPan on 11/1/16.
 */
public class FailedEntity implements ResultEntity {

    private final boolean status = false;
    private String message;

    public FailedEntity(){

        message = "";
    }

    public FailedEntity(String message){

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
