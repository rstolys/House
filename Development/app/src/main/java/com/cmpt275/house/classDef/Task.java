package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.task;

class Task implements task {
    @Override
    public taskInfo viewTask(int task_id) {
        return null;
    }

    @Override
    public boolean createTask(taskInfo tInfo) {
        return false;
    }

    @Override
    public boolean assignTask(taskInfo tInfo) {
        return false;
    }

    @Override
    public taskInfo displayTask(int task_id) {
        return null;
    }

    @Override
    public boolean approveTask(int task_id) {
        return false;
    }

    @Override
    public taskInfo[] sortTasks(int sortType) {
        return new taskInfo[0];
    }
}
