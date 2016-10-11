package cn.com.cloudpioneer.taskclient.service;

/**
 * Created by Administrator on 2016/9/27.
 */
public interface TaskClientService {

    String setChooserPolicy(String policyName);

    String getChooserPolicy();

    String setSchedulerPolicy(String policyName);

    String getSchedulerPolicy();
}
