package cn.com.cloudpioneer.zkcrawlerAPI.app;

/**
 * Created by TianyuanPan on 2016/10/14.
 */
public class ZKCrawlerAPI {


    private static volatile ZKCrawlerAPI thisApi;


    private ZKCrawlerAPI() {

    }



    public static ZKCrawlerAPI initializeAPI() {

        if (thisApi == null)
            thisApi = new ZKCrawlerAPI();
        return thisApi;

    }


    public static ZKCrawlerAPI getAPI(){

        return initializeAPI();
    }



}
