package cn.com.cloudpioneer.zkcrawlerAPI.app.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * Created by TianyuanPan on 10/31/16.
 */
public class ESClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESClient.class);

    private static String[] hostPort;

    static {

        try {
            String configs = ResourceBundle.getBundle("config").getString("ELASTICSEARCH_HOSTS");
            hostPort = configs.split(",");
        } catch (Exception e) {
            LOGGER.error("解析 ES 主机的配置错误！！");
        }
    }

    public static RestClient getRestClientInstance() {

        if (hostPort == null || hostPort.length == 0) {
            LOGGER.error("the ES hosts is not set.");
            return null;
        }
        HttpHost httpHosts[] = new HttpHost[hostPort.length];

        for (int i = 0; i < hostPort.length; i++) {
            httpHosts[i] = new HttpHost(
                    hostPort[i].split(":")[0],
                    Integer.parseInt(hostPort[i].split(":")[1]),
                    "http"
            );
        }

        RestClient client = RestClient.builder(httpHosts).build();

        return null;
    }
}
