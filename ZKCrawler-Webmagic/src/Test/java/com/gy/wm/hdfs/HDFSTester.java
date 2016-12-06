package com.gy.wm.hdfs;

import com.gy.wm.util.HDFSUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/10/25.
 */
public class HDFSTester
{
    @Test
    public void testConnectHDFS() throws IOException {

        Configuration config = new Configuration();
       // config.set("hadoop.home.dir","D:\\Tool\\hadoop2.6_Win_x64");
        FileSystem fs = FileSystem.get(URI.create("/user"), config);
        // 列出hdfs上/user/fkong/目录下的所有文件和目录
        FileStatus[] statuses = fs.listStatus(new Path("/user"));
        for (FileStatus status : statuses) {
            System.out.println(status);
        }

    }

    @Test
    public void testWriteIn() throws IOException, URISyntaxException {
        String sss="16:50:40.965 [DataStreamer for file /user/root/icp/test.txt block BP-1569678275-108.108.108.15-1437955877167:blk_1074356972_670467] DEBUG org.apache.hadoop.hdfs.DFSClient - DataStreamer block BP-1569678275-108.108.108.15-1437955877167:blk_1074356972_670467 sending packet packet seqno:0 offsetInBlock:0 lastPacketInBlock:false lastByteOffsetInBlock: 20\n" +
                "16:50:41.037 [ResponseProcessor for block BP-1569678275-108.108.108.15-1437955877167:blk_1074356972_670467] DEBUG org.apache.hadoop.hdfs.DFSClient - DFSClient seqno: 0 status: SUCCESS status: SUCCESS status: SUCCESS downstreamAckTimeNanos: 5376163\n" +
                "16:50:41.037 [DataStreamer for file /user/root/icp/test.txt block BP-1569678275-108.108.108.15-1437955877167:blk_1074356972_670467] DEBUG org.apache.hadoop.hdfs.DFSClient - DataStreamer block BP-1569678275-108.108.108.15-1437955877167:blk_1074356972_670467 sending packet packet seqno:1 offsetInBlock:20 lastPacketInBlock:true lastByteOffsetInBlock: 20\n" +
                "16:50:41.177 [ResponseProcessor for block BP-1569678275-108.108.108.15-1437955877167:blk_1074356972_670467] DEBUG org.apache.hadoop.hdfs.DFSClient - DFSClient seqno: 1 status: SUCCESS status: SUCCESS status: SUCCESS downstreamAckTimeNanos: 46982279\n" +
                "16:50:41.178 [DataStreamer for file /user/root/icp/test.txt block BP-1569678275-108.108.108.15-1437955877167:blk_1074356972_670467] DEBUG org.apache.hadoop.hdfs.DFSClient - Closing old block BP-1569678275-108.108.108.15-1437955877167:blk_1074356972_670467\n" +
                "16:50:41.184 [IPC Parameter Sending Thread #0] DEBUG org.apache.hadoop.ipc.Client - IPC Client (971766834) connection to hdp-master/108.108.108.15:8020 from Administrator sending #3\n" +
                "16:50:41.205 [IPC Client (971766834) connection to hdp-master/108.108.108.15:8020 from Administrator] DEBUG org.apache.hadoop.ipc.Client - IPC Client (971766834) connection to hdp-master/108.108.108.15:8020 from Administrator got value #3\n" +
                "16:50:41.205 [main] DEBUG org.apache.hadoop.ipc.ProtobufRpcEngine - Call: complete took 23ms\n" +
                "16:50:41.214 [Thread-2] DEBUG org.apache.hadoop.ipc.Client - stopping client from cache: org.apache.hadoop.ipc.Client@6f1c5be4\n" +
                "16:50:41.214 [Thread-2] DEBUG org.apache.hadoop.ipc.Client - removing client from cache: org.apache.hadoop.ipc.Client@6f1c5be4\n" +
                "16:50:41.214 [Thread-2] DEBUG org.apache.hadoop.ipc.Client - stopping actual client because no more references remain: org.apache.hadoop.ipc.Client@6f1c5be4\n" +
                "16:50:41.215 [Thread-2] DEBUG org.apache.hadoop.ipc.Client - Stopping client\n" +
                "16:50:41.215 [IPC Client (971766834) connection to hdp-master/108.108.108.15:8020 from Administrator] DEBUG org.apache.hadoop.ipc.Client - IPC Client (971766834) connection to hdp-master/108.108.108.15:8020 from Administrator: closed\n" +
                "16:50:41.215 [IPC Client (971766834) connection to hdp-master/108.108.108.15:8020 from Administrator] DEBUG org.apache.hadoop.ipc.Client - IPC Client (971766834) connection to hdp-master/108.108.108.15:8020 from Administrator: stopped, remaining connections 0\n";
        HDFSUtils.writeByAppend("/user/root//icp/test.txt",sss);
    }

    @Test
    public void delete() throws IOException {
        HDFSUtils.delete("/user/root/icp/news-20161114.txt");
    }

    @Test
    public void testAppend(){
        String content="dsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss";
        HDFSUtils.writeByAppend("/user/root/icp/news-20161101.txt",content);
    }
    @Test
    public void testCheck() throws IOException, URISyntaxException {
       boolean b= HDFSUtils.fileIsExist("/user/root/icp/news-20161101.txt");
        System.out.println(b);
    }
    @Test
    public void testGet(){
      Calendar calendar=  Calendar.getInstance();
        String d=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        System.out.println(d);

    }
    @Test
    public void testUpload() throws IOException {
     HDFSUtils.uploadFile("d:/news-20161102.txt","/user/root/icp/");
    }

    @Test
    public void testDownload(){

    }

    @Test
    public void testRegex(){
        Pattern pattern = Pattern.compile("http://www.guiz.com/\\w+/");
        String ur = "http://www.guiz.com/gzu/ss";
        Matcher matcher = pattern.matcher(ur);
        System.out.println(matcher.matches());
    }
}
