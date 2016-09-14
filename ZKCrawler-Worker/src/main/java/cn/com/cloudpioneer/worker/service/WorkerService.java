package cn.com.cloudpioneer.worker.service;

import com.sun.istack.internal.Nullable;

/**
 * Created by TianyuanPan on 2016/9/6.
 * <p>
 * worker 服务接口，用于给 spring boot 框架
 * 提供 worker 服务。
 */


public interface WorkerService {

    boolean statusWriteBack(@Nullable String taskId);

    /**
     * 任务回写。
     *
     * @param taskId 要回写的任务ID
     */
    void taskWriteBack(String taskId);


    /**
     * 获取当前 worker 的所有任务。
     *
     * @return worker 当前的任务 JSON 数组。
     */
    String getCurrentTasks();
}
