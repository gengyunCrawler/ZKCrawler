package cn.com.cloudpioneer.zkcrawlerAPI.Test;

import cn.com.cloudpioneer.zkcrawlerAPI.model.elastic.DataResultEntity;

/**
 * Created by TianyuanPan on 11/1/16.
 */
public class TestDataResultEntity {

    public static void main(String[] args) {

        String json = "{\"took\":102,\"timed_out\":false,\"_shards\":{\"total\":2,\"successful\":2,\"failed\":0},\"hits\":{\"total\":3,\"max_score\":0.4288621,\"hits\":[{\"_index\":\"central_resources\",\"_type\":\"cmsdata\",\"_id\":\"1\",\"_score\":0.4288621,\"_source\":{\"taskId\":\"task-id-001\",\"url\":\"url\",\"statusCode\":200,\"rootUrl\":\"root url\",\"fromUrl\":\"from url\",\"title\":\"title\",\"text\":\"text\",\"html\":\"html\",\"publishTime\":\"2016-10-28 15:12:00\",\"crawlTime\":\"2016-10-28 15:12:00\",\"tags\":\"tag-1,tag-2\",\"tags-info\":[{\"name\":\"tag-1\",\"addTime\":\"2016-10-28 15:55:00\"},{\"name\":\"tag-2\",\"addTime\":\"2016-10-28 15:55:00\"}],\"pasedData\":{\"id\":\"id\"},\"extensions\":[{\"_ex_name\":\"name\",\"_ex_type\":\"STRING|OBJECT-STRIGN|ARRAY-STRING|BOOLEAN|DATETIME|INTEGER|FLOAT\",\"_ex_value\":\"value\",\"_ex_description\":\"JSON object\"}]}},{\"_index\":\"central_resources\",\"_type\":\"cmsdata\",\"_id\":\"2\",\"_score\":0.1340194,\"_source\":{\"taskId\":\"task-id-001\",\"url\":\"url\",\"statusCode\":200,\"rootUrl\":\"root url\",\"fromUrl\":\"from url\",\"title\":\"title\",\"text\":\"text\",\"html\":\"html\",\"publishTime\":\"2016-10-28 17:09:02\",\"crawlTime\":\"2016-10-28 17:09:02\",\"tags\":\"tag-4,tag-5\",\"tags-info\":[{\"name\":\"tag-4\",\"addTime\":\"2016-10-28 15:55:00\"},{\"name\":\"tag-5\",\"addTime\":\"2016-10-28 15:55:00\"}],\"pasedData\":{\"id\":\"6idm06o1e4fum6dhimo8tajsjn\",\"content\":\"   扬子晚报讯 （记者 任国勇）女子陈某从四川嫁到南京燕子矶，几年前她离了婚，一直居住在前夫父亲承租的公房内，今年上半年，这处房屋遇到拆迁，根据现有政策和规定，她是拿不到拆迁分房的。但她多次到社区要求帮她免费解决住房，并多次向社区办公区域泼粪施压。10月18日，她竟然向社区干部的轿车上泼粪。18日那天上午8点半，陈某用塑料袋灌着粪便来到燕子矶社区，这时，她见社区干部刚把车停好，于是上前将粪便泼洒在车辆前挡风玻璃上，被当事的社区干部当场逮住。燕子矶派出所赶到现场问询调查。至此，社区干部调出此前陈某向社区办公区域泼洒粪便的监控视频。记者在9月12日下午一段视频中看到，陈某用塑料瓶装着粪便包裹在塑料袋内，来到社区一间会议室门口，将粪便涂塞在门缝里，然后仓皇离开。监控还拍到陈某此后在夜间向社区办公区的大门内，以及门厅花瓶内泼洒粪便，累计5次。陈某因故意侮辱他人，21日，派出所决定对她进行治安拘留3日。目前，燕子矶社区在进一步帮她申请廉租房。（报料人：颜先生）\",\"author\":\"任国勇\",\"title\":\"南京一女子为索取拆迁分房 多次向社区泼粪施压\",\"publishTime\":\"2016-10-26 06:18\",\"tag\":{\"column\":\"腾讯新闻-各地新闻\",\"heat\":\"8\"},\"sourceName\":\"扬子晚报网\"},\"extensions\":[{\"_ex_name\":\"name\",\"_ex_type\":\"STRING|OBJECT-STRIGN|ARRAY-STRING|BOOLEAN|DATETIME|INTEGER|FLOAT\",\"_ex_value\":\"value\",\"_ex_description\":\"JSON object\"}]}}]}}";


        DataResultEntity data = new DataResultEntity();

        data.dataResultBuilder(json);


        System.out.println(data.getResultJsonString());

    }
}
