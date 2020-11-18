package com.cmpt275.house.classDef;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmpt275.house.classDef.databaseObjects.nameObj;
import com.cmpt275.house.classDef.databaseObjects.taskAssignObj;
import com.cmpt275.house.classDef.documentClass.firebaseTaskDocument;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.mappingClass.statusMapping;
import com.cmpt275.house.classDef.mappingClass.tagMapping;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoCallback;
import com.cmpt275.house.interfaceDef.TaskBE;
import com.cmpt275.house.interfaceDef.mapping;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class taskFirebaseClass implements TaskBE {

    //
    // Class Variables
    //
    private FirebaseFirestore db;
    private final String TAG = "FirebaseTaskAction";

    // TODO: add variables below to documentation
    mapping statusMap;
    mapping tagMap;


    //
    // Class Functions
    //

    ////////////////////////////////////////////////////////////
    //
    // Constructor
    //
    ////////////////////////////////////////////////////////////
    public taskFirebaseClass() {
        db = FirebaseFirestore.getInstance();
        statusMap = new statusMapping();
        tagMap = new tagMapping();
    }


    ////////////////////////////////////////////////////////////
    //
    // Will get the current tasks for the user and return them in an array of tasks
    //
    ////////////////////////////////////////////////////////////
    public void getCurrentTasks(userInfo uInfo, tInfoArrayCallback callback) {

        Log.d(TAG, "getCurrentTasks(user) where tasks are assignedTo " + uInfo.id);

        //Ensure the uInfo has a valid id
        if(uInfo.id == null) {
            //We cannot access db without valid id for user. Return failure.
            callback.onReturn(null, false);
        }
        else {
            //Get documents from the collection that have user_id specified
            db.collection("tasks").whereEqualTo("assignedTo." + uInfo.id + ".exists", true).get()
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
                            callback.onReturn(taskInfoArray, true);

                        }
                        else {
                            Log.d(TAG, "getCurrentTasks(user): Error getting tasks for user: ", queryResult.getException());

                            //Call function to return task value
                            callback.onReturn(null, false);
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
    public void getCurrentTasks(houseInfo hInfo, tInfoArrayCallback callback) {

        Log.d(TAG, "getCurrentTasks(house) where tasks are apart of house " + hInfo.id);

        //Ensure the uInfo has a valid id
        if(hInfo.id == null) {
            //We cannot access db without valid id for user. Return failure.
            callback.onReturn(null, false);
        }
        else {
            //Get documents from the collection that have house_id specified
            db.collection("tasks").whereEqualTo("house_id", hInfo.id).get()
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
                                callback.onReturn(taskInfoArray, false);

                            }
                            else {
                                Log.d(TAG, "getCurrentTasks(house): Error getting tasks for user: ", queryResult.getException());

                                //Call function to return task value
                                callback.onReturn(null, false);
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
    public void getCurrentTasks(userInfo uInfo, String house_id, tInfoArrayCallback callback) {

        Log.d(TAG, "getCurrentTasks(user, house_id) for user: " + uInfo.id + " and house_id " + house_id);

        //Ensure the uInfo has a valid id
        if(uInfo.id == null || house_id == null) {
            //We cannot access db without valid id for user. Return failure.
            callback.onReturn(null, false);
        }
        else {
            //Get documents from the collection that have user_id specified -- need to check house_id after document collected
            db.collection("tasks").whereEqualTo("assignedTo." + uInfo.id + ".exists", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> queryResult) {

                        if(queryResult.isSuccessful()) {
                            Log.d(TAG, "Successfully Query in getCurrentTasks(user, house_id)");

                            List<taskInfo> taskInfoList = new ArrayList<taskInfo>();
                            for(QueryDocumentSnapshot document : queryResult.getResult()) {

                                //Convert queried document to taskData class
                                firebaseTaskDocument taskData = document.toObject(firebaseTaskDocument.class);

                                if(taskData.getHouse_id().equals(house_id)) {
                                    taskInfoList.add(taskData.toTaskInfo(document.getId()));
                                }
                                else {
                                    Log.d(TAG, "Task Document: " + document.getId() + " not from house " + house_id + ". house_id=" + taskData.getHouse_id());
                                }
                            }

                            //Convert list to array and return
                            taskInfo[] taskInfoArray = new taskInfo[taskInfoList.size()];
                            taskInfoList.toArray(taskInfoArray);
                            callback.onReturn(taskInfoArray, false);

                        }
                        else {
                            Log.d(TAG, "getCurrentTasks(user, house_id): Error getting tasks for user: ", queryResult.getException());

                            //Call function to return task value
                            callback.onReturn(null, false);
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
    public void approveTaskAssignment(taskInfo tInfo, String user_id, boolean taskApproved, tInfoCallback callback) {

        if(taskApproved) {
            //Set the task to approved
            tInfo.assignedTo.put(user_id, new taskAssignObj(tInfo.assignedTo.get(user_id).name, true, true));

            //check if all the other assignees have approved the task
            boolean allAssigneesApprove = true;
            for(String userID : tInfo.assignedTo.keySet()) {
                if(!tInfo.assignedTo.get(userID).approved)
                    allAssigneesApprove = false;
            }

            //If every assignee approves of the task assignment change the status
            if(allAssigneesApprove) {
                tInfo.status = statusMap.mapIntToString(1);       //Set task to not completed from awaiting approval
            }

            //ensure the task_id is true
            if(tInfo.id == null) {
                Log.d(TAG, "Invalid task_id. task_id was null");

                callback.onReturn(null, false);
            }
            else {
                //
                //Update the task in the database
                //

                //Create a custom class updatedTask to modify the document with
                firebaseTaskDocument updatedTask = new firebaseTaskDocument(tInfo);

                //Update the task document
                db.collection("tasks").document(tInfo.id).update("assignedTo", updatedTask.getAssignedTo(), "status", updatedTask.getStatus())
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Task successfully approved!");

                        //tInfo updated successfully so we can simply return it
                        callback.onReturn(tInfo, false);
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error updating document", e);

                        //IndicateError
                        callback.onReturn(null, false);
                    });
            }
        }
        else {
            boolean newAssignee = false;

            //Remove the user that declined the task from the assigned to map
            tInfo.assignedTo.remove(user_id);

            //Check if there is anyone still assigned to the task
            if(tInfo.assignedTo.size() < 1) {
                tInfo.assignedTo.put(tInfo.createdBy_id, new taskAssignObj(tInfo.createdBy, true, true));      //Add the created by user to the task
                newAssignee = true;
            }

            if(tInfo.id == null) {
                Log.d(TAG, "Invalid task_id. task_id was null");

                callback.onReturn(null, false);
            }
            else {
                //
                //Update the task in the database
                //

                //Create a custom class updatedTask to modify the document with
                firebaseTaskDocument updatedTask = new firebaseTaskDocument(tInfo);

                boolean finalNewAssignee = newAssignee;

                //Update the task document
                db.collection("tasks").document(tInfo.id).update("assignedTo", updatedTask.getAssignedTo())
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Task Document successfully updated!");

                        if(finalNewAssignee) {
                            //Add a new task to the user who created the task -- will only be one user in this loop
                            for(String taskAssignee_id : updatedTask.getAssignedTo().keySet()) {
                                db.collection("users").document(taskAssignee_id).update("tasks." + tInfo.id, new nameObj(updatedTask.getDisplayName(), true));
                            }
                        }

                        //tInfo updated successfully so we can simply return it
                        callback.onReturn(tInfo, false);
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error updating task", e);

                        //IndicateError
                        callback.onReturn(null, false);
                    });
            }
        }

        return;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will get the task data from the document and convert to taskInfo class
    //
    ////////////////////////////////////////////////////////////
    public void getTaskInfo(String task_id, tInfoCallback callback) {

        //Get document from the collection
        db.collection("tasks").document(task_id).get()
            .addOnSuccessListener(documentReference -> {
                Log.d(TAG, "get() task document successful for task: " + task_id);

                //Convert Task to taskInfo class
                firebaseTaskDocument taskData = documentReference.toObject(firebaseTaskDocument.class);
                taskInfo tInfo = taskData.toTaskInfo(task_id);

                //Call function to return task value
                callback.onReturn(tInfo, false);
            })
            .addOnFailureListener(e -> {
                Log.w(TAG, "Error getting task info document", e);

                //Return null to indicate error in task
                callback.onReturn(null, false);
            });

        return;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will update all fields of the document
    //
    ////////////////////////////////////////////////////////////
    public void setTaskInfo(taskInfo tInfo, tInfoCallback callback) {

        //Create a custom class updatedTask to modify the document with
        firebaseTaskDocument updatedTask = new firebaseTaskDocument(tInfo);

        if(tInfo.id == null) {
            //Indicate an error
            callback.onReturn(null, false);
        }
        else {
            //Update the task document
            db.collection("tasks").document(tInfo.id).set(updatedTask, SetOptions.merge())
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Task Document successfully updated!");

                    //tInfo updated successfully so we can simply return it
                    callback.onReturn(tInfo, false);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating document", e);

                    //IndicateError
                    callback.onReturn(null, false);
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
    public void setTaskInfo(taskInfo tInfo, String parameter, tInfoCallback callback) {

        //Get the field to be updated
        Map<String, Object> updateField = getUpdatedTaskField(tInfo, parameter);

        if(updateField == null) {
            //Return error
            callback.onReturn(null, false);
        }
        else {
            //Update the task document
            db.collection("tasks").document(tInfo.id).update(updateField)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Task Document successfully updated!");

                    //tInfo updated successfully so we can simply return it
                    callback.onReturn(tInfo, false);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating document", e);

                    //IndicateError
                    callback.onReturn(null, false);
                });
        }

        return;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will create the task in the database
    //
    ////////////////////////////////////////////////////////////
    public void createTask(taskInfo tInfo, tInfoCallback callback) {

        boolean taskAssignedAway = false;

        //Make sure the approved state matches what it must be for creating a task
        for (String assignee_id : tInfo.assignedTo.keySet()) {

            if(!tInfo.createdBy_id.equals(assignee_id)) {
                tInfo.assignedTo.put(assignee_id, new taskAssignObj(Objects.requireNonNull(tInfo.assignedTo.get(assignee_id)).name, true, false));
                taskAssignedAway = true;
            }
            else {
                tInfo.assignedTo.put(assignee_id, new taskAssignObj(Objects.requireNonNull(tInfo.assignedTo.get(assignee_id)).name, true, true));
            }
        }

        //Ensure the matches the current state
        if(taskAssignedAway) {
            tInfo.status = statusMap.mapIntToString(5);     //Task is being reassigned
        }
        else {
            tInfo.status = statusMap.mapIntToString(1);     //Task is not completed
        }

        //Make sure the house and created by ids exists so that this task can be found
        if(tInfo.house_id == null || tInfo.createdBy_id == null) {
            Log.d(TAG, "Invalid taskInfo class parameters");

            //Return null to indicate error in task creation
            callback.onReturn(null, false);
        }
        else {
            //Create custom class to generate a new document
            firebaseTaskDocument newTask = new firebaseTaskDocument(tInfo);

            //Add the new task to the tasks collection
            db.collection("tasks").add(newTask)
                .addOnSuccessListener( documentReference -> {
                    Log.d(TAG, "Task added with ID: " + documentReference.getId());

                    //Set the id of the task
                    tInfo.id = documentReference.getId();

                    //Add task to house
                    db.collection("houses").document(tInfo.house_id)
                        .update("tasks." + tInfo.id, new nameObj(tInfo.displayName, true));

                    //Add task to users who the task is assigned to
                    for(String taskAssignee_id : newTask.getAssignedTo().keySet()) {
                        db.collection("users").document(taskAssignee_id)
                            .update("tasks." + tInfo.id, new nameObj(newTask.getDisplayName(), true));
                    }

                    //Return the task info
                    callback.onReturn(tInfo, false);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);

                    //Return null to indicate error in task
                    callback.onReturn(null, false);
                });
        }

        return;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will delete the task and all references to the task in the system
    //
    ////////////////////////////////////////////////////////////
    public void deleteTask(taskInfo tInfo, booleanCallback callback) {

        if(tInfo.id == null) {
            Log.d(TAG, "Invalid task_id. task_id was null");

            callback.onReturn(false);
        }
        else {
            Map<String, Object> removeTask = new HashMap<>();
            removeTask.put("tasks." + tInfo.id, FieldValue.delete());

            //Remove the task from the task list in the task house
            db.collection("houses").document(tInfo.house_id).update(removeTask);

            //Remove the task from the task list in the task's assignees
            for(String taskAssignee_id : tInfo.assignedTo.keySet()) {
                db.collection("users").document(taskAssignee_id).update(removeTask);
            }

            //If the task has an ongoing vote
            if(statusMap.mapStringToInt(tInfo.status) == 4 || statusMap.mapStringToInt(tInfo.status) == 5) {
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
                    callback.onReturn(true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error removing task: " + tInfo.id, e);

                    //Indicate Error
                    callback.onReturn(false);
                });
        }


        return;
    }


    ////////////////////////////////////////////////////////////
    //
    // TODO:
    // Will generate a swap task request
    //
    ////////////////////////////////////////////////////////////
    public void swapTask(taskInfo tInfoA, taskInfo tInfoB, booleanCallback callback) {

        //Change the status and who the task is assigned to

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
            case "createdBy":
                newField.put("createdBy", tInfo.createdBy);
                break;
            case "description":
                newField.put("description", tInfo.description);
                break;
            case "difficultyScore":
                newField.put("difficultyScore", tInfo.difficultyScore);
                break;
            case "displayName":
                newField.put("displayName", tInfo.displayName);
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
            case "notificationTime":
                newField.put("notificationTime", tInfo.notificationTime);
                break;
            case "status":
                newField.put("status", statusMap.mapStringToInt(tInfo.status));
                break;
            case "tag":
                newField.put("tag", tagMap.mapList_StringToInt(tInfo.tag));
                break;

            default:
                newField = null;
                break;
        }

        return newField;
    }
}
