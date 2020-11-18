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
    private houseFirebaseClass firebaseTask;

    private roleMapping roleMap;
    private notificationMapping notificationMap;
    private voteTypeMapping voteMap;


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
        myHInfo.displayName = "Cowichan  09";
        //myHInfo.displayName = hInfo.displayName;
        myHInfo.voting_ids = null;
        myHInfo.tasks = null;

        myHInfo.members.put("w4OFKQrvL28T3WlXVP4X", new houseMemberObj("Ryan Stolys", true, roleMap.mapStringToInt("Administrator")));
        //**when creating a house the first member must be role "2" meaning admin

        myHInfo.description = "This my SFU townhouse that contains 4 people. We are all on the golf team";
        //myHInfo.description = hInfo.description;
        myHInfo.punishmentMultiplier = 2;
        myHInfo.maxMembers = 4;
        myHInfo.houseNotifications = notificationMap.WEEKLY;


        firebaseTask.createNewHouse(myHInfo, new hInfoCallback() {
            @Override
            public void onReturn(houseInfo hInfo, boolean success) {
                Log.d("createNewHouse:", "Returned with success: " + success);
                //Do stuff here ...
            }
        });

        return;
    }

    public void joinHouse(String house_id, userInfo uInfo) {
        return;
    }

    public void viewYourHouses(userInfo uInfo) {

        userInfo myUInfo = new userInfo();

        myUInfo.id = "w4OFKQrvL28T3WlXVP4X";    //Ryan Stolys user_id

        firebaseTask.getCurrentHouses(myUInfo, new hInfoArrayCallback() {
            @Override
            public void onReturn(houseInfo[] hInfos, boolean success) {
                Log.d("getCurrentHouses:", "Returned with success: " + success);
                //Do stuff here ...
            }
        });

        return;
    }

    public void viewHouse(String house_id) {

        house_id = "TfB0rlNBEuj9dSMzA1OM";        //Cowichan 09 house_id

        firebaseTask.getHouseInfo(house_id, new hInfoCallback() {
            @Override
            public void onReturn(houseInfo hInfo, boolean success) {
                Log.d("getHouseInfo:", "Returned with success: " + success);
                //Do stuff here ...
            }
        });

        return;
    }

    public void approveMember(String house_id, String user_id) {
        return;
    }

    public void addMember(String userEmail) {

        houseInfo myHInfo = new houseInfo();

        myHInfo.id = "TfB0rlNBEuj9dSMzA1OM";    //Ryan Stolys user_id
        myHInfo.members.put("w4OFKQrvL28T3WlXVP4X", new houseMemberObj("Ryan Stolys", true, roleMap.mapStringToInt("Administrator")));

        firebaseTask.addMember(myHInfo, "TestAddMember", roleMap.MEMBER, "Jayden Cole", new hInfoCallback() {
            @Override
            public void onReturn(houseInfo hInfo, boolean success) {
                Log.d("addMember:", "Returned with success: " + success);
                //Do stuff here ...
            }
        });

        return;
    }

    public void viewMember(String user_id) {
        return;
    }

    public void removeMember(String user_id) {
        return;
    }

    public void makeMemberAdmin(userInfo uInfo) {

        houseInfo myHInfo = new houseInfo();

        myHInfo.id = "TfB0rlNBEuj9dSMzA1OM";    //Ryan Stolys user_id
        //myHInfo.members.put("DummyUser", new houseMemberObj("Jayden Cole", true, roleMap.mapStringToInt("Administrator")));
        myHInfo.members.put("w4OFKQrvL28T3WlXVP4X", new houseMemberObj("Ryan Stolys", true, roleMap.mapStringToInt("Administrator")));

        //Set Ryan Stolys to a regular house member
        firebaseTask.setUserRole(myHInfo, "w4OFKQrvL28T3WlXVP4X", roleMap.mapIntToString(1), new hInfoCallback() {
            @Override
            public void onReturn(houseInfo hInfo, boolean success) {
                Log.d("setUserRole:", "Returned with success: " + success);
                //Do Stuff here ...
            }
        });

        return;
    }

    public void viewVoting(String voting_id) {

        firebaseTask.getHouseVotes("TfB0rlNBEuj9dSMzA1OM", new vInfoArrayCallback() {
            @Override
            public void onReturn(votingInfo[] vInfos, boolean success) {
                Log.d("getHouseVotes:", "Returned with success: " + success);
                //Do stuff here ...
            }
        });

        return;
    }

    public void submitVote(String voting_id, int voteType, userInfo uInfo) {

        votingInfo myVInfo = new votingInfo();

        myVInfo.id = "gviuevFrurw2DsVdkGuD";
        myVInfo.type = voteMap.DISPUTE_COMPLETION;

        firebaseTask.submitVote(myVInfo, "Ryan Stolys", "w4OFKQrvL28T3WlXVP4X", true, new vInfoCallback() {
            @Override
            public void onReturn(votingInfo vInfo, boolean success) {
                Log.d("submitVote:", "Returned with success: " + success);
                //Do stuff  here ...
            }
        });

        return;
    }

    public void viewSettings(String house_id) {
        return;
    }

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

        firebaseTask.editSettings(myHInfo, new hInfoCallback() {
            @Override
            public void onReturn(houseInfo hInfo, boolean success) {
                Log.d("editSettings:", "Returned with success: " + success);
                //Do stuff here ...
            }
        });


        return;
    }

    public void deleteHouse(houseInfo hInfo) {}
}
