package cn.com.cloudpioneer.worker.controller;

import cn.com.cloudpioneer.worker.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;

/**
 * Created by TianyuanPan on 2016/9/6.
 * <p>
 * 给 spring boot 提供对外的服务 API
 */

/**
 * 请求路径映射为 /worker
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


    /**
     * 获取 worker 当前的所有任务。
     *
     * @return json 字符串
     */
    @RequestMapping("currentTasks")
    public String currentTasks() {

        return workerService.getCurrentTasks();
    }


    /**
     * 任务回写接口，当 WebMagic 爬取的任务完成时，调用此接口把任务
     * 信息回写到数据库中。
     *
     * @param taskId：要回写的数据库ID
     * @return
     */
    @RequestMapping("taskWriteBack/{taskId}")
    public String taskWirteBack(@PathVariable("taskId") String taskId) {

        workerService.taskWriteBack(taskId);

        return "0";
    }

}
