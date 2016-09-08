package cn.com.cloudpioneer.worker.service.impl;

import cn.com.cloudpioneer.worker.app.Worker;
import cn.com.cloudpioneer.worker.service.WorkerService;
import com.sun.istack.internal.Nullable;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/9/6.
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
}
