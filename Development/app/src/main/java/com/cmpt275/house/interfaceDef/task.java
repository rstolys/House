package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;

public interface task {
    void viewTask(String task_id);
    void createTask(taskInfo tInfo);
    void assignTask(taskInfo tInfo);
    void editTask(taskInfo tInfo);
    void deleteTask(taskInfo tInfo);
    void displayTask(String task_id);
    void approveTask(String task_id, userInfo uInfo);
    void disputeTask(taskInfo tInfo);
    void requestExtension(taskInfo tInfo);
    void getTaskVotes(String task_id);
    void sortTasks(int sortType);

}
