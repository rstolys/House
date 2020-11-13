package com.cmpt275.house.classDef;

import com.cmpt275.house.classDef.databaseObjects.houseMemberObj;
import com.cmpt275.house.interfaceDef.house;
import com.cmpt275.house.interfaceDef.houseCallbacks;
import com.cmpt275.house.interfaceDef.mapping;

import java.util.HashMap;

public class houseClass extends taskClass implements house, houseCallbacks {

    //
    // Class Variables
    //
    private houseInfo[] hInfos;
    private userInfo uInfo;
    private votingInfo [] vInfos;

    //TODO: Add member attributes to documentation
    private houseFirebaseClass firebaseTask;

    private mapping roleMap;
    private mapping notificationMap;


    //
    // Class Functions
    //
    ////////////////////////////////////////////////////////////
    //
    // Constructor
    //
    ////////////////////////////////////////////////////////////
    public houseClass() {
        firebaseTask = new houseFirebaseClass(this);
        roleMap = new roleMapping();
        notificationMap = new notificationMapping();
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
        myHInfo.voting_ids = null;
        myHInfo.tasks = null;

        myHInfo.members.put("w4OFKQrvL28T3WlXVP4X", new houseMemberObj("Ryan Stolys", true, roleMap.mapStringToInt("Administrator")));
        //**when creating a house the first member must be role "2" meaning admin

        myHInfo.description = "This my SFU townhouse that contains 4 people. We are all on the golf team";
        myHInfo.punishmentMultiplier = 2;
        myHInfo.maxMembers = 4;
        myHInfo.houseNotifications = notificationMap.mapStringToInt("Weekly");


        firebaseTask.createNewHouse(myHInfo);

        return;
    }

    public void joinHouse(String house_id, userInfo uInfo) {
        return;
    }

    public void viewYourHouses(userInfo uInfo) {

        userInfo myUInfo = new userInfo();

        myUInfo.id = "w4OFKQrvL28T3WlXVP4X";

        firebaseTask.getCurrentHouses(myUInfo);

        return;
    }

    public void viewHouse(String house_id) {
        return;
    }

    public void approveMember(String house_id, String user_id) {
        return;
    }

    public void addMember(String userEmail) {
        return;
    }

    public void viewMember(String user_id) {
        return;
    }

    public void removeMember(String user_id) {
        return;
    }

    public void makeMemberAdmin(userInfo uInfo) {
        return;
    }

    public void viewVoting(String voting_id) {
        return;
    }

    public void submitVote(String voting_id, int voteType, userInfo uInfo) {
        return;
    }

    public void viewSettings(String house_id) {
        return;
    }

    public void editSettings(houseInfo hInfo) {
        return;
    }

    public void deleteHouse(houseInfo hInfo) {
        return;
    }


    public void onHouseInfoArrayReturn(houseInfo[] hInfos, String functionName) {return;}
    public void onHouseInfoReturn(houseInfo hInfo, String functionName) {return;}
    public void onHouseBooleanReturn(boolean result, String functionName) {return;}
}
