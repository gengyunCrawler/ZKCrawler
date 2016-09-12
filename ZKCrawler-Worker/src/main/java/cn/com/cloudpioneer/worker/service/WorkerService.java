package cn.com.cloudpioneer.worker.service;

import com.sun.istack.internal.Nullable;

/**
 * Created by Administrator on 2016/9/6.
 */


public interface WorkerService {

    boolean statusWriteBack(@Nullable String taskId);

    void taskWriteBack(String taskId);

    String getCurrentTasks();
}
