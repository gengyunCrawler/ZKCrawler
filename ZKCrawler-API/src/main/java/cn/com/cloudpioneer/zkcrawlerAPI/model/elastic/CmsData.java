package cn.com.cloudpioneer.zkcrawlerAPI.model.elastic;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 11/1/16.
 */
public class CmsData {

    private String taskId = "";
    private String url = "";
    private int statusCode = 200;
    private String rootUrl = "";
    private String fromUrl = "";
    private String title = "";
    private String text = "";
    private String html = "";
    private String publishTime = "";
    private String crawTime = "";
    private int hotLevel = 0;
    private String categories = "";
    private List<CategoriesInfo> categoriesInfo;
    private String tags = "";
    private List<TagsInfo> tagsInfo;
    private Object pasedData;
    private List<Object> extensions;

    public CmsData() {

        this.tagsInfo = new ArrayList<>();
        this.categoriesInfo = new ArrayList<>();
        this.pasedData = new Object();
        this.extensions = new ArrayList<>();

    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getCrawTime() {
        return crawTime;
    }

    public void setCrawTime(String crawTime) {
        this.crawTime = crawTime;
    }

    public int getHotLevel() {
        return hotLevel;
    }

    public void setHotLevel(int hotLevel) {
        this.hotLevel = hotLevel;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public List<CategoriesInfo> getCategoriesInfo() {
        return categoriesInfo;
    }

    public void setCategoriesInfo(List<CategoriesInfo> categoriesInfo) {
        this.categoriesInfo = categoriesInfo;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<TagsInfo> getTagsInfo() {
        return tagsInfo;
    }

    public void setTagsInfo(List<TagsInfo> tagsInfo) {
        this.tagsInfo = tagsInfo;
    }

    public Object getPasedData() {
        return pasedData;
    }

    public void setPasedData(Object pasedData) {
        this.pasedData = pasedData;
    }

    public List<Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<Object> extensions) {
        this.extensions = extensions;
    }


    @Override
    public String toString() {

/**************************************************************

 JSONObject thisObject = new JSONObject();
 JSONArray tagsInfoArray = new JSONArray();
 JSONArray extensionsArray = new JSONArray();
 JSONArray categoriesArray = new JSONArray();

 for (TagsInfo item : this.tagsInfo) {

 tagsInfoArray.add(item);

 }

 for (Object item : this.extensions) {

 extensionsArray.add(item);
 }

 for (CategoriesInfo item : this.categoriesInfo) {
 categoriesArray.add(item);
 }

 thisObject.put("taskId", this.taskId);
 thisObject.put("url", this.url);
 thisObject.put("statusCode", this.statusCode);
 thisObject.put("rootUrl", this.rootUrl);
 thisObject.put("fromUrl", this.fromUrl);
 thisObject.put("title", this.title);
 thisObject.put("text", this.text);
 thisObject.put("html", this.html);
 thisObject.put("publishTime", this.publishTime);
 thisObject.put("crawlTime", this.crawTime);
 thisObject.put("categories", this.categories);
 thisObject.put("categoriesInfo", this.categoriesInfo);
 thisObject.put("tags", this.tags);
 thisObject.put("tags-info", tagsInfoArray);
 thisObject.put("pasedData", this.pasedData);
 thisObject.put("extensions", extensionsArray);

 return thisObject.toJSONString();

 ***************************************************************/

        return JSON.toJSONString(this);
    }
}
