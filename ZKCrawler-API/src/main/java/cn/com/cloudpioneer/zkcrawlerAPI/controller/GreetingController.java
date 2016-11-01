package cn.com.cloudpioneer.zkcrawlerAPI.controller;

import cn.com.cloudpioneer.zkcrawlerAPI.utils.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by TianyuanPan on 6/29/16.
 */

@RestController
@Scope("prototype")
public class GreetingController {

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {

        model.addAttribute("name", name);
        return "greeting";
    }

    /**
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    public String getHbaseData(@RequestBody JSONObject jsonObject)    {
        return JSONUtil.object2JacksonString(jsonObject);
    }

}
