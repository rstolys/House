package com.cmpt275.house.classDef;

public class houseFirebaseClass {
    //
    // Class Variables
    //
    //NONE

    //
    // Class Functions
    //
    public int[] getCurrentHouses(userInfo uInfo) {return null;}
    public houseInfo getHouseInfo(int house_id) {return null;}
    public houseInfo createNewHouse(houseInfo hInfo) {return null;}
    public boolean deleteHouse(houseInfo hInfo) {return false;}
    public userInfo getUserInfoInHouse(int user_id) {return null;}
    public userInfo setUserRole(userInfo uInfo, int house_id) {return null;}
    public userInfo addMember(userInfo uInfo, int house_id) {return null;}
    public boolean deleteMember(userInfo uInfo, int house_id) {return false;}
    public userInfo makeMemberAdmin(userInfo uInfo) {return null;}
    public votingInfo getHouseVotes(int[] house_ids) {return null;}
    public votingInfo submitVote(votingInfo vInfo, int voteValue) {return null;}
    public houseInfo editSettings(houseInfo hInfo) {return null;}
}
