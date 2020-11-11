package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.house;

public class houseClass extends taskClass implements house{

    //
    // Class Variables
    //
    private houseInfo[] hInfos;
    private userInfo uInfo;
    private votingInfo [] vInfos;


    //
    // Class Functions
    //
    public void createHouse(houseInfo hInfo) {
        return;
    }

    public void joinHouse(String house_id, userInfo uInfo) {
        return;
    }

    public void viewYourHouses(userInfo uInfo) {
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
