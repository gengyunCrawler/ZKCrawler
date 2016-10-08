package cn.com.cloudpioneer.master.model;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 2016/10/8.
 */
public class TreeNodeModel {

    private String path = "";
    private String name = "";
    private byte[] data;
    private List<TreeNodeModel> children;


    public TreeNodeModel(){

        data = new byte[0];
        children = new ArrayList<>();
    }

    public TreeNodeModel(String path, String name){

        this.name = name;
        this.path = path;
        data = new byte[0];
        children = new ArrayList<>();
    }

    public TreeNodeModel(String path, String name, byte[] data){

        this.name = name;
        this.path = path;
        this.data = data;
        children = new ArrayList<>();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public List<TreeNodeModel> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNodeModel> children) {
        this.children = children;
    }

    @Override
    public String toString() {

        return JSON.toJSONString(this);
    }


    public void addChild(TreeNodeModel child){

        this.children.add(child);
    }


    /*******************************************************

    public static void main(String[] args) {

        byte[] b1 = {0x12,0x56,0x12,0x0f};
        byte[] b2 = {0x01,0x04,0x09,0x1a,0x1f};

        TreeNodeModel t1,t2,t3,t4,t5;

        t1 = new TreeNodeModel();
        t1.setPath("/workers/t1");
        t1.setName("t1");
        t1.setData(b1);

        t2 = new TreeNodeModel();
        t2.setPath("/workers/t2");
        t2.setName("t1");
        t2.setData(b2);

        t3 = new TreeNodeModel();
        t3.setPath("/workers/t3");
        t3.setName("t3");
        t3.setData(b2);


        t4 = new TreeNodeModel();
        t4.setPath("/workers/t4");
        t4.setName("t4");
        t4.setData(b1);

        t5 = new TreeNodeModel();
        t5.setPath("/workers/t5");
        t5.setName("t5");
        t5.setData(b2);

        t2.addChild(t3);
        t2.addChild(t4);

        t1.addChild(t2);
        t1.addChild(t5);


        System.out.println("T1:  " + t1.toString());
        System.out.println("T2:  " + t2.toString());
        System.out.println("T3:  " + t3.toString());
        System.out.println("T4:  " + t4.toString());
        System.out.println("T5:  " + t5.toString());


    }

    /*********************************************************/


}
