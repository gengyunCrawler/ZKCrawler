package cn.com.cloudpioneer.worker.controller;

import cn.com.cloudpioneer.worker.service.WorkerService;
import cn.com.cloudpioneer.worker.utils.GetServletRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String test(ServletRequest request){

        return GetServletRequestBody.getBodyString(request);
       // return "" + workerService.statusWriteBack(null, "123456");
    }

}
