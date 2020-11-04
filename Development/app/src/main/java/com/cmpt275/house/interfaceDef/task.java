package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.taskInfo;

public interface task {
    public taskInfo viewTask(int task_id);
    public boolean createTask(taskInfo tInfo);
    public boolean assignTask(taskInfo tInfo);
    public taskInfo displayTask(int task_id);
    public boolean approveTask(int task_id);
    public taskInfo[] sortTasks(int sortType);

}
