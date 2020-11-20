package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.classDef.databaseObjects.houseMemberObj;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.classDef.mappingClass.notificationMapping;
import com.cmpt275.house.classDef.mappingClass.roleMapping;
import com.cmpt275.house.classDef.mappingClass.voteTypeMapping;
import com.cmpt275.house.interfaceDef.Callbacks.hInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.hInfoCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoCallback;
import com.cmpt275.house.interfaceDef.house;

public class houseClass extends taskClass implements house {

    //
    // Class Variables
    //
    private houseInfo[] hInfos;
    private userInfo uInfo;
    private votingInfo[] vInfos;

    //TODO: Add member attributes to documentation
    private final houseFirebaseClass firebaseTask;

    private final roleMapping roleMap;
    private final notificationMapping notificationMap;
    private final voteTypeMapping voteMap;


    //
    // Class Functions
    //
    ////////////////////////////////////////////////////////////
    //
    // Constructor
    //
    ////////////////////////////////////////////////////////////
    public houseClass() {
        firebaseTask = new houseFirebaseClass();
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
        //myHInfo.displayName = "Cowichan  09";
        myHInfo.displayName = hInfo.displayName;
        myHInfo.voting_ids = null;
        myHInfo.tasks = null;

        //myHInfo.members.put("w4OFKQrvL28T3WlXVP4X", new houseMemberObj("Ryan Stolys", true, roleMap.mapStringToInt("Administrator")));
        myHInfo.members = hInfo.members;
        //**when creating a house the first member must be role "2" meaning admin --> Taken care of in NewHouseFrag class

        //myHInfo.description = "This my SFU townhouse that contains 4 people. We are all on the golf team";
        myHInfo.description = hInfo.description;
        // myHInfo.punishmentMultiplier = 2;
        myHInfo.punishmentMultiplier = hInfo.punishmentMultiplier;
        myHInfo.maxMembers = 4;
        // myHInfo.houseNotifications = notificationMap.WEEKLY;
        myHInfo.houseNotifications = hInfo.houseNotifications;

        //TODO Somehow tell that the backend added the house successfully.
        firebaseTask.createNewHouse(myHInfo, (hInfo1, success) -> {
            Log.d("createNewHouse:", "Returned with success: " + success);
            //Do stuff here ...
        });
    }

    public void joinHouse(String house_id, userInfo uInfo) {}

    public void viewYourHouses(userInfo uInfo) {

        userInfo myUInfo = new userInfo();

        myUInfo.id = "w4OFKQrvL28T3WlXVP4X";    //Ryan Stolys user_id

        firebaseTask.getCurrentHouses(myUInfo, (hInfos, success) -> {
            Log.d("getCurrentHouses:", "Returned with success: " + success);
            //Do stuff here ...
        });
    }

    public void viewHouse(String house_id) {

        house_id = "TfB0rlNBEuj9dSMzA1OM";        //Cowichan 09 house_id

        firebaseTask.getHouseInfo(house_id, (hInfo, success) -> {
            Log.d("getHouseInfo:", "Returned with success: " + success);
            //Do stuff here ...
        });
    }

    public void approveMember(String house_id, String user_id) {}

    public void addMember(String userEmail) {

        houseInfo myHInfo = new houseInfo();

        myHInfo.id = "TfB0rlNBEuj9dSMzA1OM";    //Ryan Stolys user_id
        myHInfo.members.put("w4OFKQrvL28T3WlXVP4X", new houseMemberObj("Ryan Stolys", true, roleMap.mapStringToInt("Administrator")));

        firebaseTask.addMember(myHInfo, "TestAddMember", roleMap.MEMBER, "Jayden Cole", (hInfo, success) -> {
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
        myHInfo.members.put("w4OFKQrvL28T3WlXVP4X", new houseMemberObj("Ryan Stolys", true, roleMap.mapStringToInt("Administrator")));


        //Set Ryan Stolys to a regular house member
        firebaseTask.setUserRole(myHInfo, "w4OFKQrvL28T3WlXVP4X", roleMap.mapIntToString(1), (hInfo, success) -> {
            Log.d("setUserRole:", "Returned with success: " + success);
            //Do Stuff here ...
        });
    }

    public void viewVoting(String voting_id) {

        firebaseTask.getHouseVotes("TfB0rlNBEuj9dSMzA1OM", (vInfos, success) -> {
            Log.d("getHouseVotes:", "Returned with success: " + success);
            //Do stuff here ...
        });
    }

    public void submitVote(String voting_id, int voteType, userInfo uInfo) {

        votingInfo myVInfo = new votingInfo();

        myVInfo.id = "gviuevFrurw2DsVdkGuD";
        myVInfo.type = voteMap.DISPUTE_COMPLETION;

        firebaseTask.submitVote(myVInfo, "Ryan Stolys", "w4OFKQrvL28T3WlXVP4X", true, (vInfo, success) -> {
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

        myHInfo.members.put("w4OFKQrvL28T3WlXVP4X", new houseMemberObj("Ryan Stolys", true, roleMap.mapStringToInt("Administrator")));
        //**when creating a house the first member must be role "2" meaning admin

        myHInfo.description = "This the SFU Golf townhouse. It contains 4 people. We are all on the golf team";
        //myHInfo.description = hInfo.description;
        myHInfo.punishmentMultiplier = 3;
        myHInfo.maxMembers = 5;
        myHInfo.houseNotifications = notificationMap.WEEKLY;

        firebaseTask.editSettings(myHInfo, true, (hInfo1, success) -> {
            Log.d("editSettings:", "Returned with success: " + success);
            //Do stuff here ...
        });
    }

    public void deleteHouse(houseInfo hInfo) {}
}
