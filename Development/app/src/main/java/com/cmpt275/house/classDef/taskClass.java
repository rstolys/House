package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.classDef.databaseObjects.taskAssignObj;

import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.mappingClass.statusMapping;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoCallback;
import com.cmpt275.house.interfaceDef.mapping;
import com.cmpt275.house.interfaceDef.task;

import java.util.Date;

public class taskClass implements task {

    //
    // Class Variables
    //
    private taskInfo[] tInfos;

    private final taskFirebaseClass firebaseTask;

    //
    // Class Functions
    //
    public taskClass() {
        firebaseTask = new taskFirebaseClass();
    }


    public void viewTask(String task_id) {

        userInfo myUInfo = new userInfo();
        houseInfo myHInfo = new houseInfo();

        myUInfo.id = "NO_ID";
        myHInfo.id = "NO_IDs_HAVE_BEEN_SET";

        firebaseTask.getCurrentTasks(myUInfo, (tInfos, success) -> {
            Log.d("getCurrentTasks:", "Returned with success " + success);
            //Do stuff here...

        });
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

        firebaseTask.createTask(myTInfo, (tInfo1, success) -> {
            Log.d("createTask:", "Returned with success " + success);
            //Do stuff here ...
        });
    }

    public void assignTask(taskInfo tInfo) {}

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

        firebaseTask.setTaskInfo(myTInfo, paramToChange, (tInfo1, success) -> {
            Log.d("setTaskInfo:", "Returned with success " + success);
            //Do stuff here ...
        });
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

        firebaseTask.deleteTask(myTInfo, result -> {
            Log.d("deleteTask:", "Returned with result " + result);
            //Do stuff here...
        });
    }

    public void displayTask(String task_id) {

        //Get the task to display
        firebaseTask.getTaskInfo(task_id, (tInfo, success) -> {
            Log.d("getTaskInfo:", "Returned with success " + success);
            //Do Stuff here...
        });
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

        firebaseTask.approveTaskAssignment(myTInfo, "NO_ID", false, (tInfo, success) -> {
            Log.d("approveTaskAssignment:", "Returned with success " + success);
            //Do stuff here ...
        });
    }

    public void sortTasks(int sortType) {}


}
