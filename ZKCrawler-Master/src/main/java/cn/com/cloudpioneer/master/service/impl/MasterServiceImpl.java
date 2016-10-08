package cn.com.cloudpioneer.master.service.impl;

import cn.com.cloudpioneer.master.app.CuratorMaster;
import cn.com.cloudpioneer.master.model.TreeNodeModel;
import cn.com.cloudpioneer.master.service.MasterService;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * Created by Administrator on 2016/10/8.
 */
public class MasterServiceImpl implements MasterService {


    @Override
    public boolean isLeader() {

        CuratorMaster master = CuratorMaster.getThisMaster();
        return master.isLeader();
    }


    @Override
    public String workersTree() {

        CuratorMaster master = CuratorMaster.getThisMaster();

        JSONArray jsonArray = new JSONArray();

        List<String> workers, workerChilren;
        TreeNodeModel treeNode, treeNodeChild;

        try {

            workers = master.getChildren(CuratorMaster.PATH_ROOT_WORKERS);

            if (workers == null || workers.size() == 0) {
                jsonArray.toJSONString();
            }

            for (String item : workers) {
                treeNode = new TreeNodeModel(CuratorMaster.PATH_ROOT_WORKERS + "/" + item, item, master.getNodeData(CuratorMaster.PATH_ROOT_WORKERS + "/" + item));
                workerChilren = master.getChildren(CuratorMaster.PATH_ROOT_WORKERS + "/" + item);
                if (workerChilren != null && workerChilren.size() != 0) {
                    for (String child : workerChilren) {

                        treeNodeChild = new TreeNodeModel(CuratorMaster.PATH_ROOT_WORKERS + "/" + item + "/" + child,
                                child, master.getNodeData(CuratorMaster.PATH_ROOT_WORKERS + "/" + item + "/" + child));

                        treeNode.addChild(treeNodeChild);

                    }

                }
                jsonArray.add(treeNode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return jsonArray.toJSONString();
        }

        return jsonArray.toJSONString();

    }


    @Override
    public String tasksTree() {

        return null;

    }
}
