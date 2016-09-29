package cn.com.cloudpioneer.taskclient.app;

import cn.com.cloudpioneer.taskclient.chooser.ChooserPolicy;
import cn.com.cloudpioneer.taskclient.scheduler.SchedulePolicy;

/**
 * Created by Administrator on 2016/9/27.
 */
public class PolicyBuilder {


    public static final String CHOOSER_POLICY_IMPL_PACKAGE = "cn.com.cloudpioneer.taskclient.chooser.chooserPolicyImpl";
    public static final String SCHEDULE_POLICY_IMPL_PACKAG = "cn.com.cloudpioneer.taskclient.scheduler.schedulerImpl";


    public static SchedulePolicy schedulePolicyBuilder(String policyName) {

        try {
            return (SchedulePolicy) Class.forName(SCHEDULE_POLICY_IMPL_PACKAG + "." + policyName).newInstance();
        } catch (Exception e) {
            return null;
        }

    }

    public static ChooserPolicy chooserPolicyBuilder(String policyName) {

        try {
            return (ChooserPolicy) Class.forName(CHOOSER_POLICY_IMPL_PACKAGE + "." + policyName).newInstance();
        } catch (Exception e) {
            return null;
        }

    }

}
