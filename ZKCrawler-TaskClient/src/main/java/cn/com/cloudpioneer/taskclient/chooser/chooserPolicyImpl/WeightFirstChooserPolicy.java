package cn.com.cloudpioneer.taskclient.chooser.chooserPolicyImpl;

import cn.com.cloudpioneer.taskclient.chooser.ChooserPolicy;
import cn.com.cloudpioneer.taskclient.model.TaskEntity;

import java.util.List;

/**
 * Created by Tianjinjin on 2016/9/1.
 */
public class WeightFirstChooserPolicy implements ChooserPolicy {

    public List<TaskEntity> chooser(int size) {
        return null;
    }

    @Override
    public String getPolicyName() {
        return "WeightFirstChooserPolicy";
    }
}