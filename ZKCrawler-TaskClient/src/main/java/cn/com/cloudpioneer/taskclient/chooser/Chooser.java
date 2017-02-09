package cn.com.cloudpioneer.taskclient.chooser;

import cn.com.cloudpioneer.taskclient.chooser.chooserPolicyImpl.LongTimeFirstChooserPolicy;
import cn.com.cloudpioneer.taskclient.model.TaskEntity;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by TianyuanPan on 2016/9/26.
 */
public class Chooser {

    private static final Logger LOGGER = Logger.getLogger(Chooser.class);

    private ChooserPolicy policy;

    public Chooser() {
        policy = new LongTimeFirstChooserPolicy();
        LOGGER.info("===> chooser policy is: " + policy.getPolicyName());
    }

    public Chooser(ChooserPolicy policy) {
        this.policy = policy;
    }

    public ChooserPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(ChooserPolicy policy) {
        this.policy = policy;
        LOGGER.info("===> set chooser policy is: " + this.policy.getPolicyName());
    }

    public List<TaskEntity> chooser(int size) {

        return this.policy.chooser(size);
    }
}
