package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.houseInfo;
import com.cmpt275.house.classDef.taskInfo;
import com.cmpt275.house.classDef.userInfo;

public interface TaskBE {
    public int[] getCurrentTasks(userInfo uInfo);
    public int[] getCurrentTasks(houseInfo hInfo);
    public int[] getCurrentTasks(userInfo uInfo, int house_id);
    public taskInfo getTaskInfo(int task_id);
    public taskInfo setTaskInfo(taskInfo tInfo);
    public taskInfo setTaskInfo(taskInfo tInfo, int parameter);
    public taskInfo createTask(taskInfo tInfo);
    public boolean swapTask(taskInfo tInfoA, taskInfo tInfoB);
}
