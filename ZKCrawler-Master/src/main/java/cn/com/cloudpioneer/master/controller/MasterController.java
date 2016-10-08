package cn.com.cloudpioneer.master.controller;

import cn.com.cloudpioneer.master.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2016/10/8.
 */
@RestController
@RequestMapping("/master")
public class MasterController {

    @Autowired
    private MasterService service;


    @RequestMapping("status")
    public String status() {

        return "false";
    }


    @RequestMapping("getWorkersTree")
    public String getWorkersTree(){

        return service.workersTree();

    }

}
