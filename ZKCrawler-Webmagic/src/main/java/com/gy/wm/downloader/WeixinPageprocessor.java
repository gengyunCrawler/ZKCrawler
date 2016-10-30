/*
package com.gy.wm.downloader;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

*/
/**
 * Created by Tijun on 2016/8/19.
 *//*

public class WeixinPageprocessor implements PageProcessor
{
    private static final String WEIXIN_NUM_URL_REGEX="(http://mp.weixin.qq.com/profile)\\?\\w";

    private static  final String WEIXIN_ARTICALE_REGEX="(http://mp.weixin.qq.com/)s\\?\\w";
    private  Site site=Site.me();
    @Override
    public void process(Page page)
    {
       System.out.println(page.getHtml());

        List<String> list=page.getUrl().all();
        String regex=page.getUrl().toString().split("\\?")[0];
        //处理微信号得到文章的列表链接
        if (page.getUrl().regex(WeixinPageprocessor.WEIXIN_NUM_URL_REGEX).match()){
        List<String> cards= page.getHtml().xpath("/*/
/*[@id='history']/div").all();
        int i=0;
            System.out.println(page.getHtml());
        for (String s:cards){
            Html html=new Html(s);
          List<String> itemList= html.xpath("//div[@class='weui_media_box appmsg']").all();
            List<WeixinArticleLink> weixinArticleLinkList =new ArrayList<WeixinArticleLink>();
            List<String> urlList=new ArrayList<String>();
            for (String item:itemList){
              Html itemHtml=  Html.create(item);

              String url= itemHtml.xpath("//h4[@class='weui_media_title']").regex("(.*hrefs=\")(.*)(\".*)",2).toString().replace("amp;","");
              String title=itemHtml.xpath("//h4[@class='weui_media_title']/text()").toString();
                String date=itemHtml.xpath("//p[@class='weui_media_extra_info']/text()").toString();
               WeixinArticleLink article=new WeixinArticleLink();
                url="http://mp.weixin.qq.com"+url;
                article.setDate(date);
                article.setTitle(title);
                article.setUrl(url);
                weixinArticleLinkList.add(article);
                urlList.add(url);


            }
            page.addTargetRequests(urlList);
            page.putField("articleList"+i, weixinArticleLinkList);

            i++;
        }
        }
        //处理微信文章链接
        if (page.getUrl().regex(WeixinPageprocessor.WEIXIN_ARTICALE_REGEX).match()){
            String title= page.getHtml().xpath("/*/
/*[@id='activity-name']/text()").toString();
            String content=page.getHtml().xpath("/*/
/*[@id='js_content']").smartContent().toString();
            System.out.println(page.getHtml());
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String dateText=page.getHtml().xpath("/*/
/*[@id='post-date']/text()").toString();
            String orginal=page.getHtml().xpath("/*/
/*[@id='post-user']/text()").toString();
            Integer readNum=Integer.parseInt(page.getHtml().xpath("/*/
/*[@id='sg_readNum3']/text()").toString());
            Integer praiseNum=Integer.parseInt(page.getHtml().xpath("/*/
/*[@id='sg_likeNum3']/text()").toString());
            Date publishDate=null;
            try
            {
                 publishDate=sdf.parse(dateText);
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
            WeixinArticle weixinArticle=new WeixinArticle();
            weixinArticle.setTitle(title);
            weixinArticle.setContent(content);
            weixinArticle.setPraiseNum(praiseNum);
            weixinArticle.setPublishDate(publishDate);
            weixinArticle.setReadNum(readNum);
            page.putField("article", weixinArticle);

        }





    }

    @Override
    public Site getSite()
    {
        return site;
    }
}
*/
