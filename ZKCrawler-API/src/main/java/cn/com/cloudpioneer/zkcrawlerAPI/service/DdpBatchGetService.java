package cn.com.cloudpioneer.zkcrawlerAPI.service;

import cn.com.cloudpioneer.zkcrawlerAPI.dao.BatchGetDao;
import cn.com.cloudpioneer.zkcrawlerAPI.model.BatchGetInfo;
import cn.com.cloudpioneer.zkcrawlerAPI.model.BatchGetLog;
import cn.com.cloudpioneer.zkcrawlerAPI.model.BatchResult;
import cn.com.cloudpioneer.zkcrawlerAPI.model.CrawlData;
import cn.com.cloudpioneer.zkcrawlerAPI.utils.RandomAlphaNumeric;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 11/9/16.
 */

@Service
public class DdpBatchGetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DdpBatchGetService.class);

    @Autowired
    private HbaseHandleService hbaseHandleService;

    @Autowired
    private BatchGetDao batchGetDao;


    /**
     * 数据批量获取的操作方法。
     *
     * @param nextSign 数据获取标记，第一次获取时取字符串 "0"，以后在返回的结果中
     *                 有nextSign信息，需要用户自己保存此标记，用于下次取数据。
     * @param size     期望取数据的条数，不指定条数时，此值要设置为 0.
     * @return 结果集 JSON 字符串。
     */
    public String batchDataGet(String nextSign, int size) {

        /**
         * 对读取标记进行判断，若读取标记为空指针或空字符串，返回错误信息；
         * 由于用户不提供正确的读取标记而无法读取数据。
         */
        if (nextSign == null || nextSign.equals("")) {
            LOGGER.error("====>> 读取标记 nextSign 错误. 读取标记nextSign为空.");
            return "{\"status\":false,\"reason\":\"读取标记 nextSign 错误,读取标记nextSign为空.\"}";
        }

        /**
         * 若无用户不指定读取数据条数，或者指定错误的
         * 数据读取条数据，则给默认值 10240.
         */
        if (size <= 0)
            size = 10240;

        /**
         * 本次获取的数据条数。
         */
        int getSize = 0;

        /**
         * 每次都生成一个新的读取标记 nextSign.
         * 生成的此标记是唯一的.
         */
        String newNextSign = RandomAlphaNumeric.randomStringOfLength(10) + "-" + System.currentTimeMillis() + "-" + RandomAlphaNumeric.randomStringOfLength(10);

        /**
         * 第一次数据获取标志，标志本次数据获取请求是否是第一次获取数据。
         * 如果nextSign的值为字符串 "0"，则是第一次获取数据，这是约定。
         * 是第一次获取数据，则把 isFirstGet 设置为 true。
         */
        boolean isFirstGet = false;
        if (nextSign.equals("0")) {
            LOGGER.info("====>> 第一次读取数据，nextSign: [ \"0\" ], size: [ " + size + " ]");
            nextSign = newNextSign;
            isFirstGet = true;
        }else {

            /**
             * 不是第一次获取数据，那么就要从操作日志中检查 nextSign 的有效性。
             */
            if (!batchGetDao.isValidNextSign(nextSign)){
                LOGGER.warn("====>> 读取标记 nextSign: [ " + nextSign + " ] 无效.");
                return "{\"status\":false,\"reason\":\"读取标记 nextSign 无效.\"}";
            }
        }

        /**
         * 结果对象 batchResult 和
         * Hbase 操作返回的结果字典对象 resultMap。
         */
        BatchResult batchResult = new BatchResult();
        Map<String, Object> resultMap;

        /**
         * 需要获取数据的任务集列表和中间用到的缓存列表。
         */
        List<BatchGetInfo> updateBatchGetInfoList = new ArrayList<>();
        List<BatchGetInfo> batchGetInfoList;
        List<BatchGetInfo> temp = new ArrayList<>();


        if (isFirstGet) {
            /**
             * 第一次获取数据，把所有需要获取数据的任务全部取出来，
             * 然后设置nextRow为第一次获取标志 taskId+|。
             */
            batchGetInfoList = batchGetDao.findAllGetInfo();
            for (BatchGetInfo elem : batchGetInfoList) {
                elem.setNextRow(elem.getIdTask() + "|");
                temp.add(elem);
            }
            batchGetInfoList = temp;
        } else {

            /**
             * 不是第一次获取数据，则按 nextSign 获取要取数据的任务；
             * 然后统计要获取数据的任务的数量 count。
             */
            batchGetInfoList = batchGetDao.findAllGetInfoByNextSign(nextSign);
            int count = batchGetDao.countGetInfo();
            LOGGER.info("====>> batchGetInfoList.size = " + batchGetInfoList.size() + ", batchInfo Count = " + count);

            if (count != batchGetInfoList.size()) {

                /**
                 * 通过nextSign取得的任务数量和任务的总量不等，以下情况导致不等：
                 * 1. 要获取的任务有增加；
                 * 2. 要获取的任务有减少；
                 * 3. 本次获取数据的用户和上次获取数据的用户不是同一个用户；
                 * 4. 本次获取数据的用户和上次获取数据的用户是同一个用户，但是他提供的nextSign不是上次他获得的nextSign。
                 *
                 * 那么，这时候就要取得所有的要获取数据的任务。
                 */
                batchGetInfoList = batchGetDao.findAllGetInfo();

                /**
                 * 循环遍历任务列表，进行必要的处理。
                 */
                for (BatchGetInfo elem : batchGetInfoList) {

                    /**
                     * 查看每个要获取数据的任务的 nextSign 和用户提供的 nextSign 是否相同。
                     */
                    if (!elem.getNextSign().equals(nextSign)) {

                        /**
                         * 两个 nextSign 不同，那么就要以用户提供的这个 nextSign 和任务的ID去查询操作日志。
                         */
                        List<BatchGetLog> logList = batchGetDao.findGetLogsByIdTaskAndNextSign(elem.getIdTask(), nextSign);
                        if (logList != null && logList.size() != 0) {
                            /**
                             * 查询的日志不为空，那说明日志的此条记录才是此用户对此任务的最后一次访问信息。
                             * 把此日志的任务日志信息还原给此用户的此任务 elem.
                             */
                            elem = JSONObject.toJavaObject(
                                    JSONObject.parseObject(logList.get(0).getLogInfo()),
                                    BatchGetInfo.class
                            );

                            temp.add(elem);

                        } else {

                            /**
                             * 日志查询不到此用户对此任务的记录，此任务是新增加的任务.
                             * 那么，给此任务的 nextRow 初始化为初始值 taskId + |.
                             */
                            elem.setNextRow(elem.getIdTask() + "|");
                            temp.add(elem);

                        }

                    } else {

                        /**
                         * 两个 nextSign 相同，说明此任务的上次操作就是此用户，不需要查询操作日志了。
                         */
                        temp.add(elem);
                    }

                }

                batchGetInfoList = temp;
            }

        }


        /**
         * 操作日志列表对象。
         */
        List<BatchGetLog> batchGetLogs = new ArrayList<>();

        /**
         * 循环操作，对每个要取数据的任务进行取数据操作。
         */
        for (BatchGetInfo item : batchGetInfoList) {

            try {

                /**
                 * 调用 Hbase 操作方法，获取任务 item 的数据结构字典 resulMap。
                 */
                resultMap = hbaseHandleService.getMapHBaseData(
                        item.getIdTask(),
                        item.getNextRow(),
                        size
                );

            } catch (Exception e) {

                LOGGER.warn("====>> hbaseHandleService.getMapHBaseData(...) 出错.");
                e.printStackTrace();
                continue;
            }

            List<CrawlData> crawlDataList = (List<CrawlData>) resultMap.get("data");

            if (crawlDataList.size() > 0) {
                getSize += crawlDataList.size();
                batchResult.addData(crawlDataList); // 结果集数据填充.
            }

            if (resultMap.get("nextRow") != null) {
                item.setNextRow((String) resultMap.get("nextRow"));
            }

            item.setNextSign(newNextSign);
            item.setLastSize((int) resultMap.get("size"));
            item.setAllSize(item.getAllSize() + (int) resultMap.get("size"));
            item.setUpdateTime(new Date());

            batchGetLogs.add(
                    new BatchGetLog(
                            item.getIdTask(),
                            newNextSign,
                            item.toJSONString()
                    )
            );
            updateBatchGetInfoList.add(item);
        }

        /**
         * 结果集数据填充。
         */
        batchResult.setNextSign(newNextSign);
        batchResult.setStatus(true);
        batchResult.setSize(getSize);

        /**
         * 任务表更新和操作日志表添加新的操作日志。
         */
        batchGetDao.updateGetInfos(updateBatchGetInfoList);
        batchGetDao.insertGetLogs(batchGetLogs);

        LOGGER.info("====>> return size: " + getSize);
        LOGGER.info("====>> return nextSign: [ \"" + newNextSign + "\" ]");

        /**
         * 返回结果集JSON串。
         */
        return batchResult.toJSONString();
    }

}
