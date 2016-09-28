package cn.com.cloudpioneer.starter.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.*;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by TianyuanPan on 2016/9/27.
 */
public class ShellUtils {

    private JSch jsch;
    private Session session;
    private Channel channel;

    private InputStream instream;
    private OutputStream outstream;

    private String hostname;
    private int port = 22;
    private String username;
    private String password;


    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static ShellUtils getShellUtils(String username, String password, String hostname, int port) {

        ShellUtils shell;

        try {
            shell = new ShellUtils(username, password, hostname, port);

        } catch (Exception ex) {

            ex.printStackTrace();
            return null;
        }

        return shell;

    }


    private ShellUtils(String username, String password, String hostname, int port) throws JSchException, IOException {

        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;

        jsch = new JSch();

    }


    public String doExecuteShell(String command) {

        String outString = "";
        String cmdString = command;

        if (cmdString.length() == 0 || cmdString.charAt(cmdString.length() - 1) != '\n')
            cmdString = cmdString + "\n";

        try {

            session = jsch.getSession(this.username, this.hostname, this.port);
            //如果服务器连接不上，则抛出异常
            if (session == null) {

                throw new JSchException("session is null");
            }

            //设置登陆主机的密码
            session.setPassword(this.password);//设置密码
            //设置第一次登陆的时候提示，可选值：(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect(30 * 1000);
            channel = session.openChannel("exec");

            ((ChannelExec) channel).setCommand(cmdString);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            this.instream = channel.getInputStream();

            channel.connect(30 * 1000);

            byte[] tmp = new byte[1024];
            while (true) {

                while (this.instream.available() > 0) {

                    int i = this.instream.read(tmp, 0, 1024);
                    if (i < 0) break;
                    outString += new String(tmp, 0, i, "utf-8");
//                    System.out.print(new String(tmp, 0, i));
                }

                if (channel.isClosed()) {
                    if (this.instream.available() > 0) continue;

//                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {

                    Thread.sleep(200);

                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }

            instream.close();
            channel.disconnect();
            session.disconnect();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return outString;
    }


    public void openPerformShell() {

        try {
            session = jsch.getSession(this.username, this.hostname, this.port);

            //如果服务器连接不上，则抛出异常
            if (session == null) {

                throw new JSchException("session is null");
            }

            //设置登陆主机的密码
            session.setPassword(this.password);//设置密码

            //设置第一次登陆的时候提示，可选值：(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");
            //设置登陆超时时间
            session.connect(30 * 1000);
            //创建sftp通信通道
            channel = session.openChannel("shell");

            this.outstream = this.channel.getOutputStream();
            this.instream = this.channel.getInputStream();

            this.channel.connect(30 * 1000);

//            byte[] login = new byte[64];
//            instream.read(login);
//            System.out.println("LOGIN: " + new String(login));

        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }


    public InputStream performShell(String command) {


        String cmdString = command;

        if (cmdString.length() == 0 || cmdString.charAt(cmdString.length() - 1) != '\n')
            cmdString = cmdString + "\n";

        try {

            outstream.write(cmdString.getBytes());
            outstream.flush();

        } catch (IOException ex1) {

            ex1.printStackTrace();

        }

        return this.instream;
    }


    public void closePerformShell() {

        try {
            outstream.close();
            instream.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        } finally {

            this.channel.disconnect();
            this.session.disconnect();
        }
    }


}
