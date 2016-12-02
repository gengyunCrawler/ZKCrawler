package com.gy.wm.util;

/**
 * <类详细说明:生成MD5散列字段>
 *
 * @Author： Huanghai
 * @Version: 2016-11-08
 **/
public class MD5 {
    public static String generateMD5(String md5) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
                byte[] array = md.digest(md5.getBytes());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < array.length; ++i) {
                    sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
                }
                return sb.toString();
            } catch (java.security.NoSuchAlgorithmException e) {
            }
            return null;
        }


}
