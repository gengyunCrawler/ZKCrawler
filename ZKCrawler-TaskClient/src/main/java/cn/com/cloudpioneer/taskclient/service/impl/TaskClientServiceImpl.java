package cn.com.cloudpioneer.taskclient.service.impl;

import cn.com.cloudpioneer.taskclient.scheduler.PolicyBuilder;
import cn.com.cloudpioneer.taskclient.app.TaskClient;
import cn.com.cloudpioneer.taskclient.chooser.ChooserPolicy;
import cn.com.cloudpioneer.taskclient.scheduler.SchedulePolicy;
import cn.com.cloudpioneer.taskclient.service.TaskClientService;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/9/27.
 */

@Service
public class TaskClientServiceImpl implements TaskClientService {

    private TaskClient taskClient = TaskClient.getThisTaskClient();

    @Override
    public String setChooserPolicy(String policyName) {

        ChooserPolicy policy = PolicyBuilder.chooserPolicyBuilder(policyName);

        if (policy == null)
            return "policy not impl.";
        taskClient.getTasksChooser().setPolicy(policy);

        return taskClient.getTasksChooser().getPolicy().getPolicyName();
    }

    @Override
    public String getChooserPolicy() {
        return taskClient.getTasksChooser().getPolicy().getPolicyName();
    }

    @Override
    public String setSchedulerPolicy(String policyName) {

        SchedulePolicy policy = PolicyBuilder.schedulePolicyBuilder(policyName);
        if (policy == null)
            return "policy not impl";
        taskClient.getWorkersScheduler().setPolicy(policy);

        return taskClient.getWorkersScheduler().getPolicy().getPolicyName();
    }

    @Override
    public String getSchedulerPolicy() {
        return taskClient.getWorkersScheduler().getPolicy().getPolicyName();
    }
}
