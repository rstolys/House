package com.cmpt275.house.classDef;

import android.app.role.RoleManager;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.cmpt275.house.HouseActivity;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.classDef.mappingClass.notificationMapping;
import com.cmpt275.house.classDef.mappingClass.roleMapping;
import com.cmpt275.house.classDef.mappingClass.voteTypeMapping;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.updateCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoCallback;
import com.cmpt275.house.interfaceDef.house;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Set;

public class houseClass extends Observable implements house {

    //
    // Class Variables
    //
    public ArrayList<houseInfo> hInfos; // Store data for hInfos pertaining to specific uInfo
    public ArrayList<houseInfo> hInfosAll; // Store data for all hInfos in db
    public houseInfo hInfo; // Store data to specific hInfo needed at that time
    private userInfo uInfo;
    public votingInfo[] vInfos;

    // Implement observer list
    private List<HouseActivity> hActivityObs = new ArrayList<>();

    //TODO: Add member attributes to documentation
    private final houseFirebaseClass firebaseTask;
    private final userFirebaseClass firebaseUserTask;
    private final taskFirebaseClass taskFirebaseTask;
    private final Context mContext;
    private final roleMapping roleMap;
    private final notificationMapping notificationMap;
    private final voteTypeMapping voteMap;
    private final displayMessage display;
    public ArrayList<taskInfo> tInfos;

    //
    // Class Functions
    //
    ////////////////////////////////////////////////////////////
    //
    // Constructor
    //
    ////////////////////////////////////////////////////////////
    public houseClass(Context mContext) {
        firebaseTask = new houseFirebaseClass();
        this.mContext = mContext;
        roleMap = new roleMapping();
        notificationMap = new notificationMapping();
        voteMap = new voteTypeMapping();
        display = new displayMessage();
        firebaseUserTask = new userFirebaseClass();
        taskFirebaseTask = new taskFirebaseClass();
    }


    ////////////////////////////////////////////////////////////
    //
    // Create a house based on user inputs
    //
    ////////////////////////////////////////////////////////////
    public void createHouse(houseInfo hInfo) {

        hInfo.id = null;
        hInfo.voting_ids = null;
        hInfo.tasks = null;
        hInfo.maxMembers = 4;

        firebaseTask.createNewHouse(hInfo, (hInfoReturned, success, errorMessage) -> {
            Log.d("createNewHouse:", "Returned with success: " + success);
            // If successful, the new house is created and displayed to screen with the rest of the houes
            if( success ){
                this.hInfos.add(hInfoReturned);
                String updateInfo = "createHouses";
                setChanged();
                notifyObservers(updateInfo);
            } else {
                display.showToastMessage(mContext, errorMessage, display.LONG);
            }
        });
    }

    public void joinHouse(houseInfo hInfo, userInfo uInfo) {
        this.addMember(uInfo, hInfo, roleMap.REQUEST);
    }

    public void viewYourHouses(userInfo uInfo) {
        Log.d("viewCurrentHouses:", "In viewYourHouses");

        firebaseTask.getCurrentHouses(uInfo, (hInfos, success, errorMessage) -> {
            Log.d("getCurrentHouses:", "Returned with success: " + success);

            if(success) {
                // Convert hInfos list into an hInfo array
                ArrayList<houseInfo> houseInfoList = new ArrayList<>();
                Collections.addAll(houseInfoList, hInfos);

                this.hInfos = houseInfoList;

                // Update observers
                String updateInfo = "viewHouses";
                setChanged();
                notifyObservers(updateInfo);
            } else {
                display.showToastMessage(mContext, errorMessage, display.LONG);
            }
        });
    }

    public void viewHouse(String house_id) {

        firebaseTask.getHouseInfo(house_id, (hInfo, success, errorMessage) -> {
            Log.d("getHouseInfo:", "Returned with success: " + success);

            if(success) {
                // Notify HouseActivity that viewHouse all requested on this hInfo
                this.hInfo = hInfo;

                String updateInfo = "viewHouse";
                setChanged();
                notifyObservers(updateInfo);
            } else {
                display.showToastMessage(mContext, errorMessage, display.LONG);
            }
        });
    }

    public void viewAllHouses(){
        firebaseTask.getAllHouses( (hInfos, success, errorMessage) -> {
            if(success){
                // Convert hInfos list into an hInfo array
                ArrayList<houseInfo> houseInfoList = new ArrayList<>();
                Collections.addAll(houseInfoList, hInfos);

                this.hInfosAll = houseInfoList;

                String updateInfo = "viewAllHouses";
                setChanged();
                notifyObservers(updateInfo);
            } else{
                display.showToastMessage(mContext, errorMessage, display.LONG);
            }
        });
    }

    public void addMember(userInfo uInfo, houseInfo hInfo, String role) {
        firebaseTask.addMember(hInfo, uInfo.id, role, uInfo.displayName, (hInfoReturned, success, errorMessage) -> {
            Log.d("addMember:", "Returned with success: " + success);
            if(success) {
                if (role.equals(roleMap.REQUEST)) {
                    String updateInfo = "joinHouseRequest";
                    setChanged();
                    notifyObservers(updateInfo);
                }
            } else {
                display.showToastMessage(mContext, errorMessage, display.LONG);
            }
        });
    }

    public void removeMember(houseInfo hInfo, String removedMemberID, String authorizorID) {

        if(hInfo.members.size() == 1){
            this.deleteHouse(hInfo, authorizorID);
        } else {
            firebaseTask.removeMember(hInfo, authorizorID, removedMemberID, ( success, errorMessage) ->{
                Log.d("removeMember:", "Returned with success: " + success);

                if(success){
                    String updateInfo = "removeMember";
                    setChanged();
                    notifyObservers(updateInfo);
                    display.showToastMessage(mContext, "You have moved out!", display.LONG);
                } else {
                    display.showToastMessage(mContext, errorMessage, display.LONG);
                }
            });
        }
    }

    public void setMemberRole(String user_id, houseInfo hInfo, String role) {
        //Set user_id in hInfo to the given role
        firebaseTask.setUserRole(hInfo, user_id, role, (hInfoReturned, success, errorMessage) -> {
            Log.d("setUserRole:", "Returned with success: " + success);
            if(success) {
                this.hInfo = hInfoReturned;

                String updateInfo = "setMemberRole";
                setChanged();
                notifyObservers(updateInfo);
            } else {
                display.showToastMessage(mContext, errorMessage, display.LONG);
            }
        });
    }

    public void getVotes(String house_id) {

        firebaseTask.getHouseVotes(house_id, (vInfos, success, errorMessage) -> {
            Log.d("getHouseVotes:", "Returned with success: " + success);

            this.vInfos = vInfos;
            String updateInfo = "viewVoting";
            setChanged();
            notifyObservers(updateInfo);
        });
    }

    public void getTasks(houseInfo hInfo){
        taskFirebaseTask.getCurrentTasks(hInfo, (tInfos, success, errorMessage)->{

            if(success){
                // Convert list to array
                ArrayList<taskInfo> taskInfoList = new ArrayList<>();
                Collections.addAll(taskInfoList, tInfos);

                // Store array so that observers can see it
                this.tInfos = taskInfoList;

                String updateInfo = "getTasks";
                setChanged();
                notifyObservers(updateInfo);
            }
        });
    }

    ////////////////////////////////////////////////////////////
    //
    // Will submit vote to backend
    //
    ////////////////////////////////////////////////////////////
    public void submitVote(votingInfo vInfo, userInfo uInfo, boolean yesVote, int voteIndex, vInfoCallback callback) {

        if(vInfo == null || uInfo == null) {
            Log.d("submitVote:", "null votingInfo or userInfo ");

            display.showToastMessage(mContext, "Oops, Looks like something went wrong there sorry!", display.LONG);
        }
        else {
            firebaseTask.submitVote(vInfo, uInfo.displayName, uInfo.id, yesVote, (vInfoRet, success, errorMessage) -> {
                Log.d("submitVote:", "Returned with success: " + success);

                if(success) {
                    display.showToastMessage(mContext, "Vote Successfully Submitted", display.LONG);

                    //Update vInfo array -- if it is a valid index
                    try {
                        if(voteIndex != -1)
                            vInfos[voteIndex] = vInfoRet;
                    }
                    catch (Exception e) {
                        //Likely an array out of index. Don't want to crash on this. Just let array get out of sync
                        Log.e("submitVote", "Error adding vote to voting array", e);
                    }

                    //Return result to fragment for updating UI
                    callback.onReturn(vInfoRet, true, "");
                }
                else {
                    Log.d("submitVote:", "Error occured. Message: " + errorMessage);
                    display.showToastMessage(mContext, "Error Submitting vote. Please Try Again", display.LONG);

                    callback.onReturn(null, false, "");
                }
            });
        }
    }

    public void editSettings(houseInfo hInfo) {
        // Update the settings information for this house

        firebaseTask.editSettings(hInfo, !hInfo.displayName.equals(this.hInfo.displayName), (hInfo1, success, errorMessage) -> {
            Log.d("editSettings:", "Returned with success: " + success);

            if(success){
                display.showToastMessage(mContext, "Settings Successfully Updated", display.LONG);

                // Notify observers
                String updateInfo = "editSettings";
                setChanged();
                notifyObservers(updateInfo);
            } else{
                display.showToastMessage(mContext, errorMessage, display.LONG);
            }

        });
    }

    public void deleteHouse(houseInfo hInfo, String uInfoID) {
        firebaseTask.deleteHouse(hInfo, uInfoID, (success, errorMessage)->{
            Log.d("deleteHouse:", "Returned with success: " + success);

            if (success){
                display.showToastMessage(mContext, "House Successfully Deleted", display.LONG);

                String updateInfo = "deleteHouse";
                setChanged();
                notifyObservers(updateInfo);
            } else {
                display.showToastMessage(mContext, errorMessage, display.LONG);
            }
        });
    }

    public void disputeTask(taskInfo tInfo) {
        taskFirebaseTask.disputeTask(tInfo, (tInfoRet, success, errorMessage) -> {
            Log.d("disputeTask:", "Returned with success " + success);
            if(success){
                String updateInfo = "disputeTask";
                setChanged();
                notifyObservers(updateInfo);
            } else {
                display.showToastMessage(mContext, errorMessage, Toast.LENGTH_LONG);;
            }
        });
    }

    public void requestExtension(taskInfo tInfo) {
        taskFirebaseTask.requestExtension(tInfo, (tInfoRet, success, errorMessage) -> {
            Log.d("requestExtension:", "Returned with success " + success);
            if(success){
                String updateInfo = "requestExtension";
                setChanged();
                notifyObservers(updateInfo);
            } else {
                display.showToastMessage(mContext, errorMessage, Toast.LENGTH_LONG);;
            }
        });
    }

    public void updateUserInfo(String user_id, updateCallback callback) {

        if (!user_id.equals(null)) {
            //Get new userInfo to update
            firebaseUserTask.getUserInfo(user_id, (uInfo, success, errorMessage) -> {
                Log.d("getUserInfo", "Returned with success: " + success);

                if(success) {
                    //Update the userInfo
                    this.uInfo = uInfo;

                    callback.onReturn(true);
                }
                else {
                    display.showToastMessage(mContext, "Could not load task and house info. Try loading page again", display.LONG);
                    callback.onReturn(false);
                }
            });

        } else {
            Log.d("updateUserInfo", "user_id provided was null");
        }
    }
}
