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


    public void viewTask(String task_id) {
        return;
    }

    public void createTask(taskInfo tInfo) {
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


        firebaseTask.createTask(myTInfo);

        return;
    }

    public void assignTask(taskInfo tInfo) {
        return;
    }

    public void editTask(taskInfo tInfo) {
        taskInfo myTInfo = new taskInfo();

        //Set parameters for testing
        myTInfo.id = "49DxUwHNYZ4wd3lQ1qd4";
        myTInfo.name = "Emulator Test Task Changed";
        myTInfo.description = "I really hope this works";
        myTInfo.createdBy = "SOME_IDS_ARE_SET";
        myTInfo.status = 2;
        myTInfo.assignedTo = new String[] {"NO_IDs_HAVE_BEEN_SET"};
        myTInfo.houseName = "TEST_HOUSE_TEST";
        myTInfo.house_id = "NO_IDs_HAVE_BEEN_SET";
        myTInfo.costAssociated = 0;
        myTInfo.difficultyScore = 5;
        myTInfo.dueDate = new Date();
        myTInfo.itemList = new String[] {"Test1", "Test2", "Test3"};
        myTInfo.notificationTime = new Date();
        myTInfo.tag = new Integer[] {1, 3, 5};


        firebaseTask.setTaskInfo(myTInfo, "status");

        return;
    }

    public void deleteTask(taskInfo tInfo) {
        taskInfo myTInfo = new taskInfo();

        //Each of these parameters are required to remove a task
        myTInfo.id = "49DxUwHNYZ4wd3lQ1qd4";                            //Required
        myTInfo.createdBy = "SOME_IDS_ARE_SET";                         //Required
        myTInfo.status = 2;                                             //Required
        myTInfo.assignedTo = new String[] {"NO_IDs_HAVE_BEEN_SET"};     //Required
        myTInfo.house_id = "NO_IDs_HAVE_BEEN_SET";                      //Required

        firebaseTask.deleteTask(myTInfo);

        return;
    }

    public void displayTask(String task_id) {
        //Get the task to display
        firebaseTask.getTaskInfo(task_id);

        //Will handle return in callback function
        return;
    }

    public void approveTask(String task_id) {
        return;
    }

    public void sortTasks(int sortType) {
        return;
    }


    public void onTaskInfoArrayReturn(taskInfo[] tInfo, String functionName) { return; }

    public void onTaskInfoReturn(taskInfo tInfo, String functionName) {

        Log.d("TaskInfoReturn:", "Return from " + functionName);
        Log.d("TaskInfoReturn:", "tInfo is " + tInfo.id);

        switch(functionName) {
            case "createTask":
                //Call function to deal with this result
                break;

            case "getTaskInfo":
                //Call function to deal with this result
                break;

            case "setTaskInfo(1)":
                //Call function to deal with this result
                break;

            default:
                break;
        }

        return;
    }

    public void onTaskBooleanReturn(boolean result, String functionName) {

        Log.d("TaskBooleanReturn:", "Return from " + functionName + " is " + result);

        switch(functionName) {
            case "deleteTask":
                //Call function to deal with this result
                break;

            default:
                break;
        }

        return;
    }
}
