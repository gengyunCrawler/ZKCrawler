package com.gy.wm.util;

/**
 * Created by Administrator on 2016/4/28.
 */
public class URLFilter {
    public static boolean matchDomain(String url,String domain) {
        String pattern = "(http|https)(://)(" + domain +")(.*)";
        return url.matches(pattern);
    }

    public static boolean linkFilter(String url) {
        String skip = "gif|GIF|jpg|JPG|png|PNG|ico|ICO|css|" +
                "sit|eps|wmf|zip|ZIP|ppt|mpg|xls|gz|rpm|tgz|mov|" +
                "MOV|exe|jpeg|JPEG|bmp|BMP|rar|mp4|MP4|doc|DOC|" +
                "js|css|pdf|JS|CSS|PDF|RAR|docx|DOCX|mp3|MP3|wmv|WMV|" +
                "swf|SWF";
        String [] skipOptions = skip.split("\\|");
        boolean includeTag = true;
        for(int i=0; i<skipOptions.length; i++)   {
            includeTag = includeTag&&!url.endsWith(skipOptions[i]);
        }
        return includeTag;
    }

    //test
    public static void main(String[] args) {
        System.out.println(new URLFilter().linkFilter("http://www.gzqxnkj.gov.cn/d/file/f/2016-03-22/242ca4cd8b052229ab61ec80063e7ec4.jpg"));
    }
}
