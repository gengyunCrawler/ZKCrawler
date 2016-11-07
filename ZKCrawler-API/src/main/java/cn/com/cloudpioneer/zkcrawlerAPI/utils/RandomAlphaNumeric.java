package cn.com.cloudpioneer.zkcrawlerAPI.utils;

import java.util.UUID;

/**
 * <类详细说明>
 *
 * @Author： Huanghai
 * @Version: 2016-11-01
 **/
public class RandomAlphaNumeric {

    public static String randomStringOfLength(int length) {
        StringBuffer buffer = new StringBuffer();
        while (buffer.length() < length) {
            buffer.append(uuidString());
        }

        //this part controls the length of the returned string
        return buffer.substring(0, length);
    }


    private static String uuidString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    //测试
    public static void main(String[] args) {
        System.out.println(randomStringOfLength(5));
    }
}
