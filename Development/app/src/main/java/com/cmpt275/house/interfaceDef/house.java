package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoCallback;

public interface house {
    public void createHouse(houseInfo hInfo);
    public void joinHouse(houseInfo hInfo, userInfo uInfo);
    public void viewYourHouses(userInfo uInfo);
    public void viewHouse(String house_id);
    public void addMember(userInfo uInfo, houseInfo hInfo, String role);
    public void removeMember(houseInfo hInfo, String removedMemberID, String authorizorID);
    public void setMemberRole(String user_id, houseInfo hInfo, String role);
    public void getVotes(String house_id);
    public void submitVote(votingInfo vInfo, userInfo uInfo, boolean yesVote, int voteIndex, vInfoCallback callback);
    public void editSettings(houseInfo hInfo);
    public void deleteHouse(houseInfo hInfo, String uInfoID);
    public void disputeTask(taskInfo tInfo);
    public void requestExtension(taskInfo tInfo);
}

