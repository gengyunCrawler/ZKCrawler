package com.gy.wm.myExtract;

import org.junit.runner.notification.RunListener;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tijun on 2016/11/18.
 * reference from cx-extractor
 */
@RunListener.ThreadSafe
public class HtmlExtractor {

    private HtmlExtractor(){}

    public static Article parse(String html){
        Article article = new Article();
        Pattern pattern = Pattern.compile(".*<title>(.*)</title>.*");//this can be final
        Matcher matcher = pattern.matcher(html);
       String title = matcher.group(1);

        //1。获取标题

        //2.获取正文

        //3.获取来源  由比重确定 抽取符合条件的str放到list中再计算

        //4.获取发布时间//数字在整个句子中的比重确定

        //5.获取作者  由比重确定,去掉已被确定的文章内容和标题
        // 3,4,5 for a loop to deal

        return article;
    }

    private String getText(String html){
        return null;
    }
    private static String parseTitle(String html){
        return  null;
    }
    private static String parseContent(List<String> lines){
        return  null;
    }

    private static String parsePublishTime(List<String> lines){
        return null;
    }

    private static String getAuthor(String str){
        return  null;
    }

}
