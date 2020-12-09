package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.updateCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoCallback;

public interface house {
    void createHouse(houseInfo hInfo);
    void joinHouse(houseInfo hInfo, userInfo uInfo);
    void viewYourHouses(userInfo uInfo);
    void viewHouse(String house_id);
    void addMember(userInfo uInfo, houseInfo hInfo, String role);
    void inviteMember(houseInfo hInfo, String newMemberEmail, String adminID, updateCallback callback);
    void removeMember(houseInfo hInfo, String removedMemberID, String authorizerID);
    void setMemberRole(String user_id, houseInfo hInfo, String role);
    void getVotes(String house_id);
    void submitVote(votingInfo vInfo, userInfo uInfo, boolean yesVote, int voteIndex, vInfoCallback callback);
    void editSettings(houseInfo hInfo, boolean displayNameChanged);
    void deleteHouse(houseInfo hInfo, String uInfoID);
    void disputeTask(taskInfo tInfo);
    void requestExtension(taskInfo tInfo);
}

