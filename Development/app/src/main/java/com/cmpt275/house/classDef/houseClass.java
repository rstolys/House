package com.cmpt275.house.classDef;

import android.app.role.RoleManager;
import android.content.Context;
import android.util.Log;
import android.view.Display;

import com.cmpt275.house.HouseActivity;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.classDef.mappingClass.notificationMapping;
import com.cmpt275.house.classDef.mappingClass.roleMapping;
import com.cmpt275.house.classDef.mappingClass.voteTypeMapping;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.house;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class houseClass extends taskClass implements house {

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
    private final Context mContext;
    private final roleMapping roleMap;
    private final notificationMapping notificationMap;
    private final voteTypeMapping voteMap;

    private displayMessage display;

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
    }


    ////////////////////////////////////////////////////////////
    //
    // Create a house based on user inputs
    //
    ////////////////////////////////////////////////////////////
    public void createHouse(houseInfo hInfo) {
        houseInfo myHInfo = new houseInfo();

        myHInfo.id = null;
        myHInfo.displayName = hInfo.displayName;
        myHInfo.voting_ids = null;
        myHInfo.tasks = null;

        myHInfo.members = hInfo.members;

        myHInfo.description = hInfo.description;
        myHInfo.punishmentMultiplier = hInfo.punishmentMultiplier;
        myHInfo.maxMembers = 4;
        myHInfo.houseNotifications = hInfo.houseNotifications;

        firebaseTask.createNewHouse(myHInfo, (hInfo1, success, errorMessage) -> {
            Log.d("createNewHouse:", "Returned with success: " + success);
            // If successful, the new house is created and displayed to screen with the rest of the houes
            if( success ){
                this.hInfos.add(hInfo1);
                String updateInfo = "createHouses";
                setChanged();
                notifyObservers(updateInfo);
            } // Else we should display error message
        });
    }

    public void joinHouse(houseInfo hInfo, userInfo uInfo) {
        this.addMember(uInfo, hInfo, roleMap.REQUEST);
    }

    public void viewYourHouses(userInfo uInfo) {
        Log.d("viewCurrentHouses:", "In viewYourHouses");

        firebaseTask.getCurrentHouses(uInfo, (hInfos, success, errorMessage) -> {
            Log.d("getCurrentHouses:", "Returned with success: " + success);

            // Convert hInfos list into an hInfo array
            ArrayList<houseInfo> houseInfoList = new ArrayList<>();
            Collections.addAll(houseInfoList, hInfos);

            this.hInfos = houseInfoList;

            // Update observers
            String updateInfo = "viewHouses";
            setChanged();
            notifyObservers(updateInfo);
            Log.d("viewYourHouses", "Done getting houses set up");
        });
    }

    public void viewHouse(String house_id) {

        firebaseTask.getHouseInfo(house_id, (hInfo, success, errorMessage) -> {
            Log.d("getHouseInfo:", "Returned with success: " + success);

            // Notify HouseActivity that viewHouse all requested on this hInfo
            this.hInfo = hInfo;

            String updateInfo = "viewHouse";
            setChanged();
            notifyObservers(updateInfo);
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
            }
        });
    }

    public void approveMember(String house_id, String user_id) {}

    public void addMember(userInfo uInfo, houseInfo hInfo, String role) {
        firebaseTask.addMember(hInfo, uInfo.id, role, uInfo.displayName, (hInfoReturned, success, errorMessage) -> {
            Log.d("addMember:", "Returned with success: " + success);
            if(success) {
                if (role.equals(roleMap.REQUEST)) {
                    String updateInfo = "joinHouseRequest";
                    setChanged();
                    notifyObservers(updateInfo);
                }
            }
        });
    }

    public void viewMember(String user_id) {}

    public void removeMember(houseInfo hInfo, String removedMemberID, String authorizorID) {

        firebaseTask.removeMember(hInfo, authorizorID, removedMemberID, ( success, errorMessage) ->{
            // Do stuff here...
            Log.d("removeMember:", "Returned with success: " + success);

            String updateInfo = "removeMember";
            setChanged();
            notifyObservers(updateInfo);
        });
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
            } // else display.log the toast message on failure
        });
    }

    public void viewVoting(String voting_id) {

        firebaseTask.getHouseVotes("TfB0rlNBEuj9dSMzA1OM", (vInfos, success, errorMessage) -> {
            Log.d("getHouseVotes:", "Returned with success: " + success);

            this.vInfos = vInfos;
            String updateInfo = "viewVoting";
            setChanged();
            notifyObservers(updateInfo);
        });
    }

    public void submitVote(String voting_id, int voteType, userInfo uInfo) {

        votingInfo myVInfo = new votingInfo();

        myVInfo.id = "gviuevFrurw2DsVdkGuD";
        myVInfo.type = voteMap.DISPUTE_COMPLETION;

        firebaseTask.submitVote(myVInfo, "Ryan Stolys", "w4OFKQrvL28T3WlXVP4X", true, (vInfo, success, errorMessage) -> {
            Log.d("submitVote:", "Returned with success: " + success);
            //Do stuff  here ...
        });
    }

    public void viewSettings(String house_id) {}

    public void editSettings(houseInfo hInfo) {
        // Update the settings information for this house

        firebaseTask.editSettings(hInfo, !hInfo.displayName.equals(this.hInfo.displayName), (hInfo1, success, errorMessage) -> {
            Log.d("editSettings:", "Returned with success: " + success);

            // Notify observers
            String updateInfo = "editSettings";
            setChanged();
            notifyObservers(updateInfo);
        });
    }

    public void deleteHouse(houseInfo hInfo) {}
}
