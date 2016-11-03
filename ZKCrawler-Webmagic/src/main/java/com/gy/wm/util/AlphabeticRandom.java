package com.gy.wm.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * <类详细说明:32位字母数字随机字符串生成类>
 *
 * @Author： Huanghai
 * @Version: 2016-09-14
 **/
public class AlphabeticRandom {
    private static SecureRandom random = new SecureRandom();
    public static String getString() {
        System.out.println(new BigInteger(130, random).toString(5));
        return new BigInteger(130, random).toString(5);
    }

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
        getString();

    }
}
