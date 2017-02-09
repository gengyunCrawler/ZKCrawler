package cn.com.cloudpioneer.master.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by TianyuanPan on 1/11/2017.
 * <p>
 * 对象序列化与反序列化工具类
 */
public class ObjectSerializeUtils {

    /**
     * 把对象序列化为字节
     *
     * @param model 要序列化的对象
     * @return 成功返回序列化后的对象的字节数组, 否则返回null
     */
    public static byte[] serializeObjectToBytes(Object model) {

        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(model);
            return baos.toByteArray();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return null;
    }


    /**
     * 把字节数组转载为对象
     *
     * @param bytes 字节数组
     * @return 成功返回转载成的对象, 否则返回null。
     */
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
        return null;
    }

}
