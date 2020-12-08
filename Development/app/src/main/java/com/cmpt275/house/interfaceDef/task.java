package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoCallback;
import com.cmpt275.house.interfaceDef.Callbacks.updateCallback;

public interface task {
    void viewTask(String task_id);
    void getTasks(String user_id, updateCallback callback);
    void completeTask(taskInfo tInfo, int taskIndex, updateCallback callback);
    void createTask(taskInfo tInfo, updateCallback callback);
    void assignTask(taskInfo tInfo);
    void editTask(taskInfo tInfo, boolean reassigned, String oldAssignee_id, tInfoCallback callback);
    void deleteTask(taskInfo tInfo, updateCallback callback);
    void displayTask(String task_id);
    void approveTask(String task_id, userInfo uInfo);
    void disputeTask(taskInfo tInfo);
    void requestExtension(taskInfo tInfo, updateCallback callback);
    void getTaskVotes(String task_id);
    void sortTasks(int sortType);

}
