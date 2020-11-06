package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.house;

class houseClass extends taskClass implements house{

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

    public void joinHouse(int house_id, userInfo uInfo) {
        return;
    }

    public void viewYourHouses(userInfo uInfo) {
        return;
    }

    public void viewHouse(int house_id) {
        return;
    }

    public void approveMember(int house_id, int user_id) {
        return;
    }

    public void addMember(String userEmail) {
        return;
    }

    public void viewMember(int user_id) {
        return;
    }

    public void removeMember(int user_id) {
        return;
    }

    public void makeMemberAdmin(userInfo uInfo) {
        return;
    }

    public void viewVoting(int voting_id) {
        return;
    }

    public void submitVote(int voting_id, int voteType, userInfo uInfo) {
        return;
    }

    public void viewSettings(int house_id) {
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
