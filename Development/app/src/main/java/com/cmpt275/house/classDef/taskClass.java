package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.classDef.databaseObjects.nameObj;
import com.cmpt275.house.classDef.databaseObjects.taskAssignObj;
import com.cmpt275.house.classDef.taskInfo;

import com.cmpt275.house.interfaceDef.mapping;
import com.cmpt275.house.interfaceDef.task;
import com.cmpt275.house.interfaceDef.taskCallbacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class taskClass implements task, taskCallbacks {

    //
    // Class Variables
    //
    private taskInfo[] tInfos;

    private taskFirebaseClass firebaseTask;

    //
    // Class Functions
    //
    public taskClass() {
        firebaseTask = new taskFirebaseClass(this);
    }


    public void viewTask(String task_id) {

        userInfo myUInfo = new userInfo();
        houseInfo myHInfo = new houseInfo();

        myUInfo.id = "NO_ID";
        myHInfo.id = "NO_IDs_HAVE_BEEN_SET";

        firebaseTask.getCurrentTasks(myUInfo);
        firebaseTask.getCurrentTasks(myHInfo);
        firebaseTask.getCurrentTasks(myUInfo, myHInfo.id);
        return;
    }

    public void createTask(taskInfo tInfo) {

        //Testing
        taskInfo myTInfo = new taskInfo();

        //Set parameters for testing
        myTInfo.id = null;
        myTInfo.displayName = "Emulator Test Task";
        myTInfo.description = "I really hope this works";
        myTInfo.createdBy = "Ryan Stolys";
        myTInfo.createdBy_id = "NO_IDs_HAVE_BEEN_SET";

        //Set status value
        mapping statusMap = new statusMapping();
        myTInfo.status = statusMap.mapIntToString(5);

        myTInfo.assignedTo.put("NO_ID", new taskAssignObj("Ryan Stolys", true, false));
        myTInfo.houseName = "DevHouse";
        myTInfo.house_id = "NO_IDs_HAVE_BEEN_SET";
        myTInfo.costAssociated = 0;
        myTInfo.difficultyScore = 5;
        myTInfo.dueDate = new Date();
        myTInfo.itemList.add("Test1");
        myTInfo.itemList.add("Test2");
        myTInfo.itemList.add("Test3");
        myTInfo.notificationTime = new Date();
        myTInfo.tag.add("Garbage");
        myTInfo.tag.add("Kitchen");
        myTInfo.tag.add("Cleaning");


        firebaseTask.createTask(myTInfo);

        return;
    }

    public void assignTask(taskInfo tInfo) {
        return;
    }

    public void editTask(taskInfo tInfo) {

        //Do Stuff First
        //Testing
        taskInfo myTInfo = new taskInfo();

        //Set parameters for testing
        myTInfo.id = "4W9wvRhStX55SKEFYSqJ";
        myTInfo.displayName = "Emulator Test Task";
        myTInfo.description = "I really hope this works";
        myTInfo.createdBy = "Ryan Stolys";
        myTInfo.createdBy_id = "NO_IDs_HAVE_BEEN_SET";

        //Set status value
        mapping statusMap = new statusMapping();
        myTInfo.status = statusMap.mapIntToString(3);

        myTInfo.assignedTo.put("NO_ID", new taskAssignObj("Ryan Stolys", true, false));
        myTInfo.houseName = "DevHouse";
        myTInfo.house_id = "NO_IDs_HAVE_BEEN_SET";
        myTInfo.costAssociated = 0;
        myTInfo.difficultyScore = 5;
        myTInfo.dueDate = new Date();
        myTInfo.itemList.add("Test1");
        myTInfo.itemList.add("Test2");
        myTInfo.itemList.add("Test3");
        myTInfo.notificationTime = new Date();
        myTInfo.tag.add("Garbage");
        myTInfo.tag.add("Kitchen");
        myTInfo.tag.add("Cleaning");

        //Set Parameter
        String paramToChange = "status";
        firebaseTask.setTaskInfo(myTInfo, paramToChange);

        return;
    }

    public void deleteTask(taskInfo tInfo) {
        taskInfo myTInfo = new taskInfo();

        //Each of these parameters are required to remove a task
        myTInfo.id = "4W9wvRhStX55SKEFYSqJ";
        myTInfo.createdBy_id = "SOME_IDS_ARE_SET";

        //Set status value
        mapping statusMap = new statusMapping();
        myTInfo.status = statusMap.mapIntToString(2);

        myTInfo.assignedTo.put("NO_ID", new taskAssignObj("Ryan Stolys", true, false));
        myTInfo.house_id = "NO_IDs_HAVE_BEEN_SET";

        firebaseTask.deleteTask(myTInfo);

        return;
    }

    public void displayTask(String task_id) {
        //Get the task to display
        firebaseTask.getTaskInfo(task_id);

        //Will handle return in callback function
        return;
    }

    public void approveTask(String task_id, userInfo uInfo) {

        taskInfo myTInfo = new taskInfo();

        myTInfo.id = "4W9wvRhStX55SKEFYSqJ";
        myTInfo.displayName = "Emulator Test Task";
        myTInfo.description = "I really hope this works";
        myTInfo.createdBy = "Ryan Stolys";
        myTInfo.createdBy_id = "NO_IDs_HAVE_BEEN_SET";

        //Set status value
        mapping statusMap = new statusMapping();
        myTInfo.status = statusMap.mapIntToString(3);

        myTInfo.assignedTo.put("NO_ID", new taskAssignObj("Ryan Stolys", true, false));
        myTInfo.houseName = "DevHouse";
        myTInfo.house_id = "NO_IDs_HAVE_BEEN_SET";
        myTInfo.costAssociated = 0;
        myTInfo.difficultyScore = 5;
        myTInfo.dueDate = new Date();
        myTInfo.itemList.add("Test1");
        myTInfo.itemList.add("Test2");
        myTInfo.itemList.add("Test3");
        myTInfo.notificationTime = new Date();
        myTInfo.tag.add("Garbage");
        myTInfo.tag.add("Kitchen");
        myTInfo.tag.add("Cleaning");

        firebaseTask.approveTaskAssignment(myTInfo, "NO_ID", false);
        return;
    }

    public void sortTasks(int sortType) {
        return;
    }


    public void onTaskInfoArrayReturn(taskInfo[] tInfos, String functionName) {
        Log.d("TaskInfoArrayReturn:", "Return from " + functionName);
        if(tInfos != null)
            Log.d("TaskInfoArrayReturn:", "Returned " + tInfos.length + " tInfos ");

        switch(functionName) {
            case "getCurrentTasks(user)":
                //Call function to deal with this result
                break;

            case "getCurrentTasks(house)":
                //Call function to deal with this result
                break;

            default:
                break;
        }

        return;
    }

    public void onTaskInfoReturn(taskInfo tInfo, String functionName) {

        Log.d("TaskInfoReturn:", "Return from " + functionName);
        if(tInfo != null)
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
