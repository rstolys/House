package com.cmpt275.house.classDef;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmpt275.house.classDef.databaseObjects.voterObj;
import com.cmpt275.house.classDef.documentClass.firebaseHouseDocument;
import com.cmpt275.house.classDef.documentClass.firebaseUserDocument;
import com.cmpt275.house.classDef.documentClass.firebaseVotingDocument;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.classDef.mappingClass.roleMapping;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.hInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.hInfoCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoCallback;
import com.cmpt275.house.interfaceDef.HouseBE;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class houseFirebaseClass implements HouseBE {
    //
    // Class Variables
    //
    private final FirebaseFirestore db;
    private final String TAG = "FirebaseHouseAction";

    private final String NO_ERROR = "";
    private final String INVALID_PERMISSIONS_MESSAGE = "Looks like you don't have permission to do that. Sorry!";
    private final String INVALID_PARAMETER_MESSAGE = "Looks like we couldn't access your information. Try signing in again";
    private final String DATABASE_ERROR_MESSAGE = "Oops! Looks like there was an error on our end. Sorry about that. Please try again";
    private final String UNKNOWN_ERROR_MESSAGE = "Oops! Looks something went wrong there. Sorry about that. Please try again";

    private final roleMapping roleMap;



    //
    // Class Functions
    //

    ////////////////////////////////////////////////////////////
    //
    // Constructor which implements the callback functions
    //
    ////////////////////////////////////////////////////////////
    public houseFirebaseClass() {
        db = FirebaseFirestore.getInstance();
        roleMap = new roleMapping();
    }


    ////////////////////////////////////////////////////////////
    //
    // Gets the current houses for the specified user
    //
    ////////////////////////////////////////////////////////////
    public void getCurrentHouses(userInfo uInfo, hInfoArrayCallback callback) {

        try{
            //Ensure the uInfo has a valid id
            if(uInfo.id == null) {
                Log.d(TAG, "getCurrentHouses(user) called with null user_id ");
                //We cannot access db without valid id for user. Return failure.
                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "getCurrentHouses(user) for user " + uInfo.id);
                //Get documents from the collection that have house_id specified
                db.collection("houses").whereEqualTo("members." + uInfo.id + ".exists", true).get()
                    .addOnCompleteListener(queryResult -> {
                        if(queryResult.isSuccessful()) {
                            Log.d(TAG, "Successfully Query in getCurrentHouses(user)");

                            List<houseInfo> houseInfoList = new ArrayList<houseInfo>();
                            for(QueryDocumentSnapshot document : Objects.requireNonNull(queryResult.getResult())) {

                                firebaseHouseDocument houseData = document.toObject(firebaseHouseDocument.class);
                                houseInfoList.add(houseData.toHouseInfo(document.getId()));

                                Log.d(TAG, "Collected Task Document: " + document.getId());
                            }

                            //Convert list to array and return
                            houseInfo[] houseInfoArray = new houseInfo[houseInfoList.size()];
                            houseInfoList.toArray(houseInfoArray);
                            callback.onReturn(houseInfoArray, true, NO_ERROR);

                        }
                        else {
                            Log.d(TAG, "getCurrentHouses(user): Error getting houses for user: ", queryResult.getException());

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
    // Get list of houses in system
    //
    ////////////////////////////////////////////////////////////
    public void getAllHouses(hInfoArrayCallback callback) {
        try{
            db.collection("houses").get()
                .addOnCompleteListener(queryResult -> {
                    if(queryResult.isSuccessful()) {
                        Log.d(TAG, "Successfully Query in getAllHouses");

                        List<houseInfo> houseInfoList = new ArrayList<houseInfo>();
                        for(QueryDocumentSnapshot document : Objects.requireNonNull(queryResult.getResult())) {

                            firebaseHouseDocument houseData = document.toObject(firebaseHouseDocument.class);
                            houseInfoList.add(houseData.toHouseInfo(document.getId()));

                            Log.d(TAG, "Collected Task Document: " + document.getId());
                        }

                        //Convert list to array and return
                        houseInfo[] houseInfoArray = new houseInfo[houseInfoList.size()];
                        houseInfoList.toArray(houseInfoArray);
                        callback.onReturn(houseInfoArray, true, NO_ERROR);

                    }
                    else {
                        Log.d(TAG, "getAllHouses: Error getting houses for user: ", queryResult.getException());

                        //Call function to return task value
                        callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                    }
                });
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Gets the house information correlated to the house_id provided
    //
    ////////////////////////////////////////////////////////////
    public void getHouseInfo(String house_id, hInfoCallback callback) {

        try{
            //Ensure the uInfo has a valid id
            if(house_id == null) {
                Log.d(TAG, "getHouseInfo called with null house_id");
                //We cannot access db without valid id for house. Return failure.
                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "getHouseInfo for house: " + house_id);

                //Get documents from the collection that have house_id specified
                db.collection("houses").document(house_id).get()
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "get() house document successful for task: " + house_id);

                        //Convert Task to taskInfo class
                        firebaseHouseDocument houseData = documentReference.toObject(firebaseHouseDocument.class);
                        assert houseData != null;
                        houseInfo hInfo = houseData.toHouseInfo(house_id);

                        //Call function to return task value
                        callback.onReturn(hInfo, true, NO_ERROR);
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error getting house info document", e);

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
    // Creates a new house from the houseInfo class
    //
    ////////////////////////////////////////////////////////////
    public void createNewHouse(houseInfo hInfo, hInfoCallback callback) {

        try{
            //Make sure there is at least one house member
            if(hInfo.members.isEmpty()) {
                Log.d(TAG, "House Needs at least one member");

                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                //Create custom class to generate a new document
                firebaseHouseDocument newHouse = new firebaseHouseDocument(hInfo);

                //Add the new task to the tasks collection
                db.collection("houses").add(newHouse)
                    .addOnSuccessListener( documentReference -> {
                        Log.d(TAG, "House added with ID: " + documentReference.getId());

                        //Set the id of the house
                        hInfo.id = documentReference.getId();

                        //Return the house info
                        callback.onReturn(hInfo, true, NO_ERROR);
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding document", e);

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
    // Deletes the house and references to the house
    //
    ////////////////////////////////////////////////////////////
    public void deleteHouse(houseInfo hInfo, String user_id, booleanCallback callback) {

        try{
            if(hInfo.id != null && user_id != null){
                Log.d(TAG, "deleteHouse called for house: " + hInfo.id);

                //Make sure the user trying to delete the task is an admin
                if(!Objects.requireNonNull(hInfo.members.get(user_id)).role.equals(roleMap.ADMIN)) {
                    Log.d(TAG, "deleteHouse cannot be done by a non-admin member");

                    callback.onReturn(false, INVALID_PERMISSIONS_MESSAGE);
                }
                else {
                    WriteBatch batch = db.batch();

                    //Add all tasks to delete to the batch
                    for(String task_id : hInfo.tasks.keySet()) {
                        DocumentReference deleteTask = db.collection("tasks").document(task_id);
                        batch.delete(deleteTask);
                    }

                    //Add all the votes to delete to the batch
                    for(int i = 0; i < hInfo.voting_ids.size(); i++) {
                        DocumentReference deleteVote = db.collection("voting").document(hInfo.voting_ids.get(i));
                        batch.delete(deleteVote);
                    }

                    //Add all the users to remove this house from their account
                    for(String userID : hInfo.members.keySet()) {
                        Map<String,Object> updates = new HashMap<>();
                        updates.put("houses." + hInfo.id, FieldValue.delete());

                        DocumentReference userToUpdate = db.collection("users").document(userID);
                        batch.update(userToUpdate, updates);
                    }

                    //Add the house to the batch to delete
                    DocumentReference deleteHouse = db.collection("houses").document(hInfo.id);
                    batch.delete(deleteHouse);


                    //Commit all the writes and await completion
                    batch.commit().addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "House " + hInfo.id  + " successfully deleted");

                            callback.onReturn(true, NO_ERROR);
                        }
                        else {
                            Log.d(TAG, "Delete of House " + hInfo.id  + " unsuccessful");

                            callback.onReturn(false, DATABASE_ERROR_MESSAGE);
                        }
                    });
                }
            }
            else {
                Log.d(TAG, "deleteHouse called with null house ID or callerID");

                callback.onReturn(false, INVALID_PARAMETER_MESSAGE);
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(false, UNKNOWN_ERROR_MESSAGE);
        }
    }



    ////////////////////////////////////////////////////////////
    //
    // Sets the user role in the house
    //
    ////////////////////////////////////////////////////////////
    public void setUserRole(houseInfo hInfo, String user_id, String role, hInfoCallback callback){

        try{
            if(hInfo.id == null) {
                Log.d(TAG, "House provided has null id");

                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "setUserRole called for user: " + user_id + " to set role of: " + role);

                //Make sure the user is a part of the house
                if(hInfo.members.containsKey(user_id)) {

                    boolean stillAnAdmin = false;

                    //If this member is currently an admin and will be changed out of an admin role
                    //  we need to make sure there will still be an admin member of the house
                    if( (Objects.requireNonNull(hInfo.members.get(user_id)).role.equals(roleMap.ADMIN)) && (!roleMap.ADMIN.equals(role)) ) {

                        //Make sure they will still be at least one admin in the house
                        for(String userID : hInfo.members.keySet()) {
                            if(Objects.requireNonNull(hInfo.members.get(userID)).role.equals(roleMap.ADMIN) && !userID.equals(user_id)) {
                                stillAnAdmin = true;
                                break;
                            }
                        }
                    }

                    if(stillAnAdmin) {
                        //change the role for the specified user
                        houseMemberInfoObj newRole = new houseMemberInfoObj(Objects.requireNonNull(hInfo.members.get(user_id)).name, role);
                        hInfo.members.put(user_id, newRole);

                        //Convert info class to document
                        firebaseHouseDocument houseData = new firebaseHouseDocument(hInfo);

                        //Update this information in the house document
                        db.collection("houses").document(hInfo.id).update("members." + user_id, newRole)
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "House Document successfully updated!");

                                //hInfo updated successfully so we can simply return it
                                callback.onReturn(hInfo, true, NO_ERROR);
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error updating document", e);

                                //IndicateError
                                callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                            });
                    }
                    else {
                        Log.d(TAG, "House cannot exist without an Admin Member");

                        callback.onReturn(null, false, "A house must always have at least 1 admin member. Try promoting someone else to admin member first.");
                    }
                }
                else {
                    Log.d(TAG, "User: " + user_id + " is not a member of this house");

                    callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
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
    // Add a member to the house
    //
    ////////////////////////////////////////////////////////////
    public void addMember(houseInfo hInfo, String user_id, String role, String userName, hInfoCallback callback){

        try{
            if(hInfo.id == null) {
                Log.d(TAG, "House provided has null id");

                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "addMember called for user: " + user_id + " and role of: " + role);
                //Add the user to the map
                hInfo.members.put(user_id, new houseMemberInfoObj(userName, role));

                //Convert info class to document
                firebaseHouseDocument houseData = new firebaseHouseDocument(hInfo);

                //Update this information in the house document
                db.collection("houses").document(hInfo.id).update("members", houseData.getMembers())
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "User successfully added to house: " + hInfo.id);

                        //hInfo updated successfully, return updated hInfo
                        callback.onReturn(hInfo, true, NO_ERROR);
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error updating document", e);

                        //IndicateError
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
    // Will remove a house from the
    //
    ////////////////////////////////////////////////////////////
    public void removeMember(houseInfo hInfo, String callerID, String user_id, booleanCallback callback) {

        try{
            if(hInfo.id == null) {
                Log.d(TAG, "removeMember: House id passed is null");

                callback.onReturn(false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "removeMember called for member " + user_id);

                //Verify that the caller is either the user to be removed or an admin of the house and the user is not an admin
                if(user_id.equals(callerID) || Objects.requireNonNull(hInfo.members.get(callerID)).role.equals(roleMap.ADMIN)) {
                    //Make sure the user being removed is not an admin as well
                    if(user_id.equals(callerID) || !hInfo.members.get(user_id).role.equals(roleMap.ADMIN)) {

                        WriteBatch batch = db.batch();

                        //** Tasks that were assigned to that user will remain in exsistance in the task collection
                        //** We probably want to remove them but it is a complex operation to do so. Will leave them for now

                        //Remove the house from the user
                        Map<String,Object> updateUser = new HashMap<>();
                        updateUser.put("houses." + hInfo.id, FieldValue.delete());

                        DocumentReference userRef = db.collection("users").document(user_id);
                        batch.update(userRef, updateUser);


                        //remove the member from the house
                        Map<String,Object> updateHouse = new HashMap<>();
                        updateHouse.put("members." + user_id, FieldValue.delete());


                        DocumentReference houseRef = db.collection("houses").document(hInfo.id);
                        batch.update(houseRef, updateHouse);

                        batch.commit().addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "House member " + user_id + " successfully removed");

                                callback.onReturn(true, NO_ERROR);
                            }
                            else {
                                Log.d(TAG, "Removal of house member " + user_id + " unsuccessful");

                                callback.onReturn(false, DATABASE_ERROR_MESSAGE);
                            }
                        });
                    }
                    else {
                        Log.d(TAG, "removeMember: Cannot remove another admin member. They must remove themselves");

                        callback.onReturn(false, "You can't remove an admin member from the house");
                    }
                }
                else {
                    Log.d(TAG, "removeMember: This caller: " + callerID + " does not have permission to remove the house member");

                    callback.onReturn(false, INVALID_PERMISSIONS_MESSAGE);
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
    // Will access the votes associated with a house
    //
    ////////////////////////////////////////////////////////////
    public void getHouseVotes(String house_id, vInfoArrayCallback callback){

        try{
            if(house_id == null) {
                Log.d(TAG, "getHouseVotes: House id passed is null");

                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "getHouseVotes called for house " + house_id);
                //Get the documents matching the voting_ids
                db.collection("voting").whereEqualTo("house_id", house_id).get()
                    .addOnCompleteListener(queryResult -> {

                        if(queryResult.isSuccessful()) {
                            Log.d(TAG, "Successfully Query in getHouseVotes");

                            List<votingInfo> votingInfoList = new ArrayList<votingInfo>();
                            for(QueryDocumentSnapshot document : Objects.requireNonNull(queryResult.getResult())) {

                                //Convert queried document to taskData class
                                firebaseVotingDocument votingData = document.toObject(firebaseVotingDocument.class);

                                votingInfoList.add(votingData.toVotingInfo(document.getId()));
                            }

                            //Convert list to array and return
                            votingInfo[] votingInfoArray = new votingInfo[votingInfoList.size()];
                            votingInfoList.toArray(votingInfoArray);
                            callback.onReturn(votingInfoArray, true, NO_ERROR);

                        }
                        else {
                            Log.d(TAG, "getHouseVotes: Error getting voting for house: " + house_id, queryResult.getException());

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
    // Will add the users vote to the vote total
    //
    ////////////////////////////////////////////////////////////
    public void submitVote(votingInfo vInfo, String userName, String user_id, boolean yesVote, vInfoCallback callback){

        try{
            if(vInfo.id == null || user_id == null) {
                Log.d(TAG, "submitVote: voting_id or user_id is null");

                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "submitVote called for user: " + user_id + " and vote " + yesVote);
                //**Should we verify this user is allowed to vote (is a part of house)
                //  won't be issue in normal operation, would need many errors for this to occur

                //TODO: check that user has not already voted
                //Add the vote to the vInfo
                vInfo.voters.put(user_id, new voterObj(userName, true, yesVote));

                if(yesVote) {
                    vInfo.yesVotes++;
                }
                else {
                    vInfo.noVotes++;
                }

                //Convert the vote to a firebase document
                firebaseVotingDocument voteData = new firebaseVotingDocument(vInfo);

                //Add voteData to the database
                db.collection("voting").document(vInfo.id).update("voters", voteData.getVoters())
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Vote successfully added to vote: " + vInfo.id);

                        //hInfo updated successfully, return updated hInfo
                        callback.onReturn(vInfo, true, NO_ERROR);
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error updating document", e);

                        //IndicateError
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
    // Will edit the house settings
    //
    ////////////////////////////////////////////////////////////
    public void editSettings(houseInfo hInfo, boolean displayNameChanged, hInfoCallback callback){

        try{
            if(hInfo.id == null) {
                Log.d(TAG, "editSettings: house_id is null");

                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "editSettings called for house: " + hInfo.id);

                //Convert the houseInfo to a firebase document
                firebaseHouseDocument houseData = new firebaseHouseDocument(hInfo);

                //Create batch to make updates to name
                WriteBatch batch = db.batch();

                if(displayNameChanged){     //We need to update all of the tasks, votes and users with references

                    //Add all the users to update the display name
                    for(String user_id : hInfo.members.keySet()) {
                        Map<String,Object> updates = new HashMap<>();
                        updates.put("houses." + hInfo.id + ".name", hInfo.displayName);

                        DocumentReference userToUpdate = db.collection("users").document(user_id);
                        batch.update(userToUpdate, updates);
                    }

                    //Add all the tasks to update the display name
                    for(String task_id : hInfo.tasks.keySet()) {
                        Map<String,Object> updates = new HashMap<>();
                        updates.put("houseName", hInfo.displayName);

                        DocumentReference taskToUpdate = db.collection("tasks").document(task_id);
                        batch.update(taskToUpdate, updates);
                    }

                    //Add all the votes to update the display name
                    for(int i = 0; i < hInfo.voting_ids.size(); i++) {
                        Map<String,Object> updates = new HashMap<>();
                        updates.put("houseName", hInfo.displayName);

                        DocumentReference voteToUpdate = db.collection("voting").document(hInfo.voting_ids.get(i));
                        batch.update(voteToUpdate, updates);
                    }


                }

                //Add the updates settings to the batch
                DocumentReference houseUpdates = db.collection("houses").document(hInfo.id);
                batch.update(houseUpdates, "description" , houseData.getDescription(),
                        "displayName", houseData.getDisplayName(),
                        "houseNotifications", houseData.getHouseNotifications(),
                        "maxMembers", houseData.getMaxMembers(),
                        "punishmentMultiplier", houseData.getPunishmentMultiplier());


                //Commit the writes to the database and wait for completion
                batch.commit().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d(TAG, "House Settings of " + hInfo.id  + " successfully updated");

                        callback.onReturn(hInfo, true, NO_ERROR);
                    }
                    else {
                        Log.d(TAG, "Update of settings for House " + hInfo.id  + " unsuccessful");

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
    // Invite user to join house
    //
    ////////////////////////////////////////////////////////////
    public void inviteUserToHouse(String newUserID, String newUserEmail, houseInfo hInfo, userInfo uInfo, hInfoCallback callback) {

        try {
            //Check that the user making the call is an admin user
            if(!hInfo.members.get(uInfo.id).role.equals(roleMap.ADMIN)) {
                Log.d(TAG, "inviteUserToHouse: user is not admin");

                callback.onReturn(null, false, INVALID_PERMISSIONS_MESSAGE);
            }
            //Check that the house has not reached its maximum members
            else if (hInfo.members.size() >= hInfo.maxMembers) {
                Log.d(TAG, "inviteUserToHouse: House has already reached maximum members");

                callback.onReturn(null, false, "Looks like the house is already full. Try changing the house settings first");
            }
            else if(newUserID == null) {
                Log.d(TAG, "inviteUserToHouse: new userID was null");

                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                //Get the new users information to add to system
                db.collection("users").document(newUserID).get()
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "get() of user document successful for inviteUserToHouse");

                        //Convert User to userInfo class
                        firebaseUserDocument newUserData = documentReference.toObject(firebaseUserDocument.class);
                        assert newUserData != null;
                        userInfo newUInfo = newUserData.toUserInfo(newUserID, newUserEmail);

                        //Add house to user account
                        newUInfo.houses.put(hInfo.id, hInfo.displayName);

                        //Add user to house
                        hInfo.members.put(newUserID, new houseMemberInfoObj(newUInfo.displayName, roleMap.MEMBER));


                        //Convert user and house Info to database objects
                        newUserData = new firebaseUserDocument(newUInfo);
                        firebaseHouseDocument houseData = new firebaseHouseDocument(hInfo);

                        //Write new userInfo and houseInfo to database in batch
                        WriteBatch batch = db.batch();

                        //Add userUpdate to write batch
                        Map<String,Object> userUpdate = new HashMap<>();
                        userUpdate.put("houses", newUserData.getHouses());

                        DocumentReference userDocRef = db.collection("users").document(newUInfo.id);
                        batch.update(userDocRef, userUpdate);

                        //Add houseUpdate to write batch
                        Map<String,Object> houseUpdate = new HashMap<>();
                        houseUpdate.put("members", houseData.getMembers());

                        DocumentReference houseDocRef = db.collection("houses").document(hInfo.id);
                        batch.update(houseDocRef, houseUpdate);

                        //Commit the writes to the database and wait for completion
                        batch.commit().addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "House member " + newUserID + " successfully added to house " + hInfo.id);

                                callback.onReturn(hInfo, true, NO_ERROR);
                            }
                            else {
                                Log.d(TAG, "House member wasn't added successfully");

                                callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "inviteUserToHouse: user does not exist", e);

                        //Call function to return task value
                        callback.onReturn(hInfo, false, "Looks like that user does not exist. Try adding a someone else.");

                    });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }
} // END of houseFirebaseClass
