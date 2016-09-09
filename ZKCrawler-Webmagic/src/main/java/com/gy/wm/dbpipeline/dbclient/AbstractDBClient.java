package com.gy.wm.dbpipeline.dbclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * Created by TianyuanPan on 5/4/16.
 */
public abstract class AbstractDBClient implements DBClient {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected boolean connOpen;
    protected Connection connection;


    protected void setConnOpen(boolean connOpen) {
        this.connOpen = connOpen;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
