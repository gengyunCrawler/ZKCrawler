package com.gy.wm.plugins.newsExportPlugin.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gy.wm.dao.FieldCroperEntityDao;
import com.gy.wm.model.CrawlData;
import com.gy.wm.model.FieldCroperEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <类详细说明:针对特征字段的精确裁剪，用于修正Xpath得到的不准确的修正>
 *
 * @Author： Huanghai
 * @Version: 2016-12-20
 **/
@Component
public class FieldCroperHandler {

    private FieldCroperEntityDao fieldCroperEntityDao;

    public FieldCroperHandler() {
        this.fieldCroperEntityDao = new FieldCroperEntityDao();
    }
    /**
     * 处理获取精确值
     * @param crawlData
     * @return
     */
    public CrawlData accessFieldCroper(CrawlData crawlData) {
        //加载字段精确裁剪规则croper
        FieldCroperEntity croper = fieldCroperEntityDao.findById(crawlData.getTid());

        if (crawlData.getParsedData()!=null){
            JSONObject map=JSON.parseObject(crawlData.getParsedData());

            if(croper == null)  {
                //通过Xpath已经精确提取
                crawlData.setTitle(map.getString("title"));
                crawlData.setAuthor(map.getString("author"));
                crawlData.setSourceName(map.getString("sourceName"));
                crawlData.setText(map.getString("content"));
                crawlData.setPublishTime(map.getString("publishTime"));

            }else if(croper !=null) {
                JSONObject cropObject = (JSONObject) JSON.parse(croper.getCropRule());
                //不同字段的名称前缀信息拼接字段
                List<String> fieldString = new ArrayList<>();
                for(String key : cropObject.keySet())   {
                    JSONObject ruleObject = (JSONObject)JSON.parse(cropObject.getString(key));
                    fieldString.add(ruleObject.getString("preffix"));
                }

                //有字段冗余信息的裁剪规则如下：

                //作者
                String author = getCropFieldValue("author", croper, map, fieldString);
                if(author!=null)    {
                    crawlData.setAuthor(author);
                }else {
                    crawlData.setAuthor(map.getString("author"));
                }

                //来源
                String sourceName = getCropFieldValue("sourceName", croper, map, fieldString);
                if(sourceName !=null)   {
                    crawlData.setSourceName(sourceName);
                }else {
                    crawlData.setSourceName(map.getString("sourceName"));
                }

                //时间
                String publishTime = getCropFieldValue("publishTime", croper, map, fieldString);
                if(publishTime !=null)   {
                    crawlData.setPublishTime(publishTime);
                }else {
                    crawlData.setPublishTime(map.getString("publishTime"));
                }

                crawlData.setTitle(map.getString("title"));
                crawlData.setText(map.getString("content"));
            }
        }
        return crawlData;
    }

    /**
     * 具体字段的裁剪
     * @param field_name    字段名称，如author, title，sourceName
     * @param croper    来自数据库配置的字段裁剪规则
     * @param parsedDataJSON    来自parsedData
     * @return 取得的精确字段值
     */
    public String getCropFieldValue(String field_name, FieldCroperEntity croper, JSONObject parsedDataJSON, List<String> fieldString) {
        //fieldName是数据库中字段的规则
        String cropRules = JSONObject.parseObject(croper.getCropRule()).getString(field_name);
        //特征值前缀
        String fieldName_preffixs = "";
        //特征值后缀
        String fieldName_suffixs = "";

        String cropFieldValue = "";
        //是否为空值
        boolean nullField = false;

        if (cropRules != null) {
            fieldName_preffixs = JSONObject.parseObject(cropRules).getString("preffix");
            String[] fieldPres = fieldName_preffixs.split("\\|");
            fieldName_suffixs = JSONObject.parseObject(cropRules).getString("suffix");
            String[] fieldSufs = fieldName_suffixs.split("\\|");

            // parsedDataJSON.getString(fieldName)用于保证就算此字段不能精确提取，但也要不为空才进行字符裁剪
            String waittingParseString = parsedDataJSON.getString(field_name);
            //预处理,如果":",冒号之前有空格，去掉空格
            waittingParseString = waittingParseString.replaceAll("\\s*：","：");
            //开始匹配
            if (waittingParseString !=null && ! waittingParseString.equals("")) {
                String[] parsedDataFieldValue = waittingParseString.trim().split("(\\s+|\\u00A0)");
                for (int i=0; i< parsedDataFieldValue.length; i++) {
                    //去前缀
                    for (String fieldPre : fieldPres) {
                        fieldPre = fieldPre.trim();
                        if (parsedDataFieldValue[i].contains(fieldPre)) {
                            //前缀为空
                            if(fieldPre.equals("")) {
                                cropFieldValue = parsedDataFieldValue[i];
                            }else{
                                if(parsedDataFieldValue[i].replace(fieldPre,"").equals("")==false)  {
                                    //前缀后面无空格
                                    cropFieldValue = parsedDataFieldValue[i].replace(fieldPre,"");
                                }else if(parsedDataFieldValue[i].replace(fieldPre,"").equals(""))  {
                                    if(i+1<parsedDataFieldValue.length) {
                                        //fieldString为数据库中提取字段的全部前缀
                                        for (String fieldValue : fieldString) {
                                            fieldValue=fieldValue.trim();
                                            if(! fieldValue.equals(fieldPre))    {
                                                if(parsedDataFieldValue[i+1].contains(fieldValue))    {
                                                    //判断字段是空格，并且后面的字符包含特征值，如"发布时间",表明当前字段为空
                                                    nullField = true;
                                                    break;
                                                }

                                            }
                                        }
                                    }else   {
                                        nullField = true;
                                    }
                                    //循环比较结束,发现分割值的下一个值不是特征值
                                    if(nullField==false)    {
                                        cropFieldValue = parsedDataFieldValue[i+1];
                                    }
                                }
                            }
                        }
                        //去后缀
                        for (String fieldSuf : fieldSufs) {
                            if(cropFieldValue.endsWith(fieldSuf))   {
                                //后缀为空
                                if(fieldSuf.equals("")) {
                                    break;
                                }else {
                                    cropFieldValue = cropFieldValue.replace(fieldSuf,"");
                                    break;
                                }
                            }
                        }
                    }
                    //如果已经找到特定字段的值，或者给字段本身不内容，跳出最外层循环
                    if(cropFieldValue.equals("")==false || nullField == true)    {
                        break;
                    }
                }
            }
            return cropFieldValue;
        }else {
            return null;
        }
    }
}
