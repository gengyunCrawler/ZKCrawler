package cn.com.cloudpioneer.worker.utils;

import javax.servlet.ServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/9/6.
 */
public class GetServletRequestBody {

    public static String getBodyString(ServletRequest request) {


        InputStream inputStream = null;
        int length = request.getContentLength();
        byte[] bytes = new byte[length];


        try {
            inputStream = request.getInputStream();
            inputStream.read(bytes, 0, length);
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {

            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] getBodyBytes(ServletRequest request) {

        InputStream inputStream = null;
        int length = request.getContentLength();
        byte[] bytes = new byte[length];


        try {
            inputStream = request.getInputStream();
            inputStream.read(bytes, 0, length);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {

            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
