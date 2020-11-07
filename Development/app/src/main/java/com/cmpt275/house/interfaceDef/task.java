package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.taskInfo;
import com.cmpt275.house.classDef.userInfo;

public interface task {
    public void viewTask(String task_id);
    public void createTask(taskInfo tInfo);
    public void assignTask(taskInfo tInfo);
    public void editTask(taskInfo tInfo);
    public void deleteTask(taskInfo tInfo);
    public void displayTask(String task_id);
    public void approveTask(String task_id, userInfo uInfo);
    public void sortTasks(int sortType);

}
