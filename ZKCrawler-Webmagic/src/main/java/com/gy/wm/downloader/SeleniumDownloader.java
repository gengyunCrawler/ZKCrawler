package com.gy.wm.downloader;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *This is a downloader for dynamic web page,and it needs phantomjs driver to support.<br/>
 * @author TijunWang
 *         Date: 2016-9-19 <br>
 *         Time: afternoon 1:37 <br>
 */
public class SeleniumDownloader implements Downloader {
    private Lock lock = new ReentrantLock();

    private final static org.slf4j.Logger LOG= LoggerFactory.getLogger(SeleniumDownloader.class);

    static {
        /**
         * The phantomjs driver installed path are indicated in /webdriver.properties
         */
        InputStream is= System.class.getResourceAsStream("/webdriver.properties");
        Properties properties=new Properties();
        try
        {
            properties.load(is);
            /**
             * init an environment for phantomjs
             */
             String phantomJsPath=properties.getProperty("phantomjs.binary.path");
            if (phantomJsPath==null||phantomJsPath.equals("")){
                throw new Exception("must have phantomjs webdriver path be indicated in /webdriver.properties");
            }

            System.setProperty("phantomjs.binary.path",phantomJsPath);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private int sleepTime = 0;

    private int poolSize = 1;

    private WebDriverPool webDriverPool = new WebDriverPool();


    private int webdriverSize=5;

    /**
     * set webdriver size
     * @param webdriverSize
     */
    public void setWebdriverSize(int webdriverSize) {
        this.webdriverSize = webdriverSize;
        webDriverPool.setSize(webdriverSize);
    }

    /**
     * Constructor without any filed.
     *
     * @author
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

        lock.lock();
        while (webDriverPool.getAvailabe()==0){
            waitTime();
        }
        WebDriver  webDriver1= webDriverPool.get();
        lock.unlock();

        LOG.info("downloading page " + request.getUrl());

        webDriver1.get(request.getUrl());
        this.sleep();
        WebElement webElement = webDriver1.findElement(By.xpath("/html"));
        String content = webElement.getAttribute("outerHTML");

        webDriverPool.returnWebDriver(webDriver1);

        Page page = new Page();
        page.setRawText(content);
        page.setHtml(new Html(UrlUtils.fixAllRelativeHrefs(content, request.getUrl())));
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        return page;
    }

    /**
     * sleep sleepTime  to wait page loading
     */
    private void sleep(){
        try
        {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void setThread(int thread) {
        this.poolSize = thread;
    }


    /**
     * for current thread to wait 0.36s
     */
    private void  waitTime(){
        try {
            Thread.sleep(360);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * a webdriver pool
 */
 class WebDriverPool implements Closeable{

     //available number in webdriverPool
     private AtomicInteger availableNum = new AtomicInteger();

     private int size = 1 ;
     private int num = 0;

     private Lock lock = new ReentrantLock();

     //store webdriver's reference
     private BlockingQueue<WebDriver> webDriverQueue = new LinkedBlockingDeque<>();

    //default constructor
      WebDriverPool(){
          size = 5;
          num = size;
          this.init();
     }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * create  a pool indicate the size
     * @param size
     */
      WebDriverPool(int size){
         this.size = size;
          num=size;
          this.init();
     }

    /**
     * create a webdriver instance and init basic configuration
     */
     private WebDriver create(){
         WebDriver webDriver=new PhantomJSDriver();
         webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
         return webDriver;
     }

    /**
     * init webdriverPool holds webdrivers
     */
    private void init(){
         while (num>0){
             try {
                 webDriverQueue.put(create());
                 availableNum.getAndIncrement();
                 num--;
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
     }

    /**
     * get a webdriver from this pool
     * @return
     */
      WebDriver get(){
         WebDriver webDriver = null;
         lock.lock();
          availableNum.getAndDecrement();
          if (webDriverQueue.size()>0){
             try {
                 webDriver  = webDriverQueue.take();
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }

         lock.unlock();
         return webDriver;
     }


    /**
     * return a webriver to pool
     * @param webDriver
     */
    void  returnWebDriver(WebDriver webDriver){
         if (webDriver == null){
            return;
         }
         try {
             lock.lock();
             webDriverQueue.put(webDriver);
             availableNum.getAndIncrement();

         } catch (InterruptedException e) {
             e.printStackTrace();
         }finally {
             lock.unlock();
         }
     }

    /**
     * availabe webdriver in pool,if the drive  be killed in other thread,then make a new driver in pool
     * @return
     */
      int getAvailabe(){
         lock.lock();
             try {
                 while (availableNum.get()>webDriverQueue.size()){
                      webDriverQueue.put(create());
                 }
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }finally {
                 lock.unlock();
             }
        return webDriverQueue.size();
     }

     /**
      * quit webdriver and kill the PhantomJS driver process before this object be destroy
      *
      * @throws IOException if an I/O error occurs
      */
     @Override
     public void close() throws IOException {

         if (availableNum.get()>0){

             while (webDriverQueue.isEmpty()==false){

                 try {

                     WebDriver driver = webDriverQueue.take();
                     driver.quit();
                     driver.close();

                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         }
     }
 }