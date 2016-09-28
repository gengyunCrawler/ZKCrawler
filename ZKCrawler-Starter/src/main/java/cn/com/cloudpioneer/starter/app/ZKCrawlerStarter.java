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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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


    static volatile int numberOfHost = 0;

    List<HostInfo> hostInfoList;
    List<ShellUtils> shellUtilsList;
    ExecutorService executorService;


    public ZKCrawlerStarter() {

        hostInfoList = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(50);

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

        numberOfHost = hostInfoList.size();

        return hostInfoList;
    }


    public List<HostInfo> initializeHostInfoFromString(String hostInfo) {

        hostInfoList = new ArrayList<>();

        JSONArray jsonArray = JSON.parseArray(hostInfo);
        for (Object item : jsonArray)
            hostInfoList.add(JSON.parseObject(item.toString(), HostInfo.class));

        numberOfHost = hostInfoList.size();
        return hostInfoList;

    }


    private static synchronized void subOfNumberOfHost(int deta){

        numberOfHost -= deta;
    }

    public static synchronized int getNumberOfHost(){

        return numberOfHost;
    }

    private void delay(int seconds, String waitName) {


        int i = seconds;

        while (i >= 0) {

            try {
                System.out.printf("\r");
                System.out.printf("===> waiting for " + waitName + " starting complete.  sec: " + i);
                Thread.sleep(1000);

            } catch (Exception e) {

            }

            i--;
        }

    }


    public void startZKCrawlerNodes() {

        getShell();

        for (ShellUtils item : shellUtilsList) {

            final ShellUtils myShell = item;

            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    System.out.println("Login host: " + myShell.getHostname());

                    System.out.println("\n===> now, starting Master.");
                    System.out.println(myShell.doExecuteShell(startMasterSh));
                    delay(10, "Master");
                    System.out.println("\n===> now, starting Webmagic.");
                    System.out.println(myShell.doExecuteShell(startWebmagicSh));
                    delay(20, "Webmagic");
                    System.out.println("\n===> now, starting Worker.");
                    System.out.println(myShell.doExecuteShell(startWorkerSh));
                    delay(10, "Worker");
                    System.out.println("\n===> now, starting TaskClient.");
                    System.out.println(myShell.doExecuteShell(startTaskClientSh));

                    System.out.println("Exit host: " + myShell.getHostname());

                    subOfNumberOfHost(1);
                }
            });

        }
    }
}
