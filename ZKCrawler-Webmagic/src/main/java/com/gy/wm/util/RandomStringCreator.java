package com.gy.wm.util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * <类详细说明:32位字母数字随机字符串生成类>
 *
 * @Author： Huanghai
 * @Version: 2016-09-14
 **/
public class RandomStringCreator {
    private static SecureRandom random = new SecureRandom();
    public static String getString() {
        return new BigInteger(130, random).toString(32);
    }
}
