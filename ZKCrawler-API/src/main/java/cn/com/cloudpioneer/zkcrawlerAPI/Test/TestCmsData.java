package cn.com.cloudpioneer.zkcrawlerAPI.Test;

import cn.com.cloudpioneer.zkcrawlerAPI.model.elastic.CategoriesInfo;
import cn.com.cloudpioneer.zkcrawlerAPI.model.elastic.CmsData;
import cn.com.cloudpioneer.zkcrawlerAPI.model.elastic.TagsInfo;
import com.alibaba.fastjson.JSON;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TianyuanPan on 11/1/16.
 */
public class TestCmsData {

    private static CmsData data;


    public static void main(String[] args) {

        data = new CmsData();
        data.setTaskId("task-id-001");
        data.setUrl("http://www.testdata.com/articles/art_0001.html");
        data.setStatusCode(200);
        data.setRootUrl("http://www.testdata.com/articles");
        data.setFromUrl("http://www.testdata.com/articles");
        data.setTitle("this is a title");
        data.setText("1880年，纽约新闻记者约翰·迈克尔斯（英语：John Michaels）创立了《科学》，这份期刊先后得到了托马斯·爱迪生以及亚历山大·格拉汉姆·贝尔的资助。但由于从未拥有足够的用户而难以为继，《科学》于1882年3月停刊。一年后，昆虫学家Samuel Hubbard Scudder使其复活并取得了一定的成功。然而到了1894年，《科学》重新陷入财政危机，随后被以500美元的价格转让给心理学家James McKeen Cattell");
        data.setHtml("</p> this is html </p>");
        data.setPublishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        data.setCrawTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        List<CategoriesInfo> categoriesInfo = new ArrayList<>();
        categoriesInfo.add(new CategoriesInfo("categories-01"));
        categoriesInfo.add(new CategoriesInfo("categories-02"));
        categoriesInfo.add(new CategoriesInfo("categories-03"));
        data.setCategories("categories-01,categories-02,categories-3");
        data.setCategoriesInfo(categoriesInfo);
        data.setTags("tag-01,tag-02");
        List<TagsInfo> tagsInfos = new ArrayList<>();
        tagsInfos.add(new TagsInfo("tag-01"));
        tagsInfos.add(new TagsInfo("tag-02"));
        data.setTagsInfo(tagsInfos);
        data.setPasedData(new Object());
        List<Object> objects = new ArrayList<>();
        objects.add(new TagsInfo("tag001"));
        objects.add(new TagsInfo("tag002"));
        data.setExtensions(objects);

        System.out.println("JSON Utils --> String:\n\t" + JSON.toJSONString(data));
        System.out.println("Override toString:\n\t" + data.toString());

    }

}
