package cn.com.cloudpioneer.worker.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Tianjinjin on 2016/9/1.
 */
public class TaskEntity implements Serializable {

    //爬虫的任务id，唯一
    private String id;

    //用户id，表明任务属于哪个用户
    private long idUser;

    //爬虫的任务名
    private String name;

    //爬虫任务描述
    private String remark;

    //爬取的种子入口列表，格式为JSON数组
    private String seedUrls;

    //存放模板文件路径
    private String pathTemplates;

    //标签文件路径，存放文件夹地址
    private String pathTag;

    //种子文件路径
    private String pathSeeds;

    //配置文件路径
    private String pathConfigs;

    //点击正则配置文件路径
    private String pathClickRegex;

    //正则过滤配置文件路径
    private String pathRegexFilter;

    //协议过滤配置文件地址
    private String pathProtocolFilter;

    //后缀过滤配置文件地址
    private String pathSuffixFilter;

    //爬虫任务类型
    private int type;

    //爬取深度
    private int depthCrawl;

    //动态爬取深度
    private int depthDynamic;

    //爬取遍数,默认为0
    private int pass = 0;

    //任务权重，默认为0
    private int weight = 0;

    //任务爬取时的线程数，大于0，默认为1
    private int threads = 1;

    //爬取任务完成的次数
    private int completeTimes;

    //任务爬取周期 单位为小时，默认为72
    private int cycleRecrawl = 72;

    //数字标识任务状态，默认为0
    private int status = 0;

    //任务的软删除标识，为true时奴能进行爬取，默认为false
    private boolean deleteFlag;

    //爬取任务启动的时间
    private Date timeStart;

    //爬取任务的停止时间
    private Date timeStop;

    //任务最后一次爬取的时间
    private Date timeLastCrawl;

    //上次爬取消耗的时间，单位为分钟
    private int costLastCrawl;

    //调度类型
    private int scheduleType;

    //每个任务对应的work数量
    private int workNum;

    //任务创建的时间
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSeedUrls() {
        return seedUrls;
    }

    public void setSeedUrls(String seedUrls) {
        this.seedUrls = seedUrls;
    }

    public String getPathTemplates() {
        return pathTemplates;
    }

    public void setPathTemplates(String pathTemplates) {
        this.pathTemplates = pathTemplates;
    }

    public String getPathTag() {
        return pathTag;
    }

    public void setPathTag(String pathTag) {
        this.pathTag = pathTag;
    }

    public String getPathSeeds() {
        return pathSeeds;
    }

    public void setPathSeeds(String pathSeeds) {
        this.pathSeeds = pathSeeds;
    }

    public String getPathConfigs() {
        return pathConfigs;
    }

    public void setPathConfigs(String pathConfigs) {
        this.pathConfigs = pathConfigs;
    }

    public String getPathClickRegex() {
        return pathClickRegex;
    }

    public void setPathClickRegex(String pathClickRegex) {
        this.pathClickRegex = pathClickRegex;
    }

    public String getPathRegexFilter() {
        return pathRegexFilter;
    }

    public void setPathRegexFilter(String pathRegexFilter) {
        this.pathRegexFilter = pathRegexFilter;
    }

    public String getPathProtocolFilter() {
        return pathProtocolFilter;
    }

    public void setPathProtocolFilter(String pathProtocolFilter) {
        this.pathProtocolFilter = pathProtocolFilter;
    }

    public String getPathSuffixFilter() {
        return pathSuffixFilter;
    }

    public void setPathSuffixFilter(String pathSuffixFilter) {
        this.pathSuffixFilter = pathSuffixFilter;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDepthCrawl() {
        return depthCrawl;
    }

    public void setDepthCrawl(int depthCrawl) {
        this.depthCrawl = depthCrawl;
    }

    public int getDepthDynamic() {
        return depthDynamic;
    }

    public void setDepthDynamic(int depthDynamic) {
        this.depthDynamic = depthDynamic;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getCompleteTimes() {
        return completeTimes;
    }

    public void setCompleteTimes(int completeTimes) {
        this.completeTimes = completeTimes;
    }

    public int getCycleRecrawl() {
        return cycleRecrawl;
    }

    public void setCycleRecrawl(int cycleRecrawl) {
        this.cycleRecrawl = cycleRecrawl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(Date timeStop) {
        this.timeStop = timeStop;
    }

    public Date getTimeLastCrawl() {
        return timeLastCrawl;
    }

    public void setTimeLastCrawl(Date timeLastCrawl) {
        this.timeLastCrawl = timeLastCrawl;
    }

    public int getCostLastCrawl() {
        return costLastCrawl;
    }

    public void setCostLastCrawl(int costLastCrawl) {
        this.costLastCrawl = costLastCrawl;
    }

    public int getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(int scheduleType) {
        this.scheduleType = scheduleType;
    }

    public int getWorkNum() {
        return workNum;
    }

    public void setWorkNum(int workNum) {
        this.workNum = workNum;
    }

    public Date getCreateTime() {
        return createDate;
    }

    public void setCreateTime(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {

        return JSONObject.toJSONString(this);

    }
}
