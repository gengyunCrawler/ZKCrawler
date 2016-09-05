package cn.com.cloudpioneer.taskclient.chooser;

import cn.com.cloudpioneer.taskclient.entity.TaskEntity;

import java.util.List;

/**
 * Created by Tianjinjin on 2016/9/1.
 */
public interface TaskChooser {
    public List<TaskEntity> chooser(int size);
}
