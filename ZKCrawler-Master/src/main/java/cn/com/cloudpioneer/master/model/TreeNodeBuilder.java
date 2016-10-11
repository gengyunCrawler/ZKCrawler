package cn.com.cloudpioneer.master.model;

import cn.com.cloudpioneer.master.app.CuratorMaster;

import java.util.List;

/**
 * Created by TianyuanPan on 2016/10/9.
 */

public class TreeNodeBuilder {

    public static TreeNodeModel treeNodeBuilder(CuratorMaster master, TreeNodeModel parent, String path) {

        List<String> children;
        TreeNodeModel child;

        try {

            children = master.getChildren(path);
            if (children == null || children.size() == 0)
                return parent;
            for (String item : children) {
                child = new TreeNodeModel(path + "/" + item, item);
                child.setData(master.getNodeData(path + "/" + item));
                parent.addChild(treeNodeBuilder(master, child, path + "/" + item));
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return parent;
    }

}
