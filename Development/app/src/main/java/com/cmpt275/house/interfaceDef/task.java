package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoCallback;
import com.cmpt275.house.interfaceDef.Callbacks.updateCallback;

public interface task {
    void getTasks(String user_id, updateCallback callback);
    void completeTask(taskInfo tInfo, int taskIndex, updateCallback callback);
    void createTask(taskInfo tInfo, userInfo uInfo, updateCallback callback);
    void editTask(taskInfo tInfo, userInfo uInfo, boolean reassigned, String oldAssignee_id, tInfoCallback callback);
    void deleteTask(taskInfo tInfo, updateCallback callback);
    void approveTask(taskInfo tInfo, userInfo uInfo, boolean approval, int taskIndex, updateCallback callback);
    void requestExtension(taskInfo tInfo, updateCallback callback);

}
