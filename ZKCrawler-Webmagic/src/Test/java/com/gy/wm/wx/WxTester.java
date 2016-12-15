package com.gy.wm.wx;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class WxTester {
    @Test
    public void testGetUrls() throws IOException {
       List<String> urls= DownloadUrl.downWxUrls(2016,12);
        System.out.println(urls);
    }
}
