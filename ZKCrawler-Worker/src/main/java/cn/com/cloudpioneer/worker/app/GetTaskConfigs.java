package cn.com.cloudpioneer.worker.app;

import cn.com.cloudpioneer.worker.mapper.TaskConfigEntityMapper;
import cn.com.cloudpioneer.worker.model.ConfigType;
import cn.com.cloudpioneer.worker.model.TaskConfigEntity;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 2016/9/19.
 */
public class GetTaskConfigs {

    private static Reader reader;
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }


    public List<TaskConfigEntity> findConfigs(String idTask) {

        SqlSession session = sqlSessionFactory.openSession();

        TaskConfigEntityMapper mapper = session.getMapper(TaskConfigEntityMapper.class);
        List<TaskConfigEntity> configs = mapper.findTaskConfigsById(idTask);
        session.commit();
        session.close();

        return configs;
    }


    public static Map<String, TaskConfigEntity> configsParser(List<TaskConfigEntity> taskConfigEntities) {

        Map<String, TaskConfigEntity> configs = new HashMap<>();

        for (TaskConfigEntity item : taskConfigEntities) {

            switch (item.getConfType()) {

                // 种子url配置项.
                case ConfigType.SEED_URLS:
                    configs.put(ConfigType.SEED_URLS, item);
                    break;

                // 模板配置项.
                case ConfigType.TEMPLATES:
                    configs.put(ConfigType.TEMPLATES, item);
                    break;

                // configs 配置项.
                case ConfigType.CONFIGS:
                    configs.put(ConfigType.CONFIGS, item);
                    break;

                // 点击正则配置项.
                case ConfigType.CLICK_REGEX:
                    configs.put(ConfigType.CLICK_REGEX, item);
                    break;

                // 正则过滤器配置项.
                case ConfigType.REGEX_FILTER:
                    configs.put(ConfigType.REGEX_FILTER, item);
                    break;

                // 协议过滤器配置项.
                case ConfigType.PROTOCOL_FILTER:
                    configs.put(ConfigType.PROTOCOL_FILTER, item);
                    break;

                // 后缀过滤器配置项.
                case ConfigType.SUFFIX_FILTER:
                    configs.put(ConfigType.SUFFIX_FILTER, item);
                    break;

                // 代理配置项.
                case ConfigType.PROXY:
                    configs.put(ConfigType.PROXY, item);
                    break;

                // 标签配置项.
                case ConfigType.TAGS:
                    configs.put(ConfigType.TAGS, item);
                    break;

                // 分类，栏目(频道)配置项.
                case ConfigType.CATEGORIES:
                    configs.put(ConfigType.CATEGORIES, item);
                    break;

                // 未定义配置项.
                case ConfigType.UNDEFINE:

                default:
                    configs.put(ConfigType.UNDEFINE, item);
                    break;
            }
        }

        return configs;

    }

}
