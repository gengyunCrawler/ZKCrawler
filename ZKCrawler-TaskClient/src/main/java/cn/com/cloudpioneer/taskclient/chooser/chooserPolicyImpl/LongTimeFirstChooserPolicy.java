package cn.com.cloudpioneer.taskclient.chooser.chooserPolicyImpl;

import cn.com.cloudpioneer.taskclient.chooser.ChooserPolicy;
import cn.com.cloudpioneer.taskclient.dao.TaskDao;
import cn.com.cloudpioneer.taskclient.model.TaskEntity;
import cn.com.cloudpioneer.taskclient.model.TaskStatusItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tianjinjin on 2016/9/2.
 */
public class LongTimeFirstChooserPolicy implements ChooserPolicy {

    private List<TaskEntity> taskEntities;

    @Override
    public List<TaskEntity> chooser(int size) {
        taskEntities = selectAllTask();
        cycleRecrawlFilter();
        sortByTimeLastCrawlSmallToBig();
        return getBySize(size);
    }

    @Override
    public String getPolicyName() {
        return "LongTimeFirstChooserPolicy";
    }


    private void cycleRecrawlFilter() {

        List<TaskEntity> entities = new ArrayList<>();

        for (TaskEntity item : taskEntities) {
            int hour = (int) ((new Date().getTime() - item.getTimeLastCrawl().getTime()) / 1000 / 60 / 60);
            if (hour >= item.getCycleRecrawl())
                entities.add(item);
        }
        taskEntities = entities;

    }


    private List<TaskEntity> selectAllTask() {
        TaskDao dao = new TaskDao();
        return dao.findAllValidByStatus(TaskStatusItem.TASK_STATUS_COMPLETED);
    }

    /**
     * 冒泡排序，按最后爬取时间从小到大从小到大。
     */
    private void sortByTimeLastCrawlSmallToBig() {

        int length = taskEntities.size();
        TaskEntity[] entityArray = new TaskEntity[length];

        int i = 0;
        for (TaskEntity item : taskEntities) {
            entityArray[i] = item;
            i++;
        }

        for (i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                if (entityArray[i].getTimeLastCrawl().getTime() > entityArray[j].getTimeLastCrawl().getTime()) {
                    TaskEntity tmp = entityArray[j];
                    entityArray[i] = entityArray[j];
                    entityArray[j] = tmp;
                }
            }
        }

        taskEntities = new ArrayList<>();

        for (i = 0; i < length; i++)
            taskEntities.add(entityArray[i]);

    }


    private List<TaskEntity> getBySize(int size) {
        int listSize = taskEntities.size();
        if (size > listSize)
            return taskEntities;
        else
            return taskEntities.subList(0, size);
    }
}
