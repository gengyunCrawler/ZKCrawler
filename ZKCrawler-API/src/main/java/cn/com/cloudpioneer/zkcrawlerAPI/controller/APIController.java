package cn.com.cloudpioneer.zkcrawlerAPI.controller;

import cn.com.cloudpioneer.zkcrawlerAPI.service.HbaseHandleService;
import com.alibaba.fastjson.JSONObject;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Scope("prototype")
@RequestMapping("/api")
public class APIController {
    @Autowired
    private HbaseHandleService handleService;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {

        model.addAttribute("name", name);
        return "greeting";
    }

    /**
     *
     * @api {post} ZKCrawler-API/api/getHbaseData 获取Hbase数据接口
     * @apiGroup CrawlerGroup
     * @apiVersion 0.1.0
     * @apiParam {String} taskId 任务id
     * @apiParam {String} startRow 开始读取的行标志
     * @apiParam {int} size 本次读取的数据条数，限定每次只能请求1~100条
     * @apiSuccess {String} code 结果码
     * @apiSuccess {boolean} result 请求结果是否成功
     * @apiSuccess {String} nextRow 下次请求的row，即下次调用接口时当作startRow参数传入
     * @apiSuccess {int} size 实际返回数据大小
     * @apiSuccess  {json} data 实际数据，为json数组： [json,json,json,...]
     * @apiError {json} reason 当result为false时传回，{"result":false,"reason":"xxx"}
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *   "code": 200,
     *   "result": true,
     *   "nextRow": frR7J,
     *   "size": 100,
     *   "data": {[
     *      "tid":"c5b475b03652d36b5fdfe97022be0240",
     *      "url":"http://www.gygov.gov.cn/art/2016/10/31/art_10684_1067314.html",
     *      "statusCode":"200",
     *      "pass":"1",
     *      "type":"0",
     *      "rootUrl":"http://www.gygov.gov.cn/",
     *      "fromUrl":"http://www.gygov.gov.cn/",
     *      "text":" 为支持贵阳市城市可持续发展，全球环境基金（GEF）拟捐助贵阳市开展以TOD为导向的可持续城市发展研究工作。为确保项目的科学性，现在就开展的三个项目（贵阳市TOD发展战略研究、贵阳市轨道交通（第二期）沿线用地综合开发规划、贵阳市快速公交系统沿线用地综合开发规划）在社会以及环境影响方面向广大市民征求意见。",
     *      "html":"xxx",
     *      "title":"关于贵阳市可持续发展建设综合试点项目的公示",
     *      "startTime":"2016-10-2 11:13:59",
     *      "crawlTime":"2016-10-31 11:14:14",
     *      "publishTime":"2016-10-31",
     *      "depthfromSeed":"1"，
     *      "count":"341",
     *      "tag":{"area":"贵州","column":"贵州新闻"},
     *      "fetched":"true",
     *      "author":"",
     *      "sourceName":"",
     *      "parsedData":"",
     *   ]
     *   }
     * }
     */
    @RequestMapping(value = "/getHbaseData", method = RequestMethod.POST)
    public String getHbaseData(String taskId,String startRow,String size)    {
        String result = handleService.getHBaseData(taskId,startRow,size);
//        String result = "taskId:"+taskId+"\n"+"startRow:"+startRow+"\n"+"size:"+size;
        return result;
    }

}
