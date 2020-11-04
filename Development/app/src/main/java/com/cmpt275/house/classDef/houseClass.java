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
    public boolean createHouse(houseInfo hInfo) {
        return false;
    }

    public boolean joinHouse(int house_id, userInfo uInfo) {
        return false;
    }

    public houseInfo[] viewYourHouses(userInfo uInfo) {
        return new houseInfo[0];
    }

    public houseInfo viewHouse(int house_id) {
        return null;
    }

    public houseInfo approveMember(int house_id, int user_id) {
        return null;
    }

    public houseInfo addMember(String userEmail) {
        return null;
    }

    public userInfo viewMember(int user_id) {
        return null;
    }

    public boolean deleteMember(int user_id) {
        return false;
    }

    public houseInfo makeMemberAdmin(userInfo uInfo) {
        return null;
    }

    public votingInfo viewVoting(int voting_id) {
        return null;
    }

    public votingInfo submitVote(int voting_id, int voteType, userInfo uInfo) {
        return null;
    }

    public houseInfo viewSettings(int house_id) {
        return null;
    }

    public houseInfo editSettings(houseInfo hInfo) {
        return null;
    }
}
