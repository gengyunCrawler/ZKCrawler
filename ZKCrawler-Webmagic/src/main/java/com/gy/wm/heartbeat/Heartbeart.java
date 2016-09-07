package com.gy.wm.heartbeat;


import com.gy.wm.heartbeat.model.HeartbeatMsgModel;
import com.gy.wm.heartbeat.redisQueue.HeartbeatMsgQueue;
import com.gy.wm.util.ConfigUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by TianyuanPan on 6/15/16.
 */
public class Heartbeart implements Runnable {

    private int checkInterval;
    private HeartbeatMsgModel heartbeatMsgModel;
    private HeartbeatMsgQueue heartbeatMsgQueue;
    private boolean finish;
    private Lock myLock;


    public Heartbeart(HeartbeatMsgModel heartbeatMsgModel) {

        this.heartbeatMsgModel = heartbeatMsgModel;
        this.finish = false;
        this.heartbeatMsgQueue = new HeartbeatMsgQueue();
        this.myLock = new ReentrantLock();

        try {
            this.checkInterval = Integer.parseInt(ConfigUtils.getResourceBundle().getString("HEARTBEAT_CHECKINTERVAL"));
        } catch (NumberFormatException e) {
            this.checkInterval = 10;
            e.printStackTrace();
        }
    }

    public void setFinish(boolean finish) {
        myLock.lock();
        this.finish = finish;
//        System.out.println("finish = " + this.finish );
        myLock.unlock();
    }


    public void run() {

        while (true) {

            try {

                this.heartbeatMsgModel.setTime(System.currentTimeMillis());
                this.heartbeatMsgQueue.setMessage(this.heartbeatMsgModel);
                this.heartbeatMsgQueue.pushMessage();

            } catch (Exception e) {

                e.printStackTrace();
            }

            myLock.lock();
            if (finish) {
                myLock.unlock();
//                System.out.println("break =====>>> ");
                this.heartbeatMsgQueue.pushMessage();
                break;
            }
            myLock.unlock();

            try {

                Thread.sleep(this.checkInterval * 1000);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }
}
