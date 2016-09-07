package cn.com.cloudpioneer.taskclient.chooser.chooserImpl;

import cn.com.cloudpioneer.taskclient.chooser.TaskChooser;
import cn.com.cloudpioneer.taskclient.entity.TaskEntity;

import java.util.List;

/**
 * Created by Tianjinjin on 2016/9/2.
 */
public class LongTimeFirstTaskChooser implements TaskChooser{

    private List<TaskEntity> taskEntities;

    @Override
    public List<TaskEntity> chooser(int size) {
        return null;
    }


    private List<TaskEntity> selectAllTask(){

        return null;
    }

    private void sortByTimeLastCrawl(){

    }

    private List<TaskEntity> getBySize(int size){
        return null;
    }
}
