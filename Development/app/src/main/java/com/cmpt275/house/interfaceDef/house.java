package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.userInfo;
import com.cmpt275.house.classDef.houseInfo;
import com.cmpt275.house.classDef.votingInfo;

public interface house extends task {
    public boolean createHouse(houseInfo hInfo);
    public boolean joinHouse(int house_id, userInfo uInfo);
    public houseInfo[] viewYourHouses(userInfo uInfo);
    public houseInfo viewHouse(int house_id);
    public houseInfo approveMember(int house_id, int user_id);
    public houseInfo addMember(String userEmail);
    public userInfo viewMember(int user_id);
    public boolean deleteMember(int user_id);
    public houseInfo makeMemberAdmin(userInfo uInfo);
    public votingInfo viewVoting(int voting_id);
    public votingInfo submitVote(int voting_id, int voteType, userInfo uInfo);
    public houseInfo viewSettings(int house_id);
    public houseInfo editSettings(houseInfo hInfo);
}

