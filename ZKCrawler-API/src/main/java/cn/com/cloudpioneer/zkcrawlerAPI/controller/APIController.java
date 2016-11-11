package cn.com.cloudpioneer.zkcrawlerAPI.controller;

import cn.com.cloudpioneer.zkcrawlerAPI.service.DdpBatchGetService;
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


    @Autowired
    private DdpBatchGetService ddpBatchGetService;


    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {

        model.addAttribute("name", name);
        return "greeting";
    }

    /**
     *
     * @api {post} http://ip:port/api/getHbaseData  通过任务id获取Hbase数据接口
     * @apiGroup Crawler
     * @apiVersion 0.1.0
     * @apiParam {String} taskId 任务id
     * @apiParam {String} startRow 开始读取的row,首次读取startRow为taskId+"|"，
     * @apiParam {int} size 本次读取的数据条数，限定每次只能请求1~100条
     * @apiSuccess {boolean} result 请求结果是否成功
     * @apiSuccess {String} nextRow 下次请求的row，即下次调用接口时当作startRow参数传入
     * @apiSuccess {int} size 实际返回数据大小
     * @apiSuccess  {json} data 实际数据，为json数组： [json,json,json,...]
     * @apiError {json} reason 当result为false时传回，{"result":false,"reason":"xxx"}
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
    "result": "true",
    "data": [
    {
    "text": "     　　10月20日,贵安新区举办主题为“公共安全与突发事件应对”的第19期周末大讲堂,新区党工委副书记、管委会主任孙登峰出席,国务院应急管理专家组组长、国家减灾委专家委员会副主任闪淳昌教授作了专题报告,新区党工委委员、管委会副主任张富杰主持,新区领导欧阳武、李建峰、苏永泓等参加。　　报告中,闪淳昌围绕近年来国内外应对自然灾害、事故灾难、公共卫生事件和社会安全事件案例,结合多年来参与国家应急管理领导工作的实践,以文字、图片、音频、视频等多种形式,从案例介绍、处置过程、处置结果以及经验教训等方面,客观总结了应急管理的经验和教训,深刻阐述了提高应对危机、风险能力的基本要求和方法途径。闪淳昌表示,领导干部思想麻痹是城市公共安全的最大隐患,在抓领导干部守土有责、守土尽责的教育管理上不能失责,要进一步提高领导干部应急管理能力,增强安全防范意识,确保人民生命财产安全。　　报告会结束后,闪淳昌还与现场听众进行了交流互动,畅谈心得体会,对大家提出的问题进行了解答。　　新区分管应急管理、安全生产和相关企业负责等近500人聆听了报告会。",
    "depthfromSeed": 0,
    "count": 0,
    "publishTime": "",
    "tag": "",
    "crawlTime": "20161103020144",
    "type": "",
    "parsedData": "{\"content\":\"     　　10月20日,贵安新区举办主题为“公共安全与突发事件应对”的第19期周末大讲堂,新区党工委副书记、管委会主任孙登峰出席,国务院应急管理专家组组长、国家减灾委专家委员会副主任闪淳昌教授作了专题报告,新区党工委委员、管委会副主任张富杰主持,新区领导欧阳武、李建峰、苏永泓等参加。　　报告中,闪淳昌围绕近年来国内外应对自然灾害、事故灾难、公共卫生事件和社会安全事件案例,结合多年来参与国家应急管理领导工作的实践,以文字、图片、音频、视频等多种形式,从案例介绍、处置过程、处置结果以及经验教训等方面,客观总结了应急管理的经验和教训,深刻阐述了提高应对危机、风险能力的基本要求和方法途径。闪淳昌表示,领导干部思想麻痹是城市公共安全的最大隐患,在抓领导干部守土有责、守土尽责的教育管理上不能失责,要进一步提高领导干部应急管理能力,增强安全防范意识,确保人民生命财产安全。　　报告会结束后,闪淳昌还与现场听众进行了交流互动,畅谈心得体会,对大家提出的问题进行了解答。　　新区分管应急管理、安全生产和相关企业负责等近500人聆听了报告会。\",\"title\":\"贵安新区第19期周末大讲堂举行\",\"infoBar\":\"浏览次数： 次  |  |  | 源作者：聂娜 王溶兰 | 信息来源：贵安新区报 | 发布时间：2016年10月21日17:28   打印本页关闭本页【收藏此页】字体：小中大\"}",
    "url": "http://www.gaxq.gov.cn/xwdt/zrdt_1/20263.shtml",
    "fetched": true,
    "pass": 0,
    "statusCode": 200,
    "startTime": "",
    "author": "",
    "title": "贵安新区第19期周末大讲堂举行",
    "rootUrl": "http://www.gaxq.gov.cn/xwdt/zrdt_1/index.shtml",
    "sourceName": "",
    "fromUrl": "http://www.gaxq.gov.cn/xwdt/zrdt_1/index.shtml",
    "html": "too long to display!!!",
    "tid": "0ec153c8c4dae69ae48420426f3750f6"
    },
    {
    "text": "     　　10月26日,贵安新区党工委副书记、管委会主任孙登峰与中国电子信息集团下属子公司中国信息安全研究院院长孙迎新一行座谈,双方就建立智能制造大数据公共服务平台交流讨论。　　孙登峰代表党工委、管委会对孙迎新一行的到来表示热烈欢迎,对中国电子信息集团对新区的关注和支持表示感谢,对未来双方在大数据领域展开合作表示期待。他说,贵安新区作为贵州省实施大数据战略行动的主战场,近年来抢抓国家实施大数据战略机遇,大力发展大数据产业,相继引进三大运营商、微软、IBM、华为等一批大数据项目,重点打造以大数据为引领的智能终端制造产业集群,努力构建上下游一体化的完整智能终端生态体系。中国电子信息产业集团是中央管理的国有重要骨干企业,以提供电子信息技术产品与服务为主营业务,是中国最大的国有综合性IT企业集团。新区将全力以赴支持中国电子信息集团在新区发展,不断拓展和丰富合作内容,努力实现互利互惠、共赢发展。　　孙迎新对贵安新区高起点谋划、高标准发展表示赞赏,并简要介绍了公司及有关项目的情况。他说,近年来贵安新区发展迅猛,大数据产业发展取得骄人成绩,希望双方今后在大数据与智能制造等方面加强合作,全面推进智能制造大数据公共服务平台的建立,打造“一平台”、“一小镇”、“一基地”、“一基金”的“四位一体”智能制造大数据政产学研用综合体,为新区经济社会发展贡献力量。　　新区相关部门负责人参加座谈会。",
    "depthfromSeed": 0,
    "count": 0,
    "publishTime": "",
    "tag": "",
    "crawlTime": "20161103020145",
    "type": "",
    "parsedData": "{\"content\":\"     　　10月26日,贵安新区党工委副书记、管委会主任孙登峰与中国电子信息集团下属子公司中国信息安全研究院院长孙迎新一行座谈,双方就建立智能制造大数据公共服务平台交流讨论。　　孙登峰代表党工委、管委会对孙迎新一行的到来表示热烈欢迎,对中国电子信息集团对新区的关注和支持表示感谢,对未来双方在大数据领域展开合作表示期待。他说,贵安新区作为贵州省实施大数据战略行动的主战场,近年来抢抓国家实施大数据战略机遇,大力发展大数据产业,相继引进三大运营商、微软、IBM、华为等一批大数据项目,重点打造以大数据为引领的智能终端制造产业集群,努力构建上下游一体化的完整智能终端生态体系。中国电子信息产业集团是中央管理的国有重要骨干企业,以提供电子信息技术产品与服务为主营业务,是中国最大的国有综合性IT企业集团。新区将全力以赴支持中国电子信息集团在新区发展,不断拓展和丰富合作内容,努力实现互利互惠、共赢发展。　　孙迎新对贵安新区高起点谋划、高标准发展表示赞赏,并简要介绍了公司及有关项目的情况。他说,近年来贵安新区发展迅猛,大数据产业发展取得骄人成绩,希望双方今后在大数据与智能制造等方面加强合作,全面推进智能制造大数据公共服务平台的建立,打造“一平台”、“一小镇”、“一基地”、“一基金”的“四位一体”智能制造大数据政产学研用综合体,为新区经济社会发展贡献力量。　　新区相关部门负责人参加座谈会。\",\"title\":\"孙登峰与中国电子信息集团下属子公司中国信息安全研究院院长孙迎新一行座谈\",\"infoBar\":\"浏览次数： 次  |  |  | 源作者：郑芹 | 信息来源：贵安新区报 | 发布时间：2016年10月27日14:54   打印本页关闭本页【收藏此页】字体：小中大\"}",
    "url": "http://www.gaxq.gov.cn/xwdt/zrdt_1/20593.shtml",
    "fetched": true,
    "pass": 0,
    "statusCode": 200,
    "startTime": "",
    "author": "",
    "title": "孙登峰与中国电子信息集团下属子公司中国信息安全研究院院长孙迎新一行座谈",
    "rootUrl": "http://www.gaxq.gov.cn/xwdt/zrdt_1/index.shtml",
    "sourceName": "",
    "fromUrl": "http://www.gaxq.gov.cn/xwdt/zrdt_1/index.shtml",
    "html": "too long to display!!!",
    "tid": "0ec153c8c4dae69ae48420426f3750f6"
    }
    ],
    "nextRow": "0ec153c8c4dae69ae48420426f3750f6|20161103020510|7e03a",
    "size": 2
    }
     */
    @RequestMapping(value = "/getHbaseData", method = RequestMethod.POST)
    public String getHbaseData(String taskId,String startRow,String size)    {
        System.out.println("taskId:"+taskId+"\n"+"startRow:"+startRow+"\n"+"size:"+size);
        String result = handleService.getHBaseData(taskId,startRow,size);
        System.out.println("result:" + "\n" + result);
        return result;
    }


    /**

     @api                {post}      http://ip:port/api/batchHbaseDataGeter  DDP批量获取Hbase数据接口
     @apiGroup           Crawler
     @apiVersion         0.1.0
     @apiParam           {String}  nextSign 批量读取标记，<span style="color:red;font-style: italic">第一次读取时，值为字符串 "0",</span>这一点要非常注意！！！<br/>
                                             当第一次读取过后，返回结果中有 nextSign，需要用户自己存储此值，<br/>
                                             下次取数据时，此 nextSign 要作为请求参数。
     @apiParam           {int}      size     本次读取的数据条数，<span style="color:red;font-style: italic">若不指定数据条数时，值要设置为 0，</span>这一点要非常注意！！！
                                             若指定size的大小，则返回数据的条数可能会若干倍于size，具体看任务的多少而不同。

     @apiSuccess         {boolean}   status  请求结果是否成功
     @apiSuccess         {String}    nextSign 下次请求的批量读取标记，即下次调用接口时当作 nextSign 参数传入，用户需要存储此值。
     @apiSuccess         {int}       size    实际返回数据大小
     @apiSuccess         {json}      data    实际数据，为json数组： [json,json,json,...]

     @apiSuccess          {json}      reason  当 status 为 false 时传回，{"result":false,"reason":"xxx"}

     @apiSuccessExample  {json}      成功返回示例和说明:

     HTTP/1.1 200 OK

     {
        "status":true,                                     // 状态为 true.
        "nextSign":"cc85c9d0e2-1478775672586-15fd9bdc95",   // nextSign.
        "size":1,                                           // 实际返回的数据条数.
        "data":[                                            // 数据列表.
           {                                                // 数据对象 1.
             "author":"XXX",
             "count":0,
             "crawlTime":1478775672681,
             "crawlerdata":{

              },
            "depthfromSeed":0,
            "fetched":true,
            "fromUrl":"http://www.gaxq.gov.cn/xwdt/zrdt_1/index.shtml",
            "html":"XXXXX",
            "tid":"0ec153c8c4dae69ae48420426f3750f6",
            "title":"贵安新区第19期周末大讲堂举行",
            "type":"",
            "url":"http://www.gaxq.gov.cn/xwdt/zrdt_1/20263.shtml"
          }
       ]
     }


     @apiSuccessExample  {json}      失败返回示例和说明:

     HTTP/1.1 200 OK

     {
        "status":false,          // status 为false.
        "reason":"XXXXXXXXXX"    // 错误描述.
     }


     @apiErrorExample {json} 404 错误:
     HTTP/1.1 404 Not Found
     错误信息 XXXXXXXXXXXXXX

     @apiErrorExample {json} 5xx 服务器错误:
     HTTP/1.1 5xx Server Error
     错误信息 XXXXXXXXXXXXXX

     */
    @RequestMapping(value = "/batchHbaseDataGeter", method = RequestMethod.POST)
    public String ddpBatchDataGet(String nextSign, int size){

        return ddpBatchGetService.batchDataGet(nextSign, size);
    }


    /**
     * 待测试
     * @param post_id
     * @return
     */
    @RequestMapping(value = "/testPostJSON", method = RequestMethod.POST)
    public String testPostJSON(String  post_id)  {
        return post_id;
    }
}
