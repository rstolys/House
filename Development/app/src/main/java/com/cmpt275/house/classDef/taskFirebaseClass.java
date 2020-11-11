package com.cmpt275.house.classDef;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmpt275.house.interfaceDef.TaskBE;
import com.cmpt275.house.interfaceDef.mapping;
import com.cmpt275.house.interfaceDef.taskCallbacks;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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


    ////////////////////////////////////////////////////////////
    //
    // Will get the current tasks for the user and return them in an array of tasks
    //
    ////////////////////////////////////////////////////////////
    public void getCurrentTasks(userInfo uInfo) {

        Log.d(TAG, "getCurrentTasks(user) where tasks are assignedTo " + uInfo.id);

        //Ensure the uInfo has a valid id
        if(uInfo.id == null) {
            //We cannot access db without valid id for user. Return failure.
            tCallback.onTaskInfoArrayReturn(null, "getCurrentTasks(user)");
        }
        else {
            //Get documents from the collection that have user_id specified
            db.collection("tasks").whereNotEqualTo("assignedTo." + uInfo.id, null).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> queryResult) {

                        if(queryResult.isSuccessful()) {
                            Log.d(TAG, "Successfully Query in getCurrentTasks(user)");

                            List<taskInfo> taskInfoList = new ArrayList<taskInfo>();
                            for(QueryDocumentSnapshot document : queryResult.getResult()) {

                                firebaseTaskDocument taskData = document.toObject(firebaseTaskDocument.class);
                                taskInfoList.add(taskData.toTaskInfo(document.getId()));

                                Log.d(TAG, "Collected Task Document: " + document.getId());
                            }

                            //Convert list to array and return
                            taskInfo[] taskInfoArray = new taskInfo[taskInfoList.size()];
                            taskInfoList.toArray(taskInfoArray);
                            tCallback.onTaskInfoArrayReturn(taskInfoArray, "getCurrentTasks(user)");

                        }
                        else {
                            Log.d(TAG, "getCurrentTasks(user): Error getting tasks for user: ", queryResult.getException());

                            //Call function to return task value
                            tCallback.onTaskInfoArrayReturn(null, "getCurrentTasks(user)");
                        }
                    }
                });
        }


        return;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will get the current tasks for the house specified
    //
    ////////////////////////////////////////////////////////////
    public void getCurrentTasks(houseInfo hInfo) {

        Log.d(TAG, "getCurrentTasks(house) where tasks are apart of house " + hInfo.id);

        //Ensure the uInfo has a valid id
        if(hInfo.id == null) {
            //We cannot access db without valid id for user. Return failure.
            tCallback.onTaskInfoArrayReturn(null, "getCurrentTasks(house)");
        }
        else {
            //Get documents from the collection that have house_id specified
            db.collection("tasks").whereNotEqualTo("house." + hInfo.id, null).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> queryResult) {

                            if(queryResult.isSuccessful()) {
                                Log.d(TAG, "Successfully Query in getCurrentTasks(house)");

                                List<taskInfo> taskInfoList = new ArrayList<taskInfo>();
                                for(QueryDocumentSnapshot document : queryResult.getResult()) {

                                    firebaseTaskDocument taskData = document.toObject(firebaseTaskDocument.class);
                                    taskInfoList.add(taskData.toTaskInfo(document.getId()));

                                    Log.d(TAG, "Collected Task Document: " + document.getId());
                                }

                                //Convert list to array and return
                                taskInfo[] taskInfoArray = new taskInfo[taskInfoList.size()];
                                taskInfoList.toArray(taskInfoArray);
                                tCallback.onTaskInfoArrayReturn(taskInfoArray, "getCurrentTasks(house)");

                            }
                            else {
                                Log.d(TAG, "getCurrentTasks(house): Error getting tasks for user: ", queryResult.getException());

                                //Call function to return task value
                                tCallback.onTaskInfoArrayReturn(null, "getCurrentTasks(house)");
                            }
                        }
                    });
        }

        return;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will get the current tasks for the user and house specified
    //
    ////////////////////////////////////////////////////////////
    public void getCurrentTasks(userInfo uInfo, String house_id) {

        Log.d(TAG, "getCurrentTasks(user, house_id) for user: " + uInfo.id + " and house_id " + house_id);

        //Ensure the uInfo has a valid id
        if(uInfo.id == null || house_id == null) {
            //We cannot access db without valid id for user. Return failure.
            tCallback.onTaskInfoArrayReturn(null, "getCurrentTasks(user, house_id)");
        }
        else {
            //Get documents from the collection that have user_id specified -- need to check house_id after document collected
            db.collection("tasks").whereNotEqualTo("assignedTo." + uInfo.id, null).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> queryResult) {

                            if(queryResult.isSuccessful()) {
                                Log.d(TAG, "Successfully Query in getCurrentTasks(user, house_id)");

                                List<taskInfo> taskInfoList = new ArrayList<taskInfo>();
                                for(QueryDocumentSnapshot document : queryResult.getResult()) {

                                    //Convert queried document to taskData class
                                    firebaseTaskDocument taskData = document.toObject(firebaseTaskDocument.class);

                                    if(taskData.getHouse().containsKey(house_id)) {
                                        taskInfoList.add(taskData.toTaskInfo(document.getId()));
                                        Log.d(TAG, "Added Task Document: " + document.getId());
                                    }
                                    else {
                                        Log.d(TAG, "Task Document: " + document.getId() + " not from house " + house_id);
                                    }
                                }

                                //Convert list to array and return
                                taskInfo[] taskInfoArray = new taskInfo[taskInfoList.size()];
                                taskInfoList.toArray(taskInfoArray);
                                tCallback.onTaskInfoArrayReturn(taskInfoArray, "getCurrentTasks(user, house_id)");

                            }
                            else {
                                Log.d(TAG, "getCurrentTasks(user, house_id): Error getting tasks for user: ", queryResult.getException());

                                //Call function to return task value
                                tCallback.onTaskInfoArrayReturn(null, "getCurrentTasks(user, house_id)");
                            }
                        }
                    });
        }

        return;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will determine the approval status and update the task to reflect that
    //
    ////////////////////////////////////////////////////////////
    public void approveTaskAssignment(taskInfo tInfo, String user_id, boolean taskApproved) {

        if(taskApproved) {
            tInfo.status = 1;       //Set task to not completed

            //Update the task document
            db.collection("tasks").document(tInfo.id).update("status", tInfo.status)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Task Document successfully updated!");

                    //tInfo updated successfully so we can simply return it
                    tCallback.onTaskInfoReturn(tInfo, "approveTaskAssignment");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating document", e);

                    //IndicateError
                    tCallback.onTaskInfoReturn(null, "approveTaskAssignment");
                });
        }
        else {
            boolean newAssignee = false;

            //Remove the user that declined the task from the assigned to map
            tInfo.assignedTo.remove(user_id);

            //Check if there is anyone still assigned to the task
            if(tInfo.assignedTo.size() < 1) {
                tInfo.assignedTo.put(tInfo.createdBy_id, tInfo.createdBy);      //Add the created by user to the task
                newAssignee = true;
            }

            //
            //Update the task in the database
            //

            //Create a custom class updatedTask to modify the document with
            firebaseTaskDocument updatedTask = new firebaseTaskDocument(tInfo);

            boolean finalNewAssignee = newAssignee;

            //Update the task document
            db.collection("tasks").document(tInfo.id).set(updatedTask, SetOptions.merge())
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Task Document successfully updated!");

                    if(finalNewAssignee) {
                        //Add a new task to the user who created the task
                        for(String taskAssignee_id : updatedTask.getAssignedTo().keySet()) {
                            db.collection("users").document(taskAssignee_id).update("tasks." + tInfo.id, updatedTask.getDisplayName());
                        }
                    }

                    //tInfo updated successfully so we can simply return it
                    tCallback.onTaskInfoReturn(tInfo, "approveTaskAssignment");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating task", e);

                    //IndicateError
                    tCallback.onTaskInfoReturn(null, "approveTaskAssignment");
                });

        }

        return;
    }


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
    // Will create the task in the database
    //
    ////////////////////////////////////////////////////////////
    public void createTask(taskInfo tInfo) {

        boolean taskAssignedAway = false;

        //Check if the task was assigned to someone other than the creator
        for(String assigneeUser_id : tInfo.assignedTo.keySet()) {
            if(tInfo.createdBy_id != assigneeUser_id)
                taskAssignedAway = true;
        }

        if(taskAssignedAway)
            tInfo.status = 5;       //Reassignment in approval
        else
            tInfo.status = 1;       //Task Not completed


        //Create custom class to generate a new document fields
        firebaseTaskDocument newTask = new firebaseTaskDocument(tInfo);

        //Copy tInfo to be able to return
        taskInfo finalTInfo = tInfo;

        //Add the new task to the tasks collection
        db.collection("tasks").add(newTask)
            .addOnSuccessListener( documentReference -> {
                Log.d(TAG, "Task added with ID: " + documentReference.getId());

                //Set the id of the task
                finalTInfo.id = documentReference.getId();

                //Add task to house
                db.collection("houses").document(finalTInfo.house_id).update("tasks." + finalTInfo.id, newTask.getDisplayName());

                //Add task to users who the task is assigned to
                for(String taskAssignee_id : newTask.getAssignedTo().keySet()) {
                    db.collection("users").document(taskAssignee_id).update("tasks." + finalTInfo.id, newTask.getDisplayName());
                }

                //Return the task info
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

        //Remove the task from the task list in the task's assignees
        for (String taskAssignee_id : tInfo.assignedTo.keySet()) {
            db.collection("users").document(taskAssignee_id).update(removeTask);
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

                //Return Successful call
                tCallback.onTaskBooleanReturn(true, "deleteTask");
            })
            .addOnFailureListener(e -> {
                Log.w(TAG, "Error removing task: " + tInfo.id, e);

                //Indicate Error
                tCallback.onTaskBooleanReturn(false, "deleteTask");
            });

        return;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will generate a swap task request
    //
    ////////////////////////////////////////////////////////////
    public void swapTask(taskInfo tInfoA, taskInfo tInfoB) {

        //This function should change the status of a task

        return;
    }



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
            case "displayName":
            case "name":
                newField.put("displayName", tInfo.name);
                break;
            case "notificationTime":
                newField.put("notificationTime", tInfo.notificationTime);
                break;
            case "status":
                newField.put("status", tInfo.status);
                break;
            case "tag":
                mapping tagMap = new tagMapping();
                newField.put("tag", tagMap.mapList_StringToInt(tInfo.tag));
                break;

            default:
                newField = null;
                break;
        }

        return newField;
    }
}
