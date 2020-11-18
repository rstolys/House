package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoCallback;

public interface TaskBE {
    public void getCurrentTasks(userInfo uInfo, tInfoArrayCallback callback);
    public void getCurrentTasks(houseInfo hInfo, tInfoArrayCallback callback);
    public void getCurrentTasks(userInfo uInfo, String house_id, tInfoArrayCallback callback);
    public void getTaskInfo(String task_id, tInfoCallback callback);
    public void setTaskInfo(taskInfo tInfo, tInfoCallback callback);
    public void setTaskInfo(taskInfo tInfo, String parameter, tInfoCallback callback);
    public void createTask(taskInfo tInfo, tInfoCallback callback);
    public void deleteTask(taskInfo tInfo, booleanCallback callback);
    public void swapTask(taskInfo tInfoA, taskInfo tInfoB, booleanCallback callback);
}
