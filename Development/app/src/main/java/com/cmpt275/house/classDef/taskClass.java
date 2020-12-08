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
    private final notifications notify = new notifications();

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


    //////////////////////////////////////////////////////////////
    //
    // Will create a vote for a 1 week extension
    //
    //////////////////////////////////////////////////////////////
    public void requestExtension(taskInfo tInfo, updateCallback callback) {

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


    //////////////////////////////////////////////////////////////
    //
    // Will create a task and setup the notification for the user
    //
    //////////////////////////////////////////////////////////////
    public void createTask(taskInfo tInfo, userInfo uInfo, updateCallback callback) {

        if(tInfo == null) {
            display.showToastMessage(mContext, "Something went wrong, try reloading the page", display.LONG);
        }
        else {
            //Setup the notification for the task
            if(tInfo.notificationTime != null && uInfo.notificationsAllowed)
                notify.generateNotification(mContext, "Task Time!", "It is time to comlplete your task: " + tInfo.displayName, tInfo.notificationTime);

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


    ///////////////////////////////////////////////////////////////
    //
    // Will assign the task to a specific user
    //
    ///////////////////////////////////////////////////////////////
    public void assignTask(taskInfo tInfo, updateCallback callback) {

    }


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
    public void editTask(taskInfo tInfo, userInfo uInfo, boolean reassigned, String oldAssignee_id, tInfoCallback callback) {

        if(tInfo == null) {
            display.showToastMessage(mContext, "Looks like something went wrong. Try reloading the page", display.LONG);

            callback.onReturn(null, false, "");
        }
        else {
            //Setup new notification
            if(tInfo.notificationTime != null && uInfo.notificationsAllowed)
                notify.generateNotification(mContext, "Task Time!", "It is time to comlplete your task: " + tInfo.displayName, tInfo.notificationTime);

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


    //////////////////////////////////////////////////////////////
    //
    // Will delete the task from the user's DB
    //
    //////////////////////////////////////////////////////////////
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


    //////////////////////////////////////////////////////////////
    //
    // Will approve the task assigned to the user
    //
    //////////////////////////////////////////////////////////////
    public void approveTask(taskInfo tInfo, userInfo uInfo, boolean approval, int taskIndex, updateCallback callback) {

        if(tInfo == null || tInfo.id == null || uInfo.id == null) {
            display.showToastMessage(mContext, "Something went wrong. Try reloading the page", display.LONG);

            callback.onReturn(false);
        }
        else {
            firebaseTask.approveTaskAssignment(tInfo, uInfo.id, approval, (tInfoRet, success, errorMessage) -> {
                Log.d("approveTaskAssignment:", "Returned with success " + success);

                if(success) {
                    String msg = "Task " + (approval ? "Approved!" : "Declined");
                    display.showToastMessage(mContext, msg, display.LONG);

                    if(approval) {
                        this.tInfos[taskIndex] = tInfoRet;
                    }
                    else {
                        removeTaskFromArray(taskIndex);
                    }

                    callback.onReturn(true);
                }
                else {
                    display.showToastMessage(mContext, errorMessage, display.LONG);

                    //Show update task info but it will be refreshed
                    callback.onReturn(false);
                }
            });
        }
    }


    //////////////////////////////////////////////////////////////
    //
    // Will remove the task from the list to allow proper update
    //
    //////////////////////////////////////////////////////////////
    private void removeTaskFromArray(int index) {
        taskInfo [] tempList = new taskInfo[tInfos.length -1];

        for(int i = 0, j = 0; i < tInfos.length; i++, j++) {
            if(i != index) {
                tempList[j] = tInfos[i];
            }
            else {
                j--;        //don't want to get ahead
            }
        }

        //Set tInfos to new array
        tInfos = tempList;
    }
}
