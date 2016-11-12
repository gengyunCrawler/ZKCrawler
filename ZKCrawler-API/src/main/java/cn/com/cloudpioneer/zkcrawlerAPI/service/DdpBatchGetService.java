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


    public String batchDataGet(String nextSign, int size) {

        if (nextSign == null || nextSign.equals("")) {
            LOGGER.error("读取标记 nextSign 错误. 读取标记nextSign不能为空.");
            return "{\"status\":false,\"reason\":\"读取标记 nextSign 错误.\"}";
        }

        if (size <= 0)
            size = 10240;
        int getSize = 0;

        String newNextSign = RandomAlphaNumeric.randomStringOfLength(10) + "-" + System.currentTimeMillis() + "-" + RandomAlphaNumeric.randomStringOfLength(10);
        boolean isFirstGet = false;
        if (nextSign.equals("0")) {
            LOGGER.info("====>> 第一次读取数据，nextSign: [ \"0\" ], size: [ " + size + " ]");
            nextSign = newNextSign;
            isFirstGet = true;
        }

        BatchResult batchResult = new BatchResult();
        Map<String, Object> resultMap;

        List<BatchGetInfo> updateBatchGetInfoList = new ArrayList<>();
        List<BatchGetInfo> batchGetInfoList;
        List<BatchGetInfo> temp = new ArrayList<>();
        if (isFirstGet) {
            batchGetInfoList = batchGetDao.findAllGetInfo();
            for (BatchGetInfo elem : batchGetInfoList) {
                elem.setNextRow(elem.getIdTask() + "|");
                temp.add(elem);
            }
            batchGetInfoList = temp;
        } else {

            batchGetInfoList = batchGetDao.findAllGetInfoByNextSign(nextSign);
            int count = batchGetDao.countGetInfo();

            LOGGER.info("====>> batchGetInfoList.size = " + batchGetInfoList.size() + ", batchInfo Count = " + count);

            if (count != batchGetInfoList.size()) {

                batchGetInfoList = batchGetDao.findAllGetInfo();

                for (BatchGetInfo elem : batchGetInfoList) {

                    if (!elem.getNextSign().equals(nextSign)) {

                        List<BatchGetLog> logList = batchGetDao.findGetLogsByIdTaskAndNextSign(elem.getIdTask(), nextSign);
                        if (logList != null && logList.size() != 0) {

                            elem = JSONObject.toJavaObject(
                                    JSONObject.parseObject(logList.get(0).getLogInfo()),
                                    BatchGetInfo.class
                            );

                            temp.add(elem);
                        }

                    } else {

                        temp.add(elem);
                    }

                }

                batchGetInfoList = temp;
            }

            if (batchGetInfoList.size() == 0 && count != 0) {
                LOGGER.warn("====>> 读取标记 nextSign: [ " + nextSign + " ] 无效." );
                return "{\"status\":false,\"reason\":\"读取标记 nextSign 错误.\"}";
            }

        }


        List<BatchGetLog> batchGetLogs = new ArrayList<>();

        for (BatchGetInfo item : batchGetInfoList) {

            try {

                resultMap = hbaseHandleService.getMapHBaseData(
                        item.getIdTask(),
                        item.getNextRow(),
                        size
                );

            } catch (Exception e) {

                LOGGER.warn("====>> hbaseHandleService.getMapHBaseData(...) 出错.");
                e.printStackTrace();
                //if (isFirstGet)
                //    newNextSign = "0";
                continue;
            }

            List<CrawlData> crawlDataList = (List<CrawlData>) resultMap.get("data");

            if (crawlDataList.size() > 0) {
                getSize += crawlDataList.size();
                batchResult.addData(crawlDataList);
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

        batchResult.setNextSign(newNextSign);
        batchResult.setStatus(true);
        batchResult.setSize(getSize);

        batchGetDao.updateGetInfos(updateBatchGetInfoList);
        batchGetDao.insertGetLogs(batchGetLogs);

        LOGGER.info("====>> return size: " + getSize);
        LOGGER.info("====>> return nextSign: [ \"" + newNextSign + "\" ]");

        return batchResult.toJSONString();
    }


}
