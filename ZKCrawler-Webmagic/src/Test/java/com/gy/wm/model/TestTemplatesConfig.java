package com.gy.wm.model;

import com.alibaba.fastjson.JSONArray;
import com.gy.wm.model.config.TemplatesConfig;
import org.junit.Test;

import java.util.List;

/**
 * Created by TianyuanPan on 12/7/2016.
 */
public class TestTemplatesConfig {


    @Test
    public void test(){

        TemplatesConfig templatesConfig = new TemplatesConfig();

        String s = "[\"t-01\",\"t-02\",\"t-03\",\"t-04\", \"t-05\"]";

        List<String> ss = JSONArray.parseArray(s, String.class);

        System.out.println("ss： " + ss);

        templatesConfig = new TemplatesConfig(s);

        System.out.println(templatesConfig.toJSONString());

    }
}
