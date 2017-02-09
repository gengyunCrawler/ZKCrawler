package cn.com.cloudpioneer.master.service;

/**
 * Created by Administrator on 2016/10/8.
 */
public interface MasterService {


    boolean isLeader();

    String getMasterId();


    String workersTree();


    String tasksTree();


    String workersAndTasksTree();

}
