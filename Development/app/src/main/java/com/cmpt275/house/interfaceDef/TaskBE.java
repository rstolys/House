package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoArrayCallback;

//TODO: Update documentation to match new interface
public interface TaskBE {
    void getCurrentTasks(userInfo uInfo, tInfoArrayCallback callback);
    void getCurrentTasks(houseInfo hInfo, tInfoArrayCallback callback);
    void getCurrentTasks(userInfo uInfo, String house_id, tInfoArrayCallback callback);
    void getTaskVotes(String task_id, vInfoArrayCallback callback);
    void approveTaskAssignment(taskInfo tInfo, String user_id, boolean taskApproved, tInfoCallback callback);
    void disputeTask(taskInfo tInfo, tInfoCallback callback);
    void requestExtension(taskInfo tInfo, tInfoCallback callback);
    void getTaskInfo(String task_id, tInfoCallback callback);
    void setTaskInfo(taskInfo tInfo, tInfoCallback callback);
    void setTaskInfo(taskInfo tInfo, String parameter, tInfoCallback callback);
    void createTask(taskInfo tInfo, tInfoCallback callback);
    void deleteTask(taskInfo tInfo, booleanCallback callback);
    void swapTask(taskInfo tInfoA, taskInfo tInfoB, booleanCallback callback);
}
