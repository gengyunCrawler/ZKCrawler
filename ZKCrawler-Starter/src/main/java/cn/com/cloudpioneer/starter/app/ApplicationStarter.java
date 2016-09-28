package cn.com.cloudpioneer.starter.app;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ApplicationStarter {


    private static void usage() {
        System.out.println("Usage:\n\t <option>  <value>");
        System.out.println("option:  -f <the host configuration json file.>\n\t -s <the host configuration json string.>");
        System.out.println("value: the file name or the string.\n\nexample:\n\t-f ./hostInfo.json\nor:\n\t-s [{\"hostname\":\"192.168.1.110\",\"port\",22,\"username\":\"username\",\"password\":\"password\"},{\"hostname\":\"192.168.1.100\",\"port\",22,\"username\":\"username\",\"password\":\"password\"}]");
        System.out.println("\nEnd.\n");
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {

        if (args == null || args.length < 2) {
            usage();
        }

        ZKCrawlerStarter starter = new ZKCrawlerStarter();

        String hostInfo = "";

        switch (args[0]) {
            case "-f":
                hostInfo = args[1];
                starter.initializeHostInfoFromFile(hostInfo);
                break;
            case "-s":
                for (int i = 1; i < args.length; i++)
                    hostInfo += args[i];
                starter.initializeHostInfoFromString(hostInfo);
                break;
            default:
                usage();
                break;
        }

        starter.startZKCrawlerNodes();

        while (ZKCrawlerStarter.getNumberOfHost() > 0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("==++==--++==> now, all roles started, exit starter.");
        System.exit(0);
    }


}
