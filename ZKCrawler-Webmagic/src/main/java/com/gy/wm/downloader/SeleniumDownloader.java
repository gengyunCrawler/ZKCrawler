package com.gy.wm.downloader;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.UrlUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**

 * 需要下载pantomjs driver支持。<br>
 *
 * @author code4crafter@gmail.com <br>
 *         Date: 13-7-26 <br>
 *         Time: 下午1:37 <br>
 */
public class SeleniumDownloader implements Downloader, Closeable {
private final static org.slf4j.Logger LOG= LoggerFactory.getLogger(SeleniumDownloader.class);
    static {
        InputStream is= System.class.getResourceAsStream("/webdriver.properties");
        Properties properties=new Properties();
        try
        {
            properties.load(is);
            System.setProperty("phantomjs.binary.path",properties.getProperty("phantomjs.binary.path"));
        } catch (IOException e)
        {  LOG.error("must have pantomjs webdriver path be indicated in /webdriver.properties");
            e.printStackTrace();
        }
    }
    private Logger logger = Logger.getLogger(getClass());

    private int sleepTime = 0;

    private int poolSize = 1;

    private WebDriver webDriver;

    //根据每个domain来管理 WebDriver的manmage
    private Map<String,WebDriver> webDriverMap= Collections.synchronizedMap(new HashMap<String, WebDriver>());


    private synchronized WebDriver  getWebDriver(Task task){
        String domain=task.getSite().getDomain();
        WebDriver driver=  webDriverMap.get(domain);
        if (driver==null){
            webDriver = create();
            webDriverMap.put(domain, webDriver);
            driver = webDriver;
        }

        return driver;
    }

    /**
     * Constructor without any filed. Construct PhantomJS browser
     *
     * @author bob.li.0718@gmail.com
     */
    public SeleniumDownloader() {
    }

    /**
     * set sleep time to wait until load success
     *
     * @param sleepTime sleepTime
     * @return this
     */
    public SeleniumDownloader setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    @Override
    public Page download(Request request, Task task) {
        webDriver= this.getWebDriver(task);
        logger.info("downloading page " + request.getUrl());
        webDriver.get(request.getUrl());
        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        String content = webElement.getAttribute("outerHTML");
        Page page = new Page();
        page.setRawText(content);
        page.setHtml(new Html(UrlUtils.fixAllRelativeHrefs(content, request.getUrl())));
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        return page;
    }


    @Override
    public void setThread(int thread) {
        this.poolSize = thread;
    }

    @Override
    public void close() throws IOException {
        //webDriverPool.closeAll();
    }
    //create webDriver
    private WebDriver create(){
        WebDriver webDriver=new PhantomJSDriver();
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return webDriver;
    }
    //when finished all tasks，close all webdrivers and kill all webdriver processes

    public void  closeAll(){
        for (String domain:webDriverMap.keySet()){
            WebDriver driver=webDriverMap.remove(domain);
            if(driver!=null){
                driver.quit();
                driver.close();
                driver=null;
            }

        }
    }
}