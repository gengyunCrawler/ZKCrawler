package cn.com.cloudpioneer.master.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by TianyuanPan on 2016/10/8.
 */

@RestController
@RequestMapping("/master")
public class TestController {


    @RequestMapping("test")
    public String hello(){

        return "<br><h1>Hello World !!</h1>";

    }

}
