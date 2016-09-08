package cn.com.cloudpioneer.worker.tasks;

import cn.com.cloudpioneer.worker.model.TaskModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/6.
 */
public class MyTasks {

    private Map<String, TaskModel> tasksMap;

    public MyTasks() {

        this.tasksMap = new HashMap<>();
    }

    public MyTasks(List<TaskModel> tasks) {

        this.tasksMap = new HashMap<>();

        for (TaskModel item : tasks)
            this.tasksMap.put(item.getEntity().getId(), item);

    }


    public void addTask(TaskModel task){

        tasksMap.put(task.getEntity().getId(), task);
    }

    public TaskModel removeTask(String id){

        return tasksMap.remove(id);
    }

    public int getMyTaskNumber(){

        return tasksMap.size();
    }

    public List<TaskModel> getTasks(){

        List<TaskModel> taskModels = new ArrayList<>();

        for (Map.Entry item : tasksMap.entrySet()){

            taskModels.add((TaskModel) item.getValue());
        }

        return taskModels;
    }
}
