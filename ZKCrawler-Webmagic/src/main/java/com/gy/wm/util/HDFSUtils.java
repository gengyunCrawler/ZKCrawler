package com.gy.wm.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Tijun on 2016/10/25.
 */
public class HDFSUtils {

    public static synchronized void write(String file, String words) throws IOException, URISyntaxException
    {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(file), conf);
        Path path = new Path(file);
        FSDataOutputStream out = fs.create(path);   //创建文件

        //两个方法都用于文件写入，好像一般多使用后者
       // out.writeBytes(words);
        out.write(words.getBytes("UTF-8"));

        out.close();
        fs.close();

    }

    public static synchronized boolean fileIsExist(String file) throws IOException, URISyntaxException
    {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(file), conf);
        Path path = new Path(file);
        Boolean result = fs.exists(path);
        fs.close();
        return result;

    }
    public static synchronized  void   uploadFile(String src,String file) throws IOException {
        Configuration conf = new Configuration();
        Path destPath = new Path(file);
        FileSystem fs = destPath.getFileSystem(conf);
        fs.copyFromLocalFile(false,new Path(src),destPath);
    }

    public static synchronized   void writeByAppend(String file,String worlds){

        Configuration conf = new Configuration();

        conf.setBoolean("dfs.support.append", true);
        FileSystem fs = null;
        OutputStream out = null;
        InputStream in = null;

        try {
            fs = FileSystem.get(URI.create(file), conf);
            //要追加的文件流，inpath为文件
             in = new ByteArrayInputStream(worlds.getBytes("UTF-8"));
             fs.close();
            fs = FileSystem.get(conf);
            fs.setReplication(new Path(file),(short)1);
             out = fs.append(new Path(file));

            IOUtils.copyBytes(in, out, 4096, true);

        } catch (IOException e) {
            e.printStackTrace();
        }  finally {

            try {
                in.close();
                out.close();
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static  synchronized void delete(String file) throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(file), conf);
        Path path = new Path(file);
        //查看fs的delete API可以看到三个方法。deleteonExit实在退出JVM时删除，下面的方法是在指定为目录是递归删除
        fs.delete(path,true);
        fs.close();
    }

    public static synchronized void download(String file){
        Configuration conf = new Configuration();
        conf.setBoolean("dfs.support.append", true);
        FileSystem fs = null;
        try {
            fs = FileSystem.get(URI.create(file), conf);
           fs.copyToLocalFile(false,new Path("/user/root/icp/news-20161101.txt"),new Path("d:/hdfs-files"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
