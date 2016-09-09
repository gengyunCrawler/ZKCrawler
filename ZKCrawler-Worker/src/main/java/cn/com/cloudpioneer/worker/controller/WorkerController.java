package cn.com.cloudpioneer.worker.controller;

import cn.com.cloudpioneer.worker.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;

/**
 * Created by Administrator on 2016/9/6.
 */

@RestController
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @RequestMapping("test")
    public String test(ServletRequest request) {

        //return GetServletRequestBody.getBodyString(request);
        return "" + workerService.statusWriteBack("123456");
    }

    @RequestMapping("currentTasks")
    public String currentTasks() {

        return workerService.getCurrentTasks();
    }

    @RequestMapping("taskWriteBack/{taskId}")
    public String taskWirteBack(@PathVariable("taskId") String taskId) {

        workerService.taskWriteBack(taskId);

        return "ok.";
    }

}
