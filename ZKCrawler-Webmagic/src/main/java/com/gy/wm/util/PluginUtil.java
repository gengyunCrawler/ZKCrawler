package com.gy.wm.util;

import us.codecraft.webmagic.Page;

import java.util.ResourceBundle;

/**
 * <类详细说明:通过反射取得具体解析插件>
 *
 * @Author： Huanghai
 * @Version: 2016-09-14
 **/
public class PluginUtil {
    private static final String pluginName = ResourceBundle.getBundle("config.properties").getString("pluginName");

    public static Object exactPluginObject(Page page) {
        Object object = null;
        try {
            Class c = Class.forName(pluginName);
            object = c.newInstance();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return object;
    }
}
