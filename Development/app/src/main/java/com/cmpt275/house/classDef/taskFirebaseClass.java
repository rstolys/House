package com.cmpt275.house.classDef;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmpt275.house.classDef.databaseObjects.nameObj;
import com.cmpt275.house.classDef.databaseObjects.taskAssignObj;
import com.cmpt275.house.classDef.documentClass.firebaseTaskDocument;
import com.cmpt275.house.classDef.documentClass.firebaseVotingDocument;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.classDef.mappingClass.statusMapping;
import com.cmpt275.house.classDef.mappingClass.tagMapping;
import com.cmpt275.house.classDef.mappingClass.voteTypeMapping;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.tInfoCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoArrayCallback;
import com.cmpt275.house.interfaceDef.TaskBE;
import com.cmpt275.house.interfaceDef.mapping;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class taskFirebaseClass implements TaskBE {

    //
    // Class Variables
    //
    private final FirebaseFirestore db;
    private final String TAG = "FirebaseTaskAction";

    private final String NO_ERROR = "";
    private final String INVALID_PARAMETER_MESSAGE = "Looks like we couldn't access your information. Try signing in again";
    private final String DATABASE_ERROR_MESSAGE = "Oops! Looks like there was an error on our end. Sorry about that. Please try again";
    private final String UNKNOWN_ERROR_MESSAGE = "Oops! Looks something went wrong there. Sorry about that. Please try again";

    statusMapping statusMap;
    tagMapping tagMap;
    voteTypeMapping voteMap;


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
        voteMap = new voteTypeMapping();
    }


    ////////////////////////////////////////////////////////////
    //
    // Will get the current tasks for the user and return them in an array of tasks
    //
    ////////////////////////////////////////////////////////////
    public void getCurrentTasks(userInfo uInfo, tInfoArrayCallback callback) {

        try{
            //Ensure the uInfo has a valid id
            if(uInfo.id == null) {
                Log.w(TAG, "getCurrentTasks(user) called with null userID");

                //We cannot access db without valid id for user. Return failure.
                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "getCurrentTasks(user) where tasks are assignedTo " + uInfo.id);

                //Get documents from the collection that have user_id specified
                db.collection("tasks").whereEqualTo("assignedTo." + uInfo.id + ".exists", true).get()
                    .addOnCompleteListener(queryResult -> {

                        if(queryResult.isSuccessful()) {
                            Log.d(TAG, "Successfully Query in getCurrentTasks(user)");

                            List<taskInfo> taskInfoList = new ArrayList<taskInfo>();
                            for(QueryDocumentSnapshot document : Objects.requireNonNull(queryResult.getResult())) {

                                firebaseTaskDocument taskData = document.toObject(firebaseTaskDocument.class);
                                taskInfoList.add(taskData.toTaskInfo(document.getId()));

                                Log.d(TAG, "Collected Task Document: " + document.getId());
                            }

                            //Convert list to array and return
                            taskInfo[] taskInfoArray = new taskInfo[taskInfoList.size()];
                            taskInfoList.toArray(taskInfoArray);
                            callback.onReturn(taskInfoArray, true, NO_ERROR);

                        }
                        else {
                            Log.d(TAG, "getCurrentTasks(user): Error getting tasks for user: ", queryResult.getException());

                            //Call function to return task value
                            callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                        }
                    });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will get the current tasks for the house specified
    //
    ////////////////////////////////////////////////////////////
    public void getCurrentTasks(houseInfo hInfo, tInfoArrayCallback callback) {

        try{
            //Ensure the uInfo has a valid id
            if(hInfo.id == null) {
                Log.d(TAG, "getCurrentTasks(house) passed null houseID");

                //We cannot access db without valid id for user. Return failure.
                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "getCurrentTasks(house) where tasks are apart of house " + hInfo.id);

                //Get documents from the collection that have house_id specified
                db.collection("tasks").whereEqualTo("house_id", hInfo.id).get()
                    .addOnCompleteListener(queryResult -> {

                        if(queryResult.isSuccessful()) {
                            Log.d(TAG, "Successfully Query in getCurrentTasks(house)");

                            List<taskInfo> taskInfoList = new ArrayList<taskInfo>();
                            for(QueryDocumentSnapshot document : Objects.requireNonNull(queryResult.getResult())) {

                                firebaseTaskDocument taskData = document.toObject(firebaseTaskDocument.class);
                                taskInfoList.add(taskData.toTaskInfo(document.getId()));

                                Log.d(TAG, "Collected Task Document: " + document.getId());
                            }

                            //Convert list to array and return
                            taskInfo[] taskInfoArray = new taskInfo[taskInfoList.size()];
                            taskInfoList.toArray(taskInfoArray);
                            callback.onReturn(taskInfoArray, true, NO_ERROR);

                        }
                        else {
                            Log.d(TAG, "getCurrentTasks(house): Error getting tasks for user: ", queryResult.getException());

                            //Call function to return task value
                            callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                        }
                    });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will get the current tasks for the user and house specified
    //
    ////////////////////////////////////////////////////////////
    public void getCurrentTasks(userInfo uInfo, String house_id, tInfoArrayCallback callback) {

        try{
            //Ensure the uInfo has a valid id
            if(uInfo.id == null || house_id == null) {
                Log.w(TAG, "getCurrentTasks(user, house_id) passed at least 1 null parameters ");

                //We cannot access db without valid id for user. Return failure.
                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "getCurrentTasks(user, house_id) for user: " + uInfo.id + " and house_id " + house_id);

                //Get documents from the collection that have user_id specified -- need to check house_id after document collected
                db.collection("tasks").whereEqualTo("assignedTo." + uInfo.id + ".exists", true).get()
                    .addOnCompleteListener(queryResult -> {

                        if(queryResult.isSuccessful()) {
                            Log.d(TAG, "Successfully Query in getCurrentTasks(user, house_id)");

                            List<taskInfo> taskInfoList = new ArrayList<taskInfo>();
                            for(QueryDocumentSnapshot document : Objects.requireNonNull(queryResult.getResult())) {

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
                            callback.onReturn(taskInfoArray, true, NO_ERROR);

                        }
                        else {
                            Log.d(TAG, "getCurrentTasks(user, house_id): Error getting tasks for user: ", queryResult.getException());

                            //Call function to return task value
                            callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                        }
                    });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will get the current votes for the task specified
    //
    ////////////////////////////////////////////////////////////
    public void getTaskVotes(String task_id, vInfoArrayCallback callback) {

        try{
            //Ensure the uInfo has a valid id
            if(task_id == null) {
                Log.w(TAG, "getTaskVotes passed with null task_id");

                //We cannot access db without valid id for user. Return failure.
                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "getTaskVotes for task: " + task_id);

                //Get documents from the collection that have user_id specified -- need to check house_id after document collected
                db.collection("voting").whereEqualTo("task_id", task_id).get()
                        .addOnCompleteListener(queryResult -> {

                            if(queryResult.isSuccessful()) {
                                Log.d(TAG, "Successfully Query in getTaskVotes");

                                List<votingInfo> voteInfoList = new ArrayList<votingInfo>();
                                for(QueryDocumentSnapshot document : Objects.requireNonNull(queryResult.getResult())) {

                                    //Convert queried document to taskData class
                                    firebaseVotingDocument voteData = document.toObject(firebaseVotingDocument.class);

                                    voteInfoList.add(voteData.toVotingInfo(document.getId()));
                                }

                                //Convert list to array and return
                                votingInfo[] vInfos = new votingInfo[voteInfoList.size()];
                                voteInfoList.toArray(vInfos);
                                callback.onReturn(vInfos, true, NO_ERROR);
                            }
                            else {
                                Log.d(TAG, "getTaskVotes: Error getting tasks for user: ", queryResult.getException());

                                //Call function to return task value
                                callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                            }
                        });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will determine the approval status and update the task to reflect that
    //
    ////////////////////////////////////////////////////////////
    public void approveTaskAssignment(taskInfo tInfo, String user_id, boolean taskApproved, tInfoCallback callback) {

        try{
            if(taskApproved) {
                //Set the task to approved
                tInfo.assignedTo.put(user_id, new taskAssignObj(Objects.requireNonNull(tInfo.assignedTo.get(user_id)).name, true, true));

                //check if all the other assignees have approved the task
                boolean allAssigneesApprove = true;
                for(String userID : tInfo.assignedTo.keySet()) {
                    if(!Objects.requireNonNull(tInfo.assignedTo.get(userID)).approved)
                        allAssigneesApprove = false;
                }

                //If every assignee approves of the task assignment change the status
                if(allAssigneesApprove) {
                    tInfo.status = statusMap.NOT_COMPLETE;       //Set task to not completed from awaiting approval
                }

                //ensure the task_id is true
                if(tInfo.id == null) {
                    Log.w(TAG, "Invalid task_id. task_id was null");

                    callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
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
                            callback.onReturn(tInfo, true, NO_ERROR);
                        })
                        .addOnFailureListener(e -> {
                            Log.w(TAG, "Error updating document", e);

                            //IndicateError
                            callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                        });
                }
            }
            else {
                boolean newAssignee = false;

                //Remove the user that declined the task from the assigned to map
                tInfo.assignedTo.remove(user_id);

                //Check if there is anyone still assigned to the task -- if not assign creator
                if(tInfo.assignedTo.size() < 1) {
                    tInfo.assignedTo.put(tInfo.createdBy_id, new taskAssignObj(tInfo.createdBy, true, true));      //Add the created by user to the task
                    newAssignee = true;
                }

                if(tInfo.id == null) {
                    Log.d(TAG, "Invalid task_id. task_id was null");

                    callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
                }
                else {
                    //
                    //Update the task in the database
                    //

                    //Create a custom class updatedTask to modify the document with
                    firebaseTaskDocument updatedTask = new firebaseTaskDocument(tInfo);

                    boolean finalNewAssignee = newAssignee;

                    WriteBatch batch  = db.batch();

                    //Remove task from user
                    Map<String,Object> oldUserUpdates = new HashMap<>();
                    oldUserUpdates.put("tasks." + tInfo.id, FieldValue.delete());

                    DocumentReference oldUserDoc = db.collection("users").document(user_id);
                    batch.update(oldUserDoc, oldUserUpdates);

                    if(finalNewAssignee) {
                        //Add task to other user
                        Map<String,Object> newUserUpdates = new HashMap<>();
                        newUserUpdates.put("tasks." + tInfo.id, new nameObj(updatedTask.getDisplayName(), true));

                        DocumentReference newUserDoc = db.collection("users").document(tInfo.createdBy_id);
                        batch.update(newUserDoc, newUserUpdates);
                    }


                    //Update the task
                    Map<String,Object> taskUpdates = new HashMap<>();
                    taskUpdates.put("assignedTo", updatedTask.getAssignedTo());

                    DocumentReference taskToUpdate = db.collection("tasks").document(tInfo.id);
                    batch.update(taskToUpdate, taskUpdates);


                    //Commit the batch to update database
                    batch.commit().addOnCompleteListener(task ->{
                        if(task.isSuccessful()) {
                            Log.d(TAG, "Task Document successfully updated!");

                            //tInfo updated successfully so we can simply return it
                            callback.onReturn(tInfo, true, NO_ERROR);
                        }
                        else {
                            Log.d(TAG, "Error with task acceptance for task: " + tInfo.id);

                            callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                        }
                    });
                }
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will set up a vote for the task dispute in order to determine the state
    //
    ////////////////////////////////////////////////////////////
    public void disputeTask(taskInfo tInfo, tInfoCallback callback) {

        try{
            //Ensure the task  and disputer don't  have null values
            if(tInfo == null || tInfo.id == null) {
                Log.w(TAG, "Invalid parameters. taskInfo or tInfo.id sent was null");

                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                //Set the new status of the task
                tInfo.status = statusMap.DISPUTE;

                //Create a vote info class
                votingInfo vInfo = new votingInfo();
                vInfo.house_id = tInfo.house_id;
                vInfo.houseName = tInfo.houseName;
                vInfo.task_id = tInfo.id;
                vInfo.taskName = tInfo.displayName;
                vInfo.type = voteMap.DISPUTE_COMPLETION;

                //Set the vote ending date
                Date endOfVote = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(endOfVote);
                c.add(Calendar.DATE, 1);            //Allow for one day of voting
                vInfo.endOfVote = c.getTime();


                //Convert info classes to data classes for database
                firebaseTaskDocument taskData = new firebaseTaskDocument(tInfo);
                firebaseVotingDocument voteData = new firebaseVotingDocument(vInfo);

                //Create write batch
                WriteBatch batch = db.batch();

                //Add update to task status
                DocumentReference taskRef = db.collection("tasks").document(tInfo.id);
                batch.update(taskRef, "status", taskData.getStatus());

                //Add the vote
                DocumentReference voteRef = db.collection("voting").document();
                batch.set(voteRef, voteData);

                //Write to database
                batch.commit().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d(TAG, "Task: " + tInfo.id + " Successfully disputed");

                        //Return the task info
                        callback.onReturn(tInfo, true, NO_ERROR);
                    }
                    else {
                        Log.d(TAG, "Error disputing Task: " + tInfo.id);

                        callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                    }
                });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will set up a vote for the task dispute in order to determine the state
    //
    ////////////////////////////////////////////////////////////
    public void requestExtension(taskInfo tInfo, tInfoCallback callback) {

        try{
            //Ensure the task  and disputer don't  have null values
            if(tInfo == null || tInfo.id == null) {
                Log.w(TAG, "Invalid parameters. taskInfo or tInfo.id sent was null");

                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                //Set the new status of the task
                if(tInfo.dueDate.before(new Date())) {
                    tInfo.status = statusMap.LATE;
                }
                else {
                    tInfo.status = statusMap.NOT_COMPLETE;
                }

                //Create a vote info class
                votingInfo vInfo = new votingInfo();
                vInfo.house_id = tInfo.house_id;
                vInfo.houseName = tInfo.houseName;
                vInfo.task_id = tInfo.id;
                vInfo.taskName = tInfo.displayName;
                vInfo.type = voteMap.DEADLINE_EXTENSION;

                //Set the vote ending date
                Date endOfVote = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(endOfVote);
                c.add(Calendar.DATE, 1);        //Allow for one day of voting
                vInfo.endOfVote = c.getTime();


                //Convert info classes to data classes for database
                firebaseTaskDocument taskData = new firebaseTaskDocument(tInfo);
                firebaseVotingDocument voteData = new firebaseVotingDocument(vInfo);

                //Create write batch
                WriteBatch batch = db.batch();

                //Add update to task status
                DocumentReference taskRef = db.collection("tasks").document(tInfo.id);
                batch.update(taskRef, "status", taskData.getStatus());

                //Add the vote
                DocumentReference voteRef = db.collection("voting").document();
                batch.set(voteRef, voteData);

                //Write to database
                batch.commit().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d(TAG, "Task: " + tInfo.id + " extension successfully requested");

                        //Return the task info
                        callback.onReturn(tInfo, true, NO_ERROR);
                    }
                    else {
                        Log.d(TAG, "Error requesting extension Task: " + tInfo.id);

                        callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                    }
                });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will get the task data from the document and convert to taskInfo class
    //
    ////////////////////////////////////////////////////////////
    public void getTaskInfo(String task_id, tInfoCallback callback) {

        try {
            if(task_id == null) {
                Log.w(TAG, "Invalid task_id. task_id was null");

                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                //Get document from the collection
                db.collection("tasks").document(task_id).get()
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "get() task document successful for task: " + task_id);

                        //Convert Task to taskInfo class
                        firebaseTaskDocument taskData = documentReference.toObject(firebaseTaskDocument.class);
                        assert taskData != null;
                        taskInfo tInfo = taskData.toTaskInfo(task_id);

                        //Call function to return task value
                        callback.onReturn(tInfo, true, NO_ERROR);
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error getting task info document", e);

                        //Return null to indicate error in task
                        callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                    });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will update all fields of the document
    //
    ////////////////////////////////////////////////////////////
    public void setTaskInfo(taskInfo tInfo, boolean reassigned, String oldAssignee_id, tInfoCallback callback) {

        try{
            if(tInfo.id == null) {
                Log.d(TAG, "setTaskInfo called with null task_id");

                //Indicate an error
                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "setTaskInfo called for task: " + tInfo.id);

                //Create a custom class updatedTask to modify the document with
                firebaseTaskDocument updatedTask = new firebaseTaskDocument(tInfo);

                //Create batch to update all of the tasks and houses with references along with user
                WriteBatch batch = db.batch();

                //If the task got reassigned
                if(reassigned) {
                    //Remove the task from the one user and add to the new user
                    Map<String,Object> userUpdate1 = new HashMap<>();
                    userUpdate1.put("tasks." + tInfo.id, FieldValue.delete());

                    DocumentReference userToUpdate1 = db.collection("users").document(oldAssignee_id);
                    batch.update(userToUpdate1, userUpdate1);


                    //Add new user to task
                    Map<String,Object> userUpdate2 = new HashMap<>();
                    userUpdate2.put("tasks." + tInfo.id, new nameObj(updatedTask.getDisplayName(), true));

                    DocumentReference userToUpdate2 = db.collection("users").document(oldAssignee_id);
                    batch.update(userToUpdate2, userUpdate2);
                }


                //Add the house to update the display name
                Map<String,Object> houseUpdates = new HashMap<>();
                houseUpdates.put("tasks." + tInfo.id + ".name", updatedTask.getDisplayName());

                DocumentReference houseToUpdate = db.collection("houses").document(updatedTask.getHouse_id());
                batch.update(houseToUpdate, houseUpdates);


                //Add all the tasks to update the display name
                for(String user_id : tInfo.assignedTo.keySet()) {
                    Map<String,Object> updates = new HashMap<>();
                    updates.put("tasks." + tInfo.id + ".name", updatedTask.getDisplayName());

                    DocumentReference userToUpdate = db.collection("users").document(user_id);
                    batch.update(userToUpdate, updates);
                }


                //Update the task information
                DocumentReference taskToUpdate = db.collection("tasks").document(tInfo.id);
                batch.set(taskToUpdate, updatedTask);


                //Commit updates
                batch.commit().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d(TAG, "Task " + tInfo.id + " successfully updated");

                        callback.onReturn(tInfo, true, NO_ERROR);
                    }
                    else {
                        Log.d(TAG, "Update of task " + tInfo.id  + " unsuccessful");

                        callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                    }
                });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will update a single parameter of the task document
    // as specified by the input
    //
    ////////////////////////////////////////////////////////////
    public void setTaskInfo(taskInfo tInfo, String parameter, tInfoCallback callback) {

        try{
            //Get the field to be updated
            Map<String, Object> updateField = getUpdatedTaskField(tInfo, parameter);

            if(updateField == null) {
                Log.w(TAG, "setTaskInfo(param) resulted in null field");
                //Return error
                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else if(tInfo.id == null) {
                Log.w(TAG, "setTaskInfo(param) called with null task_id");
                //Return error
                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else if(parameter.equals("assignedTo")) {
                Log.w(TAG, "setTaskInfo(param) can't reassign tasks");
                //Return error
                callback.onReturn(null, false, "You are not able to reassign tasks this way. Sorry!");
            }
            else {
                //Create batch to do multiple updates -- in case it's needed
                WriteBatch batch = db.batch();

                if(parameter.equals("displayName")) {
                    Log.d(TAG, "setTaskInfo(param) called to update displayName for task: " + tInfo.id);

                    //Add the house to update the display name
                    Map<String,Object> houseUpdates = new HashMap<>();
                    houseUpdates.put("tasks." + tInfo.id + ".name", tInfo.displayName);

                    DocumentReference houseToUpdate = db.collection("houses").document(tInfo.house_id);
                    batch.update(houseToUpdate, houseUpdates);


                    //Add all the tasks to update the display name
                    for(String user_id : tInfo.assignedTo.keySet()) {
                        Map<String,Object> updates = new HashMap<>();
                        updates.put("tasks." + tInfo.id + ".name", tInfo.displayName);

                        DocumentReference userToUpdate = db.collection("users").document(user_id);
                        batch.update(userToUpdate, updates);
                    }
                }
                //Tasks won't be re assigned through this method

                //Update the task information
                DocumentReference taskToUpdate = db.collection("tasks").document(tInfo.id);
                batch.set(taskToUpdate, updateField);


                //Update the documents
                batch.commit().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d(TAG, "Task Document successfully updated!");

                        //tInfo updated successfully so we can simply return it
                        callback.onReturn(tInfo, true, NO_ERROR);
                    }
                    else {
                        Log.w(TAG, "Error updating document");

                        //IndicateError
                        callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                    }
                });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will create the task in the database
    //
    ////////////////////////////////////////////////////////////
    public void createTask(taskInfo tInfo, tInfoCallback callback) {

        try{
            boolean taskAssignedAway = false;

            if(tInfo.assignedTo != null && tInfo.house_id != null && tInfo.createdBy_id != null) {
                Log.d(TAG, "createTask called for task: " + tInfo.displayName);

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


                //Create custom class to generate a new document
                firebaseTaskDocument newTask = new firebaseTaskDocument(tInfo);

                //Add the new task to the tasks collection
                db.collection("tasks").add(newTask)
                    .addOnSuccessListener( documentReference -> {
                        Log.d(TAG, "Task added with ID: " + documentReference.getId());

                        //Set the id of the task
                        tInfo.id = documentReference.getId();

                        WriteBatch batch = db.batch();

                        //Add task to house
                        Map<String, Object> houseUpdate = new HashMap<>();
                        houseUpdate.put("tasks." + tInfo.id, new nameObj(tInfo.displayName, true));

                        DocumentReference houseRef = db.collection("houses").document(tInfo.house_id);
                        batch.update(houseRef, houseUpdate);


                        //Add task to users who the task is assigned to
                        for(String taskAssignee_id : newTask.getAssignedTo().keySet()) {
                            Map<String, Object> userUpdate = new HashMap<>();
                            userUpdate.put("tasks." + tInfo.id, new nameObj(tInfo.displayName, true));

                            DocumentReference userRef = db.collection("users").document(taskAssignee_id);
                            batch.update(userRef, userUpdate);
                        }

                        //Commit the writes to the databse and wait for completion
                        batch.commit().addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "Task: " + tInfo.id + " Successfully added");

                                //Return the task info
                                callback.onReturn(tInfo, true, NO_ERROR);
                            }
                            else {
                                Log.d(TAG, "Error adding Task: " + tInfo.id + " to house and users");

                                callback.onReturn(tInfo, false, DATABASE_ERROR_MESSAGE);
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "Error adding Task");

                        //Return null to indicate error in task
                        callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                    });
            }
            else {
                Log.w(TAG, "createTask called with invalid parameters");

                //Return null to indicate error in task creation
                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will delete the task and all references to the task in the system
    //
    ////////////////////////////////////////////////////////////
    public void deleteTask(taskInfo tInfo, booleanCallback callback) {

        try{
            if(tInfo.id == null) {
                Log.w(TAG, "deleteTask called with invalid task_id. task_id was null");

                callback.onReturn(false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "deleteTask called for task " + tInfo.id);

                WriteBatch batch = db.batch();

                Map<String, Object> removeTask = new HashMap<>();
                removeTask.put("tasks." + tInfo.id, FieldValue.delete());

                //Remove the task from the task list in the task house
                DocumentReference houseRef = db.collection("houses").document(tInfo.house_id);
                batch.update(houseRef, removeTask);

                //Remove the task from the task list in the task's assignees
                for(String taskAssignee_id : tInfo.assignedTo.keySet()) {
                    DocumentReference userRef = db.collection("users").document(taskAssignee_id);
                    batch.update(userRef, removeTask);
                }

                //If the task has an ongoing vote
                if(tInfo.status.equals(statusMap.DISPUTE) || tInfo.status.equals(statusMap.REASSIGNMENT_APPROVAL)) {
                    db.collection("voting").whereEqualTo("task_id", tInfo.id).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    DocumentReference voteRef = db.collection("voting").document(document.getId());
                                    batch.delete(voteRef);
                                }
                            }

                            //Delete the task from the page
                            DocumentReference taskRef = db.collection("tasks").document(tInfo.id);
                            batch.delete(taskRef);

                            //Commit the writes to the database and wait for completion
                            batch.commit().addOnCompleteListener(commitTask -> {
                                if(commitTask.isSuccessful()) {
                                    Log.d(TAG, "Task: " + tInfo.id + " Successfully deleted");

                                    //Return the task info
                                    callback.onReturn(true, NO_ERROR);
                                }
                                else {
                                    Log.d(TAG, "Error removing Task: " + tInfo.id);

                                    callback.onReturn(false, DATABASE_ERROR_MESSAGE);
                                }
                            });
                        });
                }
                else {
                    //Delete the task from the page
                    DocumentReference taskRef = db.collection("tasks").document(tInfo.id);
                    batch.delete(taskRef);

                    //Commit the writes to the database and wait for completion
                    batch.commit().addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "Task: " + tInfo.id + " Successfully deleted");

                            //Return the task info
                            callback.onReturn(true, NO_ERROR);
                        }
                        else {
                            Log.d(TAG, "Error removing Task: " + tInfo.id);

                            callback.onReturn(false, DATABASE_ERROR_MESSAGE);
                        }
                    });
                }
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(false, UNKNOWN_ERROR_MESSAGE);
        }
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
