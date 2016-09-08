package com.gy.wm.util;

/**
 * Created by Administrator on 2016/6/23.
 */
public class GetDomain {
    public static String getDomain(String url)  {
        String [] array_all =url.split("//");
        String part_last = array_all[1];
        String [] array_last = part_last.split("/");
        String result = array_last[0];
        return  result/*.replace("www.","")*/;
    }

    //test
    public static void main(String[] args) {
        System.out.println(getDomain("http://www.gog.cn/"));
    }
}
