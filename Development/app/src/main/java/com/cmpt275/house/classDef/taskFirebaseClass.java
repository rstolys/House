package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.interfaceDef.TaskBE;

import com.cmpt275.house.interfaceDef.taskCallbacks;
import com.google.firebase.firestore.FirebaseFirestore;

public class taskFirebaseClass implements TaskBE {

    //
    // Class Variables
    //
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private taskCallbacks tCallback;
    private final String TAG = "FirebaseAction";

    //
    // Class Functions
    //
    ////////////////////////////////////////////////////////////
    //
    // Constructor which implements the callback functions
    //
    ////////////////////////////////////////////////////////////
    public taskFirebaseClass(taskCallbacks tCallback) {
        this.tCallback = tCallback;
    }

    public void getCurrentTasks(userInfo uInfo) {return;}
    public void getCurrentTasks(houseInfo hInfo) {return;}
    public void getCurrentTasks(userInfo uInfo, int house_id) {return;}

    ////////////////////////////////////////////////////////////
    //
    // Prototype Priority
    //
    ////////////////////////////////////////////////////////////
    public void getTaskInfo(int task_id) {return;}


    ////////////////////////////////////////////////////////////
    //
    // Prototype Priority
    //
    ////////////////////////////////////////////////////////////
    public void setTaskInfo(taskInfo tInfo) {return;}


    ////////////////////////////////////////////////////////////
    //
    // Prototype Priority
    //
    ////////////////////////////////////////////////////////////
    public void setTaskInfo(taskInfo tInfo, int parameter) {return;}

    ////////////////////////////////////////////////////////////
    //
    // Prototype Priority
    //
    ////////////////////////////////////////////////////////////
    public void createTask(taskInfo tInfo) {

        Log.d("TEST","in createTask function");

        //Create custom class to generate a new document fields
        firebaseTaskDocument newTask = new firebaseTaskDocument(tInfo);

        //Copy tInfo to be able to return
        taskInfo finalTInfo = tInfo;

        //Add the new task to the tasks collection
        db.collection("tasks").add(newTask)
            .addOnSuccessListener( documentReference -> {
                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                //Update the task with its id value and return
                finalTInfo.id = documentReference.getId();
                tCallback.onTaskInfoReturn(finalTInfo, "createTask");
            })
            .addOnFailureListener(e -> {
                Log.w(TAG, "Error adding document", e);

                //Return null to indicate error in task
                tCallback.onTaskInfoReturn(null, "createTask");
            });

        return;
    }

    ////////////////////////////////////////////////////////////
    //
    // Prototype Priority
    //
    ////////////////////////////////////////////////////////////
    public void deleteTask(taskInfo tInfo) {return;}


    public void swapTask(taskInfo tInfoA, taskInfo tInfoB) {return;}
}
