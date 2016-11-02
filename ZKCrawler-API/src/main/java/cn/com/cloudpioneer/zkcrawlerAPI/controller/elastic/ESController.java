package cn.com.cloudpioneer.zkcrawlerAPI.controller.elastic;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by TianyuanPan on 11/1/16.
 */

@RestController
@RequestMapping(value = "/central_resources", method = RequestMethod.POST)
public class ESController {

    @RequestMapping("cmsdata/test")
    public String test(){

        return "Hi!!!!!!!--------->> Hello world !!";
    }


}
