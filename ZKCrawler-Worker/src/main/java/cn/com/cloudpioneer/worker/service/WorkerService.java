package cn.com.cloudpioneer.worker.service;

import cn.com.cloudpioneer.worker.app.Worker;
import com.sun.istack.internal.Nullable;

/**
 * Created by Administrator on 2016/9/6.
 */


public interface WorkerService {

        boolean statusWriteBack(@Nullable Worker worker, @Nullable String taskId);
}
