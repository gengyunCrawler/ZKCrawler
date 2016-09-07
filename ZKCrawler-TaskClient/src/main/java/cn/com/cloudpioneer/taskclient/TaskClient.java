package cn.com.cloudpioneer.taskclient;
import cn.com.cloudpioneer.taskclient.chooser.TaskChooser;
import cn.com.cloudpioneer.taskclient.entity.TaskEntity;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * Created by Tianjinjin on 2016/9/1.
 */
public class TaskClient {

    //TaskClient的配置
    private Map<String,Object> config;

    //TaskClient的Id
    private String myId;

    //ZK的客户端对象
    private CuratorFramework client;

    //主节点选择器
    private final LeaderSelector leaderSelector = null;

    //任务的缓存器
    private final PathChildrenCache tasksCache=null;

    //任务实体TaskEntity列表
    private List<TaskEntity> taskEntityList;

    //任务选择器成员
    private TaskChooser taskChooser;

    //领导锁
    private CountDownLatch leaderLatch;

    //关闭锁
    private CountDownLatch closeLatch;

    //领导监听器，是个接口，需要自己实现
    private CuratorListener leaderListener;

    //任务缓存监听器，是个接口，需要自己实现
    private PathChildrenCacheListener tasksCacheListener;

    //未知错误处理监听器，是个接口，需自己实现
    private UnhandledErrorListener errorsListener;

    //askClient的逻辑实现监听器，是个接口，自己实现逻辑
    private CuratorListener performListener;



    public TaskClient() {
    }

    public TaskClient(String configFileName) {
    }

    public TaskClient(TaskChooser chooser) {
    }

    public TaskClient(String configFileName,TaskChooser chooser) {
    }

    public void setConfig(String configFileName){

    }

    public Object getConfig(String key){
        return null;
    }

    private Map<String, Object> initConfig(String configFileName){
        return null;
    }


    /**
     * 创建任务，把任务添加到task节点中
     * @param taskEntityList
     * @return
     */
    private int tasksCreator(List<TaskEntity> taskEntityList) throws Exception {
        String path;
        for(TaskEntity task:taskEntityList){
            String data =task.toString();
            client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/tasks/task" + "-" + task.getId() + "-", data.getBytes());
            System.out.println(client.getChildren().forPath("/tasks"));
        }
        return 0;
    }

    private List<TaskEntity> tasksGeter(int size){
        return null;
    }

    private int tasksWriteBack(List<TaskEntity> taskEntityList){
        return 0;
    }

    private boolean testMaster(){
        return false;
    }


    /**
     * 删除tasks下已完成的任务节点
     * @param node
     * @return
     */
    private boolean taskDelete(String node) throws Exception {

        client.delete().forPath("/tasks/task3");
        return false;
    }

    public void startZK(){

    }

    public void runForTC(){

    }

    public void awaitLeadership(){

    }

    public boolean isLeader(){
        return false;
    }

    public void takeLeadership(CuratorFramework client){

    }

    public void stateChanged(CuratorFramework client,ConnectionState newState){

    }

    public void close(){

    }

    public void listenTaskNode() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //ExecutorService pool = Executors.newCachedThreadPool();
        //设置节点的cache
        TreeCache treeCache = new TreeCache(client, "/tasks");
        //设置监听器和处理过程
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(org.apache.curator.framework.CuratorFramework client, TreeCacheEvent event) throws Exception {
                String regexp="/tasks/task-\\d{10}-\\d{10}";
                Pattern pattern = Pattern.compile(regexp);
                //System.out.println(event.getType());
                ChildData data = event.getData();
                switch (event.getType()) {
                    case NODE_ADDED:
                        String path=event.getData().getPath();
                        if(pattern.matcher(path).matches()){
                            client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path+"/status", "1".getBytes());
                            System.out.println(client.getChildren().forPath("/tasks"));
                        }
                        System.out.println("NODE_ADDED : " + data.getPath() + "  数据:" + new String(data.getData()));
                        break;
                    case NODE_REMOVED:
                        System.out.println("NODE_REMOVED : " + data.getPath());
                        break;
                    case NODE_UPDATED:
                        System.out.println("NODE_UPDATED : " + data.getPath() + "  数据:" + new String(data.getData()));
                        break;
                    default:
                        break;
                }
                if (data == null) {
                    System.out.println("data is null : " + event.getType());
                }
            }
        });
        //开始监听
        treeCache.start();

        countDownLatch.await();
    }

    public static void main(String[] args) throws Exception {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId("1234567890");
        taskEntity.setCompleteTimes(2);
        taskEntity.setDeleteFlag(true);
        taskEntity.setCostLastCrawl(20);
        taskEntity.setCycleRecrawl(40);
        taskEntity.setDepthCrawl(3);
        taskEntity.setIdUser(0144552);
        taskEntity.setName("dfsaf");
        taskEntity.setPathTemplates("agvb");
        taskEntity.setSeedUrls("rety");
        taskEntity.setPathRegexFilter("trh");
        taskEntity.setPathRegexFilter("ngjrd");
        taskEntity.setThreads(3);
        taskEntity.setWorkNum(5);
        taskEntity.setSeedUrls("url1,url2,url3");

        List<TaskEntity> taskEntityList = new ArrayList<TaskEntity>();
        taskEntityList.add(taskEntity);

        TaskClient taskClient=new TaskClient();

        taskClient.client = CuratorFrameworkFactory.newClient("192.168.229.130:2181", new RetryNTimes(Integer.MAX_VALUE, 1000));
        taskClient.client.start();
        taskClient.tasksCreator(taskEntityList);

    }

}
