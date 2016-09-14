package cn.com.cloudpioneer.worker.service.impl;

import cn.com.cloudpioneer.worker.app.Worker;
import cn.com.cloudpioneer.worker.service.WorkerService;
import com.alibaba.fastjson.JSONArray;
import com.sun.istack.internal.Nullable;
import org.springframework.stereotype.Service;

/**
 * Created by TianyuanPan on 2016/9/6.
 * <p>
 * 接口 workerService 的实现类。
 */

@Service
public class WorkerServiceImpl implements WorkerService {

    private Worker worker = Worker.getThisWorker();

    @Override
    public boolean statusWriteBack(@Nullable String taskId) {
        return false;
    }

    @Override
    public void taskWriteBack(String taskId) {

        worker.myTaskWirteBack(taskId);

    }


    @Override
    public String getCurrentTasks() {

        return JSONArray.toJSONString(worker.getMyTasks());
    }
}
