package com.cmpt275.house.classDef;

import android.content.Context;
import android.util.Log;

import com.cmpt275.house.classDef.databaseObjects.taskAssignObj;

import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.mappingClass.statusMapping;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoCallback;
import com.cmpt275.house.interfaceDef.Callbacks.updateCallback;
import com.cmpt275.house.interfaceDef.mapping;
import com.cmpt275.house.interfaceDef.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Observable;

public class taskClass extends Observable implements task {

    //
    // Class Variables
    //
    public taskInfo[] tInfos;
    public userInfo uInfo;

    private final Context mContext;
    private final taskFirebaseClass firebaseTask;

    private final displayMessage display = new displayMessage();
    private final statusMapping statusMap = new statusMapping();


    //
    // Class Functions
    //

    /////////////////////////////////////////////////
    //
    // Class constructor
    //
    /////////////////////////////////////////////////
    public taskClass(Context mContext) {
        firebaseTask = new taskFirebaseClass();
        this.mContext = mContext;
    }


    /////////////////////////////////////////////////
    //
    // Will get the users task to display to the screen
    //
    /////////////////////////////////////////////////
    public void getTasks(String user_id, updateCallback callback) {

        if(user_id == null) {
            display.showToastMessage(mContext, "Something went wrong loading your data. Try reloading the page", display.LONG);

            callback.onReturn(false);
        }
        else {
            firebaseTask.getCurrentTasks(uInfo, (tInfos, success, errorMessage) -> {
                Log.d("getCurrentTasks:", "Returned with success: " + success);

                if(success) {
                    this.tInfos = tInfos;
                    callback.onReturn(true);
                }
                else {
                    display.showToastMessage(mContext, "There was an error loading your data. Please try again", display.LONG);
                    callback.onReturn(true);
                }
            });
        }
    }


    public void viewTask(String task_id) {

        userInfo myUInfo = new userInfo();
        houseInfo myHInfo = new houseInfo();

        myUInfo.id = "NO_ID";
        myHInfo.id = "NO_IDs_HAVE_BEEN_SET";

        firebaseTask.getCurrentTasks(myUInfo, (tInfos, success, errorMessage) -> {
            Log.d("getCurrentTasks:", "Returned with success " + success);
            //Do stuff here...

            if( success ) {

            }

            //

        });
    }

    public void disputeTask(taskInfo tInfo) {

        String task_id = "6PBXbr63dg8XaMK9w3gV";

        firebaseTask.getTaskInfo(task_id, (tInfoRet, success, errorMessage) -> {
            Log.d("getTaskInfo:", "Returned with success " + success);

            if(success) {
                firebaseTask.disputeTask(tInfoRet, (tInfoRet2, success2, errorMessage2) -> {
                    Log.d("disputeTask:", "Returned with success " + success);
                    //Do stuff here...
                });
            }
        });
    }

    public void requestExtension(taskInfo tInfo,
                                 updateCallback callback) {

        if(tInfo.id != null) {
            firebaseTask.requestExtension(tInfo, (tInfoRet, success, errorMessage) -> {
                Log.d("requestExtension:", "Returned with success " + success);

                if(success) {
                    display.showToastMessage(mContext, "Extension requested successfully", display.LONG);

                    callback.onReturn(true);
                }
                else {
                    display.showToastMessage(mContext, errorMessage, display.LONG);
                    callback.onReturn(false);
                }
            });
        }
        else {
            display.showToastMessage(mContext, "Looks like somethinig went wrong, try again", display.LONG);
            callback.onReturn(false);
        }


    }

    public void createTask(taskInfo tInfo, updateCallback callback) {

        if(tInfo == null) {
            display.showToastMessage(mContext, "Something went wrong, try reloading the page", display.LONG);
        }
        else {
            firebaseTask.createTask(tInfo, (tInfoRet, success, errorMessage) -> {
                Log.d("createTask:", "Returned with success " + success);

                if(success) {
                    display.showToastMessage(mContext, "Task successfully created!", display.LONG);

                    //Should update tInfo -- will change activities and it will be reloaded anyways
                    callback.onReturn(true);
                }
                else {
                    display.showToastMessage(mContext, errorMessage, display.LONG);

                    callback.onReturn(false);
                }
            });
        }

    }

    public void assignTask(taskInfo tInfo) {}


    ///////////////////////////////////////////////////////////////
    //
    // Will complete the task for the user
    //
    ///////////////////////////////////////////////////////////////
    public void completeTask(taskInfo tInfo, int taskIndex, updateCallback callback) {

        if(tInfo == null) {
            display.showToastMessage(mContext, "Looks like something went wrong there. Try reloading the page", display.LONG);
        }
        else {
            //Set status value
            tInfo.status = statusMap.COMPLETED;

            //Set Parameter to change
            String paramToChange = "status";

            firebaseTask.setTaskInfo(tInfo, paramToChange, (tInfoRet, success, errorMessage) -> {
                Log.d("setTaskInfo:", "Returned with success " + success);

                if(success) {
                    if(taskIndex != -1)
                        this.tInfos[taskIndex] = tInfoRet;

                    display.showToastMessage(mContext, "Task Completed! Well Done!", display.LONG);

                    callback.onReturn(true);
                }
                else {
                    display.showToastMessage(mContext, errorMessage, display.LONG);

                    callback.onReturn(false);
                }
            });
        }
    }


    ///////////////////////////////////////////////////////////////
    //
    // Will edit the task contents
    //
    ///////////////////////////////////////////////////////////////
    public void editTask(taskInfo tInfo, boolean reassigned, String oldAssignee_id, tInfoCallback callback) {

        if(tInfo == null) {
            display.showToastMessage(mContext, "Looks like something went wrong. Try reloading the page", display.LONG);

            callback.onReturn(null, false, "");
        }
        else {
            firebaseTask.setTaskInfo(tInfo, reassigned, oldAssignee_id, (tInfoRet, success, errorMessage) -> {
                Log.d("setTaskInfo:", "Returned with success " + success);

                if(success) {
                    display.showToastMessage(mContext, "Task Successfully Updated!", display.LONG);

                    callback.onReturn(tInfoRet, true, "");
                }
                else {
                    display.showToastMessage(mContext, errorMessage, display.LONG);

                    callback.onReturn(null, false, "");
                }
            });
        }
    }

    /////////////////////////////////////////////////
    //
    // Will delete the task from the user's DB
    //
    /////////////////////////////////////////////////
    public void deleteTask(taskInfo tInfo, updateCallback callback) {

        if(tInfo == null) {
            display.showToastMessage(mContext, "Looks like somethig went wrong. Try reloading the page", display.LONG);
        }
        else {
            firebaseTask.deleteTask(tInfo, (result, errorMessage) -> {
                Log.d("deleteTask:", "Returned with result " + result);

                if(result) {
                    display.showToastMessage(mContext, "Task Successfully Deleted", display.LONG);

                    callback.onReturn(true);
                }
                else {
                    display.showToastMessage(mContext, errorMessage, display.LONG);

                    callback.onReturn(false);
                }
            });
        }


    }

    public void displayTask(String task_id) {

        //Get the task to display
        firebaseTask.getTaskInfo(task_id, (tInfo, success, errorMessage) -> {
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

        firebaseTask.approveTaskAssignment(myTInfo, "NO_ID", false, (tInfo, success, errorMessage) -> {
            Log.d("approveTaskAssignment:", "Returned with success " + success);
            //Do stuff here ...
        });
    }


    public void getTaskVotes(String task_id) {

        task_id = "6PBXbr63dg8XaMK9w3gV_fail";

        //Get the task to display
        firebaseTask.getTaskVotes(task_id, (vInfos, success, errorMessage) -> {
            Log.d("getTaskVotes:", "Returned with success " + success);

            if(success) {
                Log.d("getTaskVotes:", "Returned " + vInfos.length + " voting classes");
                //Do Stuff here...
            }
        });
    }

    public void sortTasks(int sortType) {}

    private int getTaskIndex(String user_id) {
        int index = 0;
        /*
        for( ; index < tInfos.size(); index++) {
            if(tInfos.at(index).id.equals(user_id))
                break;
        }
        */
        return index;
    }


}
