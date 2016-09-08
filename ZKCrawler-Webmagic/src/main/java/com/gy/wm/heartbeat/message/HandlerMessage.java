package com.gy.wm.heartbeat.message;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public class HandlerMessage {

    private String message;
    private boolean status;

    private HandlerMessage(String message, boolean status){

        this.message = message;
        this.status = status;
    }

    private String getMessage() {

        return "{\"message\":\"" + this.message + "\", \"status\":" + this.status + "}";
    }

    public  static String getMessage(String message, boolean status){

        return new HandlerMessage(message, status).getMessage();
    }

}
