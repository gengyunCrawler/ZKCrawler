package com.gy.wm.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.openqa.selenium.WebDriver;

/**
 * Created by Administrator on 2016/11/3.
 */
public class WebDriverPool extends BasePooledObjectFactory<WebDriver> {
    @Override
    public WebDriver create() throws Exception {
        return null;

    }

    @Override
    public PooledObject<WebDriver> wrap(WebDriver webDriver) {
        return new DefaultPooledObject<>(webDriver);
    }

    public static void main(String []args){

        ObjectPool<WebDriver> pooledObject =  new GenericObjectPool<WebDriver>(new WebDriverPool());



    }

}
