package com.gy.wm.model.rdb;

import com.gy.wm.model.CrawlData;
import com.gy.wm.model.InsertSqlModel;

import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 5/31/16.
 */
public interface RdbModel {

    public Object setThisModelFields(CrawlData data);
    public InsertSqlModel insertSqlModelBuilder(String tableName);
}
