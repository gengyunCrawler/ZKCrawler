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

        List<BatchGetInfo> batchGetInfoList;
        if (isFirstGet) {

            batchGetInfoList = batchGetDao.findAllGetInfo();
        } else {

            batchGetInfoList = batchGetDao.findAllGetInfoByNextSign(nextSign);
        }

        List<BatchGetLog> batchGetLogs = new ArrayList<>();

        for (int i = 0; i < batchGetInfoList.size(); i++) {

            try {
                if (isFirstGet) {
                    resultMap = hbaseHandleService.getMapHBaseData(
                            batchGetInfoList.get(i).getIdTask(),
                            batchGetInfoList.get(i).getIdTask() + "|",
                            size
                    );
                } else {
                    resultMap = hbaseHandleService.getMapHBaseData(
                            batchGetInfoList.get(i).getIdTask(),
                            batchGetInfoList.get(i).getNextRow(),
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

            batchGetInfoList.get(i).setNextRow((String) resultMap.get("nextRow"));
            batchGetInfoList.get(i).setNextSign(newNextSign);
            batchGetInfoList.get(i).setLastSize((int) resultMap.get("size"));
            batchGetInfoList.get(i).setAllSize(batchGetInfoList.get(i).getAllSize() + (int) resultMap.get("size"));
            batchGetInfoList.get(i).setUpdateTime(new Date(System.currentTimeMillis()));

            batchGetLogs.add(
                    new BatchGetLog(
                            batchGetInfoList.get(i).getIdTask(),
                            newNextSign,
                            batchGetInfoList.get(i).toJSONString()
                    )
            );
        }

        batchResult.setNextSign(newNextSign);
        batchResult.setStatus(true);
        batchResult.setSize(getSize);

        batchGetDao.updateGetInfos(batchGetInfoList);
        batchGetDao.insertGetLogs(batchGetLogs);

        return batchResult.toJSONString();
    }


}
