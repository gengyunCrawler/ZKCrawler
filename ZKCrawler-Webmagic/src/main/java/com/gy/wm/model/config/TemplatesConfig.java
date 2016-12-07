package com.gy.wm.model.config;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 12/7/2016.
 */

/**
 * [
 * "template-01",
 * "template-02",
 * "template-03"
 * ]
 */
public class TemplatesConfig implements Serializable {

    private List<String> templates;

    public TemplatesConfig() {
        this.templates = new ArrayList<>();
    }

    public TemplatesConfig(String templates) {

        try {
            this.templates = JSONArray.parseArray(templates, String.class);
        } catch (Exception e) {
            this.templates = new ArrayList<>();
        }

    }

    public List<String> getTemplates() {
        return templates;
    }


    public void setTemplates(List<String> templates) {
        this.templates = templates;
    }


    public String toJSONString() {

        return JSONArray.toJSONString(this.templates);
    }
}
