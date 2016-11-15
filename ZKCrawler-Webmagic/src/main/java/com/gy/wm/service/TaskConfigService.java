package com.gy.wm.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gy.wm.dao.TaskConfigDao;
import com.gy.wm.entry.TaskConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tijun on 2016/11/14.
 * @author TijunWang
 */
@Service
public class TaskConfigService {

    @Autowired
    TaskConfigDao dao;

    public JSONObject findByIdTask(String idTask){

        TaskConfig config = dao.findTaskConfigByIdTask(idTask);
        String confValue="";
        if (config!=null){
            confValue = config.getConfValue();
        }
       JSONObject object = JSON.parseObject(confValue);
        return object;
    }

    public List<TaskConfig> findByIdStart(int id){
        List<TaskConfig> configs = dao.findByIdStart(id);
     return  configs;
    }
}
