package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.task;

class taskClass implements task {

    //
    // Class Variables
    //
    private taskInfo[] tInfos;
    private votingInfo vInfo;

    //
    // Class Functions
    //
    public taskInfo viewTask(int task_id) {
        return null;
    }

    public boolean createTask(taskInfo tInfo) {
        return false;
    }

    public boolean assignTask(taskInfo tInfo) {
        return false;
    }

    public taskInfo displayTask(int task_id) {
        return null;
    }

    public boolean approveTask(int task_id) {
        return false;
    }

    public taskInfo[] sortTasks(int sortType) {
        return new taskInfo[0];
    }
}
