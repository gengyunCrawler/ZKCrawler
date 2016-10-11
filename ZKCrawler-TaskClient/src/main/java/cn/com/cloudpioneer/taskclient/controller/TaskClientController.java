package cn.com.cloudpioneer.taskclient.controller;

import cn.com.cloudpioneer.taskclient.service.impl.TaskClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by TianyuanPan on 2016/9/27.
 */

@RestController
@RequestMapping("/taskClient")
public class TaskClientController {

    @Autowired
    private TaskClientServiceImpl taskClientService;

    @RequestMapping("getChooserPolicy")
    public String getChooserPolicy() {

        return taskClientService.getChooserPolicy();
    }


    @RequestMapping("setChooserPolicy/{policyName}")
    public String setChooserPolicy(@PathVariable("policyName") String policyName) {

        taskClientService.setChooserPolicy(policyName);

        return taskClientService.getChooserPolicy();
    }

    @RequestMapping("getSchedulerPolicy")
    public String getSchedulerPolicy() {

        return taskClientService.getSchedulerPolicy();
    }


    @RequestMapping("setSchedulerPolicy/{policyName}")
    public String setSchedulerPolicy(@PathVariable("policyName") String policyName) {

        taskClientService.setSchedulerPolicy(policyName);

        return taskClientService.getSchedulerPolicy();
    }

}
