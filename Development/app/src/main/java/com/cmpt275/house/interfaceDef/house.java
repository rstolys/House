package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoCallback;

public interface house extends task {
    void createHouse(houseInfo hInfo);
    void joinHouse(houseInfo hInfo, userInfo uInfo);
    void viewYourHouses(userInfo uInfo);
    void viewHouse(String house_id);
    void approveMember(String house_id, String user_id);
    void addMember(userInfo uInfo, houseInfo hInfo, String role);
    void viewMember(String user_id);
    void removeMember(String user_id);
    void makeMemberAdmin(userInfo uInfo);
    void getVotes(String house_id);
    void submitVote(votingInfo vInfo, userInfo uInfo, boolean yesVote, int voteIndex, vInfoCallback callback);
    void viewSettings(String house_id);
    void editSettings(houseInfo hInfo);
    void deleteHouse(houseInfo hInfo);
}

