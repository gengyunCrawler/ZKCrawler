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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/19.
 */
public class GetTaskConfig {

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


    public static Map<String, List<TaskConfigEntity>> configsParser(List<TaskConfigEntity> taskConfigEntities) {

        Map<String, List<TaskConfigEntity>> configs = new HashMap<>();

        for (TaskConfigEntity item : taskConfigEntities) {

            switch (item.getConfType()) {
                case ConfigType.SEED_URLS:
                    if (configs.get(ConfigType.SEED_URLS) == null)
                        configs.put(ConfigType.SEED_URLS, new ArrayList<TaskConfigEntity>());
                    configs.get(ConfigType.SEED_URLS).add(item);
                    break;
                case ConfigType.TEMPLATES:
                    if (configs.get(ConfigType.TEMPLATES) == null)
                        configs.put(ConfigType.TEMPLATES, new ArrayList<TaskConfigEntity>());
                    configs.get(ConfigType.TEMPLATES).add(item);
                    break;
                case ConfigType.TAGS:
                    if (configs.get(ConfigType.TAGS) == null)
                        configs.put(ConfigType.TAGS, new ArrayList<TaskConfigEntity>());
                    configs.get(ConfigType.TAGS).add(item);
                    break;
                case ConfigType.CONFIGS:
                    if (configs.get(ConfigType.CONFIGS) == null)
                        configs.put(ConfigType.CONFIGS, new ArrayList<TaskConfigEntity>());
                    configs.get(ConfigType.CONFIGS).add(item);
                    break;
                case ConfigType.CLICK_REGEX:
                    if (configs.get(ConfigType.CLICK_REGEX) == null)
                        configs.put(ConfigType.CLICK_REGEX, new ArrayList<TaskConfigEntity>());
                    configs.get(ConfigType.CLICK_REGEX).add(item);
                    break;
                case ConfigType.REGEX_FILTER:
                    if (configs.get(ConfigType.REGEX_FILTER) == null)
                        configs.put(ConfigType.REGEX_FILTER, new ArrayList<TaskConfigEntity>());
                    configs.get(ConfigType.REGEX_FILTER).add(item);
                    break;
                case ConfigType.PROTOCOL_FILTER:
                    if (configs.get(ConfigType.PROTOCOL_FILTER) == null)
                        configs.put(ConfigType.PROTOCOL_FILTER, new ArrayList<TaskConfigEntity>());
                    configs.get(ConfigType.PROTOCOL_FILTER).add(item);
                    break;
                case ConfigType.SUFFIX_FILTER:
                    if (configs.get(ConfigType.SUFFIX_FILTER) == null)
                        configs.put(ConfigType.SUFFIX_FILTER, new ArrayList<TaskConfigEntity>());
                    configs.get(ConfigType.SUFFIX_FILTER).add(item);
                    break;
                case ConfigType.DOWNLOADER:
                    if (configs.get(ConfigType.DOWNLOADER) == null)
                        configs.put(ConfigType.DOWNLOADER, new ArrayList<TaskConfigEntity>());
                    configs.get(ConfigType.DOWNLOADER).add(item);
                    break;
                case ConfigType.URL_PARSER:
                    if (configs.get(ConfigType.URL_PARSER) == null)
                        configs.put(ConfigType.URL_PARSER, new ArrayList<TaskConfigEntity>());
                    configs.get(ConfigType.URL_PARSER).add(item);
                    break;
                case ConfigType.PAGE_PARSER:
                    if (configs.get(ConfigType.PAGE_PARSER) == null)
                        configs.put(ConfigType.PAGE_PARSER, new ArrayList<TaskConfigEntity>());
                    configs.get(ConfigType.PAGE_PARSER).add(item);
                    break;
                case ConfigType.PROXY:
                    if (configs.get(ConfigType.PROXY) == null)
                        configs.put(ConfigType.PROXY, new ArrayList<TaskConfigEntity>());
                    configs.get(ConfigType.PROXY).add(item);
                    break;
                case ConfigType.UNDEFINE:
                default:
                    if (configs.get(ConfigType.UNDEFINE) == null)
                        configs.put(ConfigType.UNDEFINE, new ArrayList<TaskConfigEntity>());
                    configs.get(ConfigType.UNDEFINE).add(item);
                    break;
            }
        }

        return configs;

    }


}
