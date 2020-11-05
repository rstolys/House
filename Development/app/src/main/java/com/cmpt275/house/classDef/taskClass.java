package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.classDef.taskInfo;

import com.cmpt275.house.interfaceDef.task;
import com.cmpt275.house.interfaceDef.taskCallbacks;

import java.util.Date;

public class taskClass implements task, taskCallbacks {

    //
    // Class Variables
    //
    private taskInfo[] tInfos;
    private votingInfo vInfo;

    private taskFirebaseClass firebaseTask;

    //
    // Class Functions
    //
    public taskClass() {
        firebaseTask = new taskFirebaseClass(this);
    }


    public taskInfo viewTask(int task_id) {
        return null;
    }

    public boolean createTask(taskInfo tInfo) {
        taskInfo myTInfo = new taskInfo();

        //Set parameters for testing
        myTInfo.id = null;
        myTInfo.name = "Emulator Test Task";
        myTInfo.description = "I really hope this works";
        myTInfo.createdBy = "NO_IDs_HAVE_BEEN_SET";
        myTInfo.status = 1;
        myTInfo.assignedTo = new String[] {"NO_IDs_HAVE_BEEN_SET"};
        myTInfo.houseName = "DevHouse";
        myTInfo.house_id = "NO_IDs_HAVE_BEEN_SET";
        myTInfo.costAssociated = 0;
        myTInfo.difficultyScore = 5;
        myTInfo.dueDate = new Date();
        myTInfo.itemList = new String[] {"Test1", "Test2", "Test3"};
        myTInfo.notificationTime = new Date();
        myTInfo.tag = new Integer[] {1, 3, 5};


        Log.d("TEST","Calling firebase");
        firebaseTask.createTask(myTInfo);
        Log.d("TEST","Returned from firebase");

        return true;
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

    public void onTaskInfoReturn(taskInfo tInfo, String functionName) {
        //Deal with callback here
        Log.d("TEST", "Return from " + functionName);
        Log.d("TEST", "is tInfo is null: " + (tInfo == null));
    }
}
