package com.gy.wm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/5.
 */
public class TimeJudger {
    public static Date validTime(Date date, SimpleDateFormat sdf)   {
        String dateStr = sdf.format(date);
        String [] givenTime = dateStr.split("\\-");
        String currentTime  = sdf.format(new Date());
        String [] now = currentTime.split("\\-");
        if(Integer.parseInt(givenTime[0]) > Integer.parseInt(now[0]))   {
            return null;
        }else if(Integer.parseInt(givenTime[0]) == Integer.parseInt(now[0])
                &&Integer.parseInt(givenTime[1]) > Integer.parseInt(now[1]))   {
            return null;
        }else if(Integer.parseInt(givenTime[0]) == Integer.parseInt(now[0])
                &&Integer.parseInt(givenTime[1]) == Integer.parseInt(now[1])
                &&Integer.parseInt(givenTime[2]) > Integer.parseInt(now[2])) {
            return null;
        }else{
            return date;
        }
    }

    //测试
     public static void main(String[] args) throws ParseException {
         String s = "2016-07-05";
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         Date date = sdf.parse(s);
         Date result = new TimeJudger().validTime(date,sdf);
         String s1 = sdf.format(result);
         System.out.println(s1);
     }
}
