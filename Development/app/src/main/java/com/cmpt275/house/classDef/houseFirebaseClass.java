package com.cmpt275.house.classDef;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmpt275.house.classDef.databaseObjects.houseMemberObj;
import com.cmpt275.house.classDef.databaseObjects.voterObj;
import com.cmpt275.house.classDef.documentClass.firebaseHouseDocument;
import com.cmpt275.house.classDef.documentClass.firebaseVotingDocument;
import com.cmpt275.house.classDef.infoClass.houseInfo;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class houseFirebaseClass implements HouseBE {
    //
    // Class Variables
    //
    private final FirebaseFirestore db;
    private final String TAG = "FirebaseHouseAction";

    // TODO: add variables below to documentation
    private final roleMapping roleMap;


    //Also need to update documentation with new version of callbacks



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

        Log.d(TAG, "getCurrentHouses(user) for user " + uInfo.id);

        //Ensure the uInfo has a valid id
        if(uInfo.id == null) {
            //We cannot access db without valid id for user. Return failure.
            callback.onReturn(null, false);
        }
        else {
            //Get documents from the collection that have house_id specified
            db.collection("houses").whereEqualTo("members." + uInfo.id + ".exists", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> queryResult) {

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
                            callback.onReturn(houseInfoArray, true);

                        }
                        else {
                            Log.d(TAG, "getCurrentHouses(user): Error getting houses for user: ", queryResult.getException());

                            //Call function to return task value
                            callback.onReturn(null, false);
                        }
                    }
                });
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Gets the house information correlated to the house_id provided
    //
    ////////////////////////////////////////////////////////////
    public void getHouseInfo(String house_id, hInfoCallback callback) {

        Log.d(TAG, "getHouseInfo for house: " + house_id);

        //Ensure the uInfo has a valid id
        if(house_id == null) {
            //We cannot access db without valid id for house. Return failure.
            callback.onReturn(null, false);
        }
        else {
            //Get documents from the collection that have house_id specified
            db.collection("houses").document(house_id).get()
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "get() house document successful for task: " + house_id);

                    //Convert Task to taskInfo class
                    firebaseHouseDocument houseData = documentReference.toObject(firebaseHouseDocument.class);
                    assert houseData != null;
                    houseInfo hInfo = houseData.toHouseInfo(house_id);

                    //Call function to return task value
                    callback.onReturn(hInfo, true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error getting house info document", e);

                    //Return null to indicate error in task
                    callback.onReturn(null, false);
                });
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Creates a new house from the houseInfo class
    //
    ////////////////////////////////////////////////////////////
    public void createNewHouse(houseInfo hInfo, hInfoCallback callback) {

        //Make sure there is at least one house member
        if(hInfo.members.isEmpty()) {
            Log.d(TAG, "House Needs at least one member");

            callback.onReturn(null, false);
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
                    callback.onReturn(hInfo, true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);

                    //Return null to indicate error in task
                    callback.onReturn(null, false);
                });
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Deletes the house and references to the house
    //
    ////////////////////////////////////////////////////////////
    public void deleteHouse(houseInfo hInfo, booleanCallback bCallback) {}



    ////////////////////////////////////////////////////////////
    //
    // Sets the user role in the house
    //
    ////////////////////////////////////////////////////////////
    public void setUserRole(houseInfo hInfo, String user_id, String role, hInfoCallback callback){

        Log.d(TAG, "setUserRole called for user: " + user_id + " to set role of: " + role);

        if(hInfo.id == null) {
            Log.d(TAG, "House provided has null id");

            callback.onReturn(null, false);
        }
        else {
            //Make sure the user is a part of the house
            if(hInfo.members.containsKey(user_id)) {

                boolean stillAnAdmin = false;

                //If this member is currently an admin and will be changed out of an admin role
                //  we need to make sure there will still be an admin member of the house
                if( (Objects.requireNonNull(hInfo.members.get(user_id)).role.equals(roleMap.ADMIN_NUM)) && (!roleMap.ADMIN.equals(role)) ) {

                    //Make sure they will still be at least one admin in the house
                    for(String userID : hInfo.members.keySet()) {
                        if(Objects.requireNonNull(hInfo.members.get(userID)).role.equals(roleMap.ADMIN_NUM) && !userID.equals(user_id)) {
                            stillAnAdmin = true;
                            break;
                        }
                    }
                }

                if(stillAnAdmin) {
                    //change the role for the specified user
                    houseMemberObj newRole = new houseMemberObj(Objects.requireNonNull(hInfo.members.get(user_id)).name, true, roleMap.mapStringToInt(role));
                    hInfo.members.put(user_id, newRole);

                    //Convert info class to document
                    firebaseHouseDocument houseData = new firebaseHouseDocument(hInfo);

                    //Update this information in the house document
                    db.collection("houses").document(hInfo.id).update("members." + user_id, newRole)
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "House Document successfully updated!");

                            //hInfo updated successfully so we can simply return it
                            callback.onReturn(hInfo, true);
                        })
                        .addOnFailureListener(e -> {
                            Log.w(TAG, "Error updating document", e);

                            //IndicateError
                            callback.onReturn(null, false);
                        });
                }
                else {
                    Log.d(TAG, "House cannot exist without an Admin Member");

                    callback.onReturn(null, false);
                }
            }
            else {
                Log.d(TAG, "User: " + user_id + " is not a member of this house");

                callback.onReturn(null, false);
            }

        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Add a member to the house
    //
    ////////////////////////////////////////////////////////////
    public void addMember(houseInfo hInfo, String user_id, String role, String userName, hInfoCallback callback){

        Log.d(TAG, "addMember called for user: " + user_id + " and role of: " + role);

        if(hInfo.id == null) {
            Log.d(TAG, "House provided has null id");

            callback.onReturn(null, false);
        }
        else {
            //Add the user to the map
            hInfo.members.put(user_id, new houseMemberObj(userName, true, roleMap.mapStringToInt(role)));

            //Convert info class to document
            firebaseHouseDocument houseData = new firebaseHouseDocument(hInfo);

            //Update this information in the house document
            db.collection("houses").document(hInfo.id).update("members", houseData.getMembers())
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "User successfully added to house: " + hInfo.id);

                    //hInfo updated successfully, return updated hInfo
                    callback.onReturn(hInfo, true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating document", e);

                    //IndicateError
                    callback.onReturn(null, false);
                });
        }
    }

    ////////////////////////////////////////////////////////////
    //
    // Will remove a house from the
    //
    ////////////////////////////////////////////////////////////
    public void deleteMember(houseInfo hInfo, String user_id, booleanCallback callback){}


    ////////////////////////////////////////////////////////////
    //
    // Will access the votes associated with a house
    //
    ////////////////////////////////////////////////////////////
    public void getHouseVotes(String house_id, vInfoArrayCallback callback){

        Log.d(TAG, "getHouseVotes called for house " + house_id);

        if(house_id == null) {
            Log.d(TAG, "getHouseVotes: House id passed is null");

            callback.onReturn(null, false);
        }
        else {
            //Get the documents matching the voting_ids
            db.collection("voting").whereEqualTo("house_id", house_id).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> queryResult) {

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
                            callback.onReturn(votingInfoArray, true);

                        }
                        else {
                            Log.d(TAG, "getHouseVotes: Error getting voting for house: " + house_id, queryResult.getException());

                            //Call function to return task value
                            callback.onReturn(null, false);
                        }
                    }
                });
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will add the users vote to the vote total
    //
    ////////////////////////////////////////////////////////////
    public void submitVote(votingInfo vInfo, String userName, String user_id, boolean yesVote, vInfoCallback callback){

        Log.d(TAG, "submitVote called for user: " + user_id + " and vote " + yesVote);

        if(vInfo.id == null || user_id == null) {
            Log.d(TAG, "submitVote: voting_id or user_id is null");

            callback.onReturn(null, false);
        }
        else {
            //**Should we verify this user is allowed to vote (is a part of house)
            //  won't be issue in normal operation, would need many errors for this to occur

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
                    callback.onReturn(vInfo, true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating document", e);

                    //IndicateError
                    callback.onReturn(null, false);
                });
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will edit the house settings
    //
    ////////////////////////////////////////////////////////////
    public void editSettings(houseInfo hInfo, hInfoCallback callback){

        Log.d(TAG, "editSettings called for house: " + hInfo.id);

        if(hInfo.id == null) {
            Log.d(TAG, "editSettings: house_id is null");

            callback.onReturn(null, false);
        }
        else {
            //Convert the houseInfo to a firebase document
            firebaseHouseDocument houseData = new firebaseHouseDocument(hInfo);

            //Add voteData to the database
            db.collection("houses").document(hInfo.id)
                .update("description" , houseData.getDescription(),
                        "displayName", houseData.getDisplayName(),
                        "houseNotifications", houseData.getHouseNotifications(),
                        "maxMembers", houseData.getMaxMembers(),
                        "punishmentMultiplier", houseData.getPunishmentMultiplier())
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Vote successfully added to vote: " + hInfo.id);

                    //hInfo updated successfully, return updated hInfo
                    callback.onReturn(hInfo, true);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating document", e);

                    //IndicateError
                    callback.onReturn(null, false);
                });
        }
    }
}
