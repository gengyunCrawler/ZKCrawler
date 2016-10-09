package cn.com.cloudpioneer.master.service.impl;

import cn.com.cloudpioneer.master.app.CuratorMaster;
import cn.com.cloudpioneer.master.model.TreeNodeBuilder;
import cn.com.cloudpioneer.master.model.TreeNodeModel;
import cn.com.cloudpioneer.master.service.MasterService;
import com.alibaba.fastjson.JSONArray;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by TianyuanPan on 2016/10/8.
 */

@Service
public class MasterServiceImpl implements MasterService {

    private static final Logger LOGGER = Logger.getLogger(MasterServiceImpl.class);

    @Override
    public boolean isLeader() {

        CuratorMaster master = CuratorMaster.getThisMaster();
        return master.isLeader();
    }

    @Override
    public String getMasterId() {
        CuratorMaster master = CuratorMaster.getThisMaster();
        return master.getMyId();
    }


    @Override
    public String workersTree() {

        CuratorMaster master = CuratorMaster.getThisMaster();

        JSONArray jsonArray = new JSONArray();

        List<String> workers;
        TreeNodeModel treeNode;

        try {

            workers = master.getChildren(CuratorMaster.PATH_ROOT_WORKERS);

            if (workers == null || workers.size() == 0) {
                LOGGER.info("===> workers list is null or workers list size is 0.");
                return jsonArray.toJSONString();
            }
            LOGGER.info("===> workers list size is: " + workers.size());
            for (String item : workers) {
                treeNode = new TreeNodeModel(CuratorMaster.PATH_ROOT_WORKERS + "/" + item, item, master.getNodeData(CuratorMaster.PATH_ROOT_WORKERS + "/" + item));
                jsonArray.add(TreeNodeBuilder.treeNodeBuilder(master, treeNode, CuratorMaster.PATH_ROOT_WORKERS + "/" + item));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return jsonArray.toJSONString();
        }

        return jsonArray.toJSONString();

    }


    @Override
    public String tasksTree() {

        CuratorMaster master = CuratorMaster.getThisMaster();

        JSONArray jsonArray = new JSONArray();

        List<String> tasks;
        TreeNodeModel treeNode;

        try {

            tasks = master.getChildren(CuratorMaster.PATH_ROOT_TASKS);

            if (tasks == null || tasks.size() == 0) {
                LOGGER.info("===> tasks list is null or tasks list size is 0.");
                return jsonArray.toJSONString();
            }

            LOGGER.info("===> tasks list size is: " + tasks.size());

            for (String item : tasks) {
                treeNode = new TreeNodeModel(CuratorMaster.PATH_ROOT_TASKS + "/" + item, item, master.getNodeData(CuratorMaster.PATH_ROOT_TASKS + "/" + item));
                jsonArray.add(TreeNodeBuilder.treeNodeBuilder(master, treeNode, CuratorMaster.PATH_ROOT_TASKS + "/" + item));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return jsonArray.toJSONString();
        }

        return jsonArray.toJSONString();

    }

    @Override
    public String workersAndTasksTree() {

        String workers, tasks;

        workers = workersTree();
        tasks = tasksTree();

        return "{\"workers\":" + workers + ",\"tasks\":" + tasks + "}";
    }
}
