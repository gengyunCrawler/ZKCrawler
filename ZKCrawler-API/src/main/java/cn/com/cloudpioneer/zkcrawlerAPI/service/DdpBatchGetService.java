package cn.com.cloudpioneer.zkcrawlerAPI.service;

import cn.com.cloudpioneer.zkcrawlerAPI.dao.BatchGetDao;
import cn.com.cloudpioneer.zkcrawlerAPI.model.BatchGetInfo;
import cn.com.cloudpioneer.zkcrawlerAPI.model.BatchGetLog;
import cn.com.cloudpioneer.zkcrawlerAPI.model.BatchResult;
import cn.com.cloudpioneer.zkcrawlerAPI.model.CrawlData;
import cn.com.cloudpioneer.zkcrawlerAPI.utils.RandomAlphaNumeric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 11/9/16.
 */

@Service
public class DdpBatchGetService {

    @Autowired
    private HbaseHandleService hbaseHandleService;

    @Autowired
    private BatchGetDao batchGetDao;


    public String batchDataGet(String nextSign, int size) {

        if (nextSign == null || nextSign.equals(""))
            nextSign = "0";

        if (size <= 0)
            size = 100;
        int getSize = 0;

        String newNextSign = RandomAlphaNumeric.randomStringOfLength(10) + "-" + System.currentTimeMillis() + "-" + RandomAlphaNumeric.randomStringOfLength(10);
        boolean isFirstGet = false;
        if (nextSign.equals("0")) {
            nextSign = newNextSign;
            isFirstGet = true;
        }

        BatchResult batchResult = new BatchResult();
        Map<String, Object> resultMap;

        List<BatchGetInfo> updateBatchGetInfoList = new ArrayList<>();
        List<BatchGetInfo> batchGetInfoList;
        if (isFirstGet) {

            batchGetInfoList = batchGetDao.findAllGetInfo();
        } else {

            batchGetInfoList = batchGetDao.findAllGetInfoByNextSign(nextSign);
        }

        List<BatchGetLog> batchGetLogs = new ArrayList<>();

        for (BatchGetInfo item : batchGetInfoList) {

            try {
                if (isFirstGet) {
                    resultMap = hbaseHandleService.getMapHBaseData(
                            item.getIdTask(),
                            item.getIdTask() + "|",
                            size
                    );
                } else {
                    resultMap = hbaseHandleService.getMapHBaseData(
                            item.getIdTask(),
                            item.getNextRow(),
                            size
                    );
                }
            } catch (Exception e) {

                e.printStackTrace();
                if (isFirstGet)
                    newNextSign = "0";
                continue;
            }

            List<CrawlData> crawlDataList = (List<CrawlData>) resultMap.get("data");

            if (crawlDataList.size() > 0) {
                getSize += crawlDataList.size();
                batchResult.addData(crawlDataList);
            }

            item.setNextRow((String) resultMap.get("nextRow"));
            item.setNextSign(newNextSign);
            item.setLastSize((int) resultMap.get("size"));
            item.setAllSize(item.getAllSize() + (int) resultMap.get("size"));
            item.setUpdateTime(new Date(System.currentTimeMillis()));

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

        return batchResult.toJSONString();
    }


}
