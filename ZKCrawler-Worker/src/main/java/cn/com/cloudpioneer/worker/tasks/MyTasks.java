package cn.com.cloudpioneer.worker.tasks;

import cn.com.cloudpioneer.worker.model.TaskModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 2016/9/6.
 * <p>
 * 此类是 worker 用于存储自己当前的所有任务。
 */
public class MyTasks {

    /**
     * 任务以字典的方式存储，检索字段是
     * 任务 id，数据是 TaskModel。
     */
    private Map<String, TaskModel> tasksMap;

    /**
     * 无参数构造方法。
     */
    public MyTasks() {

        this.tasksMap = new HashMap<>();
    }

    /**
     * 以任务模型列表为参数的构造方法。
     *
     * @param tasks 任务模型列表。
     */
    public MyTasks(List<TaskModel> tasks) {

        this.tasksMap = new HashMap<>();

        for (TaskModel item : tasks)
            this.tasksMap.put(item.getEntity().getId(), item);

    }


    /**
     * 添加任务到字典。
     *
     * @param task 要添加的任务。
     */
    public synchronized void addTask(TaskModel task) {

        tasksMap.put(task.getEntity().getId(), task);
    }


    /**
     * 从字典中移除任务。
     *
     * @param id 任务ID
     * @return 移除的任务，若任务不存在，则为 null。
     */
    public synchronized TaskModel removeTask(String id) {

        return tasksMap.remove(id);
    }

    /**
     * 获取任务数量。
     *
     * @return 任务数量。
     */
    public synchronized int getMyTaskNumber() {

        return tasksMap.size();
    }

    /**
     * 获取当前所有任务
     *
     * @return 任务列表。
     */
    public synchronized List<TaskModel> getTasks() {

        List<TaskModel> taskModels = new ArrayList<>();

        for (Map.Entry item : tasksMap.entrySet()) {

            taskModels.add((TaskModel) item.getValue());
        }

        return taskModels;
    }
}
