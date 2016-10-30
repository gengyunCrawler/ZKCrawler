/*
package com.gy.wm.downloader;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;

import java.util.concurrent.TimeUnit;

*/
/**
 * Created by Tijun on 2016/8/19.
 *//*

public class WeixinTester
{
    @RandomStringCreator
    public void testWebDriver4weixin(){
        System.setProperty("phantomjs.binary.path", "D:\\Tool\\PhantomJS\\phantomjs.exe");
        WebDriver driver=new PhantomJSDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        SeleniumDownloader seleniumDownloader=new SeleniumDownloader();
        seleniumDownloader.setWebDriver(driver);
       //String url="http://mp.weixin.qq.com/profile?src=3&timestamp=1471592055&ver=1&signature=W6RDntkcLnwNu6-avPzPKiNtL*cO4*QONS1weTTAx4z3IKVA1z**-EJP6uo3b84H6d2-f8xIV97sO9RuoTP-zw==";
       // String  url="http://mp.weixin.qq.com/profile?src=3&timestamp=1471838367&ver=1&signature=W6RDntkcLnwNu6-avPzPKiNtL*cO4*QONS1weTTAx4z3IKVA1z**-EJP6uo3b84HQI9nFTet7R2bkplaWXvNng==";
        String url="http://news.163.com";
        long start=System.currentTimeMillis();
        Spider.create(new WeixinPageprocessor()).
                addPipeline(new JsonFilePipeline("D:\\testData\\SpiderData\\")).
                addUrl(url).setDownloader(seleniumDownloader).thread(5).run();
        long end=System.currentTimeMillis();
        System.out.println("花费时间------"+(end-start)/1000);
    }

}
*/
