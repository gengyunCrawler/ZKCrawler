package cn.com.cloudpioneer.taskclient;
import cn.com.cloudpioneer.taskclient.chooser.TaskChooser;
import cn.com.cloudpioneer.taskclient.entity.TaskEntity;
import org.apache.curator.RetryPolicy;
import org.apache.curator.RetrySleeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

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

    private int tasksCreator(List<TaskEntity> taskEntityList){
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

    private boolean taskDelete(String node){
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

    public static void main(String[] args) throws Exception {
        TaskEntity taskEntity = new TaskEntity();
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

        /*RetryPolicy retryPolicy = new RetryPolicy() {
            @Override
            public boolean allowRetry(int i, long l, RetrySleeper retrySleeper) {
                return false;
            }
        };*/

        //创建 ZooKeeper 实例
        /*ZooKeeper zk = new ZooKeeper("192.168.229.128:2181,192.168.229.129:2181,192.168.229.130:2181", 30*1000, new Watcher() {
            public void process(WatchedEvent event) {
                if( event.getType().equals(Event.EventType.NodeDataChanged) ){
                    System.out.println("config is changed");
                    try {
                        updateConfig();
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/


        String path = "/test_path";
//        CuratorFramework client = CuratorFrameworkFactory.builder()
//                .connectString("192.168.229.130:2181")
//                .namespace("brokers")
//                .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
//                .connectionTimeoutMs(5000).build();        // 启动 上面的namespace会作为一个最根的节点在使用时自动创建
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181",new RetryNTimes(Integer.MAX_VALUE, 1000));
                client.start();           // 创建一个节点
                client.create().forPath("/head", new byte[0]);
                // 异步地删除一个节点
                client.delete().inBackground().forPath("/head");
                // 创建一个临时节点
                client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/head/child", new byte[0]);
                // 取数据
                client.getData().watched().inBackground().forPath("/test");
                // 检查路径是否存在
                client.checkExists().forPath(path);
                // 异步删除
                client.delete().inBackground().forPath("/head");
                // 注册观察者，当节点变动时触发
                client.getData().usingWatcher(new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        System.out.println("node is changed");
                    }
                }).inBackground().forPath("/test");
                // 结束使用
                client.close();

    }

}
