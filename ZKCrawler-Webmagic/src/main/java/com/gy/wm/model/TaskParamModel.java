package com.gy.wm.model;

import com.gy.wm.vo.Base;
import com.gy.wm.vo.Param;
import org.springframework.stereotype.Component;

/**
 * <类详细说明:任务启动所需要的参数，其中param和base的说明详见具体类>
 *
 * @Author： Huanghai
 * @Version: 2016-09-20
 **/
@Component
public class TaskParamModel {
    private Param param;
    private Base base;

    public Param getParam() {
        return param;
    }

    public void setParam(Param param) {
        this.param = param;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }
}
