package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.houseInfo;

public interface house extends task {
    public void createHouse(houseInfo hInfo);
    public void joinHouse(houseInfo hInfo, userInfo uInfo);
    public void viewYourHouses(userInfo uInfo);
    public void viewHouse(String house_id);
    public void approveMember(String house_id, String user_id);
    public void addMember(userInfo uInfo, houseInfo hInfo, String role);
    public void viewMember(String user_id);
    public void removeMember(String user_id);
    public void makeMemberAdmin(userInfo uInfo);
    public void viewVoting(String voting_id);
    public void submitVote(String voting_id, int voteType, userInfo uInfo);
    public void viewSettings(String house_id);
    public void editSettings(houseInfo hInfo);
    public void deleteHouse(houseInfo hInfo);
}

