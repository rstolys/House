package com.cmpt275.house.classDef;

import android.content.Context;
import android.util.Log;

import com.cmpt275.house.HouseActivity;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.classDef.mappingClass.notificationMapping;
import com.cmpt275.house.classDef.mappingClass.roleMapping;
import com.cmpt275.house.classDef.mappingClass.voteTypeMapping;
import com.cmpt275.house.interfaceDef.house;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class houseClass extends taskClass implements house {

    //
    // Class Variables
    //
    public ArrayList<houseInfo> hInfos;
    //public houseInfo[] hInfos;
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

    //
    // Observable pattern update hInfo
    //
    public void sethInfos(ArrayList<houseInfo> hInfos){
        // For every observer in observer list, notify them
        this.hInfos = hInfos;
        String updateInfo = "";
        setChanged();
        notifyObservers(updateInfo);
    }

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
                sethInfos(this.hInfos);
            } // Else we should display error message
        });
    }

    public void joinHouse(String house_id, userInfo uInfo) {}

    public void viewYourHouses(userInfo uInfo) {
        Log.d("viewCurrentHouses:", "In viewYourHouses");

        userInfo myUInfo = uInfo;

        firebaseTask.getCurrentHouses(myUInfo, (hInfos, success, errorMessage) -> {
            Log.d("getCurrentHouses:", "Returned with success: " + success);
            ArrayList<houseInfo> houseInfoList = new ArrayList<houseInfo>();
            Collections.addAll(houseInfoList, hInfos);

            this.sethInfos( houseInfoList );
            Log.d("viewYourHouses", "Done getting houses set up");
        });
    }

    public void viewHouse(String house_id) {

        firebaseTask.getHouseInfo(house_id, (hInfo, success, errorMessage) -> {
            Log.d("getHouseInfo:", "Returned with success: " + success);
            // Notify HouseActivity that viewHouse all requested on this hInfo
            setChanged();
            notifyObservers(hInfo);
        });
    }

    public void approveMember(String house_id, String user_id) {}

    public void addMember(String userEmail) {

        houseInfo myHInfo = new houseInfo();

        myHInfo.id = "TfB0rlNBEuj9dSMzA1OM";    //Ryan Stolys user_id
        myHInfo.members.put("w4OFKQrvL28T3WlXVP4X", new houseMemberInfoObj("Ryan Stolys", roleMap.ADMIN));

        firebaseTask.addMember(myHInfo, "TestAddMember", roleMap.MEMBER, "Jayden Cole", (hInfo, success, errorMessage) -> {
            Log.d("addMember:", "Returned with success: " + success);
            //Do stuff here ...
        });
    }

    public void viewMember(String user_id) {}

    public void removeMember(String user_id) {}

    public void makeMemberAdmin(userInfo uInfo) {

        houseInfo myHInfo = new houseInfo();

        myHInfo.id = "TfB0rlNBEuj9dSMzA1OM";    //Ryan Stolys user_id
        //myHInfo.members.put("DummyUser", new houseMemberObj("Jayden Cole", true, roleMap.mapStringToInt("Administrator")));
        myHInfo.members.put("w4OFKQrvL28T3WlXVP4X", new houseMemberInfoObj("Ryan Stolys", roleMap.ADMIN));


        //Set Ryan Stolys to a regular house member
        firebaseTask.setUserRole(myHInfo, "w4OFKQrvL28T3WlXVP4X", roleMap.mapIntToString(1), (hInfo, success, errorMessage) -> {
            Log.d("setUserRole:", "Returned with success: " + success);
            //Do Stuff here ...
        });
    }

    public void viewVoting(String voting_id) {

        firebaseTask.getHouseVotes("TfB0rlNBEuj9dSMzA1OM", (vInfos, success, errorMessage) -> {
            Log.d("getHouseVotes:", "Returned with success: " + success);
            //Do stuff here ...
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

        houseInfo myHInfo = new houseInfo();

        myHInfo.id = "TfB0rlNBEuj9dSMzA1OM";
        myHInfo.displayName = "Cowichan  9";
        //myHInfo.displayName = hInfo.displayName;
        myHInfo.voting_ids = null;
        myHInfo.tasks = null;

        myHInfo.members.put("w4OFKQrvL28T3WlXVP4X", new houseMemberInfoObj("Ryan Stolys", roleMap.ADMIN));
        //**when creating a house the first member must be role "2" meaning admin

        myHInfo.description = "This the SFU Golf townhouse. It contains 4 people. We are all on the golf team";
        //myHInfo.description = hInfo.description;
        myHInfo.punishmentMultiplier = 3;
        myHInfo.maxMembers = 5;
        myHInfo.houseNotifications = notificationMap.WEEKLY;

        firebaseTask.editSettings(myHInfo, true, (hInfo1, success, errorMessage) -> {
            Log.d("editSettings:", "Returned with success: " + success);
            //Do stuff here ...
        });
    }

    public void deleteHouse(houseInfo hInfo) {}
}
