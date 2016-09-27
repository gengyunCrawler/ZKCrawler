package cn.com.cloudpioneer.starter.app;

import cn.com.cloudpioneer.starter.model.HostInfo;
import cn.com.cloudpioneer.starter.utils.ShellUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ZKCrawlerStarter {


    private static String startMasterSh;
    private static String startWebmagicSh;
    private static String startWorkerSh;
    private static String startTaskClientSh;

    static {

        startMasterSh = ResourceBundle.getBundle("config").getString("SCRIPT_START_MASTER");
        startWebmagicSh = ResourceBundle.getBundle("config").getString("SCRIPT_START_WEBMAGIC");
        startWorkerSh = ResourceBundle.getBundle("config").getString("SCRIPT_START_WORKER");
        startTaskClientSh = ResourceBundle.getBundle("config").getString("SCRIPT_START_TASK_CLIENT");
    }




    List<HostInfo> hostInfoList;
    List<ShellUtils> shellUtilsList;


    public ZKCrawlerStarter() {

        hostInfoList = new ArrayList<>();

    }

    public List<ShellUtils> getShell() {
        shellUtilsList = new ArrayList<>();
        ShellUtils shell;

        for (HostInfo item : hostInfoList) {
            shell = ShellUtils.getShellUtils(item.getUsername(), item.getPassword(), item.getHostname(), item.getPort());
            shellUtilsList.add(shell);
        }

        return shellUtilsList;
    }


    public List<HostInfo> initializeHostInfoFromFile(String fileName) {


        try {
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String jsonString = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                jsonString += line;
            }

            hostInfoList = initializeHostInfoFromString(jsonString);

        } catch (IOException e) {

            e.printStackTrace();
        }

        return hostInfoList;
    }


    public List<HostInfo> initializeHostInfoFromString(String hostInfo) {

        hostInfoList = new ArrayList<>();

        JSONArray jsonArray = JSON.parseArray(hostInfo);
        for (Object item : jsonArray)
            hostInfoList.add(JSON.parseObject(item.toString(),HostInfo.class));

        return hostInfoList;

    }


    public void startZKCrawlerNodes() {

        getShell();

        for (ShellUtils item : shellUtilsList) {

            item.openPerformShell();

            System.out.println(item.doExecuteShell(startMasterSh));
            System.out.println(item.doExecuteShell(startWebmagicSh));
            System.out.println(item.doExecuteShell(startWorkerSh));
            System.out.println(item.doExecuteShell(startTaskClientSh));

            item.closePerformShell();

        }

    }


}
