package cn.com.cloudpioneer.taskclient.test;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tianjinjin on 2016/9/7.
 */
public class OtherTest {

    @Test
    public void testRegExp(){
        //task-1234567890-0000000024
        String path=null;
        String regexp="task-\\d{10}-\\d{10}";
        Pattern pattern = Pattern.compile(regexp);
        Matcher check = pattern.matcher(path);
        // 字符串是否与正则表达式相匹配
        boolean rs = check.matches();
        System.out.println(rs);

    }
}
