package cn.com.cloudpioneer.master.controller;

import cn.com.cloudpioneer.master.service.MasterService;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by TianyuanPan on 2016/10/8.
 */


@RestController
@RequestMapping("/master")
public class MasterController {

    private static final Logger LOGGER = Logger.getLogger(MasterController.class);


    @Autowired
    private MasterService masterService;


    @RequestMapping("status")
    public String status() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("masterId", masterService.getMasterId());
        jsonObject.put("isLeader", masterService.isLeader());

        return jsonObject.toJSONString();
    }


    @RequestMapping("getWorkersTree")
    public String getWorkersTree() {

        LOGGER.info("request getWorkersTree.");

        return masterService.workersTree();

    }


    @RequestMapping("getTasksTree")
    public String getTasksTree() {

        LOGGER.info("request getTasksTree.");

        return masterService.tasksTree();
    }


    @RequestMapping("getWorkersAndTasksTree")
    public String getWorkersAndTasksTree() {

        LOGGER.info("request getWorkersAndTasksTree.");

        return masterService.workersAndTasksTree();
    }

}

