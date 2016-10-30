package com.gy.wm.timer;

import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Tianjinjin on 2016/10/9.
 */
public class NewsTimer{
    Timer timer;

    public NewsTimer() {
        timer = new Timer();
    }
    public static void main(String args[]) throws ParseException, InterruptedException {
        System.out.println("program run......");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = dateFormat.parse("2016-10-09 17:02:30");

        NewsTimer newsTimer=new NewsTimer();
        newsTimer.timer.schedule(new MyTask(),  5 * 1000);
        /*Thread.sleep(20000);
        newsTimer.timer.cancel();*/
        while(true){
            int ch = 0;
            try {
                ch = System.in.read();
                if(ch-'c'==0){
                    newsTimer.timer.cancel();//使用这个方法退出任务
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


class MyTask extends TimerTask {
    public void run() {
            System.out.println("Time's up!");
        //this.cancel();
    }
}
