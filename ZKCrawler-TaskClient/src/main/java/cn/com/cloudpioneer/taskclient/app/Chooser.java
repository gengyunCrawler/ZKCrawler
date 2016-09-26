package cn.com.cloudpioneer.taskclient.app;

import cn.com.cloudpioneer.taskclient.chooser.ChooserPolicy;
import cn.com.cloudpioneer.taskclient.chooser.chooserPolicyImpl.LongTimeFirstChooserPolicy;
import cn.com.cloudpioneer.taskclient.model.TaskEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class Chooser {

    private ChooserPolicy policy;

    public Chooser() {
        policy = new LongTimeFirstChooserPolicy();
    }

    public Chooser(ChooserPolicy policy) {
        this.policy = policy;
    }

    public ChooserPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(ChooserPolicy policy) {
        this.policy = policy;
    }

    public List<TaskEntity> chooser(int size) {

        return this.policy.chooser(size);
    }
}
