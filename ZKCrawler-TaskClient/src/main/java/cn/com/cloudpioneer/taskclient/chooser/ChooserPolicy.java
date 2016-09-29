package cn.com.cloudpioneer.taskclient.chooser;

import cn.com.cloudpioneer.taskclient.model.TaskEntity;

import java.util.List;

/**
 * Created by Tianjinjin on 2016/9/1.
 */
public interface ChooserPolicy {

    List<TaskEntity> chooser(int size);

    String getPolicyName();
}
