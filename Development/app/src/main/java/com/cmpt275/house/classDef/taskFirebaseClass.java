package com.cmpt275.house.classDef;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmpt275.house.interfaceDef.TaskBE;
import com.cmpt275.house.interfaceDef.taskCallbacks;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class taskFirebaseClass implements TaskBE {

    //
    // Class Variables
    //
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private taskCallbacks tCallback;
    private final String TAG = "FirebaseTaskAction";

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
    public void getCurrentTasks(userInfo uInfo, String house_id) {return;}


    ////////////////////////////////////////////////////////////
    //
    // Will get the task data from the document and convert to taskInfo class
    //
    ////////////////////////////////////////////////////////////
    public void getTaskInfo(String task_id) {

        //Get document from the collection
        db.collection("tasks").document(task_id).get()
            .addOnSuccessListener(documentReference -> {
                Log.d(TAG, "get() task document successful for task: " + task_id);

                //Convert Task to taskInfo class
                firebaseTaskDocument taskData = documentReference.toObject(firebaseTaskDocument.class);
                taskInfo tInfo = taskData.toTaskInfo(task_id);

                //Call function to return task value
                tCallback.onTaskInfoReturn(tInfo, "getTaskInfo");
            })
            .addOnFailureListener(e -> {
                Log.w(TAG, "Error getting document info document", e);

                //Return null to indicate error in task
                tCallback.onTaskInfoReturn(null, "getTaskInfo");
            });

        return;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will update multiple fields of the task document
    //
    ////////////////////////////////////////////////////////////
    public void setTaskInfo(taskInfo tInfo) {

        //Create a custom class updatedTask to modify the document with
        firebaseTaskDocument updatedTask = new firebaseTaskDocument(tInfo);

        if(tInfo.id == null) {
            //Indicate an error
            tCallback.onTaskInfoReturn(null, "setTaskInfo");
        }
        else {
            //Update the task document
            db.collection("tasks").document(tInfo.id).set(updatedTask, SetOptions.merge())
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Task Document successfully updated!");

                    //tInfo updated successfully so we can simply return it
                    tCallback.onTaskInfoReturn(tInfo, "setTaskInfo(1)");     //(1) means 1 input indicating it is this function
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating document", e);

                    //IndicateError
                    tCallback.onTaskInfoReturn(null, "setTaskInfo(1)");
                });
        }

        return;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will update a single parameter of the task document
    // as specified by the input
    //
    ////////////////////////////////////////////////////////////
    public void setTaskInfo(taskInfo tInfo, String parameter) {

        //Get the field to be updated
        Map<String, Object> updateField = getUpdatedTaskField(tInfo, parameter);

        if(updateField == null) {
            //Return error
            tCallback.onTaskInfoReturn(null, "setTaskInfo(2)");
        }
        else {
            //Update the task document
            db.collection("tasks").document(tInfo.id).update(updateField)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Task Document successfully updated!");

                    //tInfo updated successfully so we can simply return it
                    tCallback.onTaskInfoReturn(tInfo, "setTaskInfo(2)");     //(1) means 1 input indicating it is this function
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating document", e);

                    //IndicateError
                    tCallback.onTaskInfoReturn(null, "setTaskInfo(2)");
                });
        }


        return;
    }

    ////////////////////////////////////////////////////////////
    //
    // Will Create the task in the database
    //
    ////////////////////////////////////////////////////////////
    public void createTask(taskInfo tInfo) {

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
    // Will delete the task and all references to the task in the system
    //
    ////////////////////////////////////////////////////////////
    public void deleteTask(taskInfo tInfo) {

        Map<String, Object> removeTask = new HashMap<>();
        removeTask.put("task_ids", FieldValue.arrayRemove(tInfo.id));

        //Remove the task from the task list in the task house
        db.collection("houses").document(tInfo.house_id).update(removeTask);

        //Remove the task from the task list in the task's creator
        db.collection("users").document(tInfo.createdBy_id).update(removeTask);

        //Remove the task from the task list in the task's assignees
        for (Map.Entry<String, String> assignMap : tInfo.assignedTo.entrySet()) {
            db.collection("users").document(assignMap.getKey()).update(removeTask);
        }

        //If the task has an ongoing vote
        if(tInfo.status == 4 || tInfo.status == 5) {
            db.collection("voting").whereEqualTo("task_id", tInfo.id).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Delete each vote associated with the task
                                db.collection("voting").document(document.getId()).delete();
                            }
                        }
                    }
                });
        }


        //Delete the task from the page
        db.collection("tasks").document(tInfo.id).delete()
            .addOnSuccessListener(documentReference -> {
                Log.d(TAG, "Successfully Deleted Task: " + tInfo.id);

                //Return Successful call -- ***Should this be the function we use? Or should it be one that returns the result of the delete()?***
                tCallback.onTaskBooleanReturn(true, "deleteTask");
            })
            .addOnFailureListener(e -> {
                Log.w(TAG, "Error removing task: " + tInfo.id, e);

                //Indicate Error -- ***Should this be the function we use? Or should it be one that returns the result of the delete?***
                tCallback.onTaskBooleanReturn(false, "deleteTask");
            });

        return;
    }


    public void swapTask(taskInfo tInfoA, taskInfo tInfoB) {return;}



    ////////////////////////////////////////////////////////////
    //
    // Will return an object of the field that was updated
    //
    ////////////////////////////////////////////////////////////
    private Map<String, Object> getUpdatedTaskField(taskInfo tInfo, String parameter) {

        //Create Generic field to be updated
        Map<String, Object> newField = new HashMap<>();

        //Would be nice to simply do all of the below code in one line
        //  need to learn how to access method attribute dynamically -- couldn't find a way

        switch(parameter) {
            case "assignedTo":
                newField.put("assignedTo", tInfo.assignedTo);
                break;
            case "costAssociated":
                newField.put("costAssociated", tInfo.costAssociated);
                break;
            case "createdBy_id":
                newField.put("createdBy_id", tInfo.createdBy_id);
                break;
            case "description":
                newField.put("description", tInfo.description);
                break;
            case "difficultyScore":
                newField.put("difficultyScore", tInfo.difficultyScore);
                break;
            case "dueDate":
                newField.put("dueDate", tInfo.dueDate);
                break;
            case "house_id":
                newField.put("house_id", tInfo.house_id);
                break;
            case "houseName":
                newField.put("houseName", tInfo.houseName);
                break;
            case "itemList":
                newField.put("itemList", tInfo.itemList);
                break;
            case "name":
                newField.put("name", tInfo.name);
                break;
            case "notificationTime":
                newField.put("notificationTime", tInfo.notificationTime);
                break;
            case "status":
                newField.put("status", tInfo.status);
                break;
            case "tag":
                newField.put("tag", tInfo.tag);
                break;

            default:
                newField = null;
                break;
        }

        return newField;
    }
}
