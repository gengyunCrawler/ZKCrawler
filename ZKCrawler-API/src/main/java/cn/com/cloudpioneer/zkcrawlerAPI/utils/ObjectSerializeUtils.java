package cn.com.cloudpioneer.zkcrawlerAPI.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class ObjectSerializeUtils {

    public static byte[] serializeObjectToBytes(Object model) {

        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(model);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return new byte[0];
    }


    public static Object buildObjectFromBytes(byte[] bytes) {

        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return new byte[0];
    }

}