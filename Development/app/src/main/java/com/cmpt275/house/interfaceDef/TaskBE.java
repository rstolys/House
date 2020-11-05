package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.houseInfo;
import com.cmpt275.house.classDef.taskInfo;
import com.cmpt275.house.classDef.userInfo;

public interface TaskBE {
    public void getCurrentTasks(userInfo uInfo);
    public void getCurrentTasks(houseInfo hInfo);
    public void getCurrentTasks(userInfo uInfo, int house_id);
    public void getTaskInfo(String task_id);
    public void setTaskInfo(taskInfo tInfo);
    public void setTaskInfo(taskInfo tInfo, String parameter);
    public void createTask(taskInfo tInfo);
    public void deleteTask(taskInfo tInfo);
    public void swapTask(taskInfo tInfoA, taskInfo tInfoB);
}
