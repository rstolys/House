package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.userInfo;
import com.cmpt275.house.classDef.houseInfo;
import com.cmpt275.house.classDef.votingInfo;

public interface house extends task {
    public void createHouse(houseInfo hInfo);
    public void joinHouse(int house_id, userInfo uInfo);
    public void viewYourHouses(userInfo uInfo);
    public void viewHouse(int house_id);
    public void approveMember(int house_id, int user_id);
    public void addMember(String userEmail);
    public void viewMember(int user_id);
    public void removeMember(int user_id);
    public void makeMemberAdmin(userInfo uInfo);
    public void viewVoting(int voting_id);
    public void submitVote(int voting_id, int voteType, userInfo uInfo);
    public void viewSettings(int house_id);
    public void editSettings(houseInfo hInfo);
    public void deleteHouse(houseInfo hInfo);
}

