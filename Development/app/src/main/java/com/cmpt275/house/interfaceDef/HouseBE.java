package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.houseInfo;
import com.cmpt275.house.classDef.userInfo;
import com.cmpt275.house.classDef.votingInfo;

import java.util.List;

public interface HouseBE {
    public void getCurrentHouses(userInfo uInfo);
    public void getHouseInfo(String house_id);
    public void createNewHouse(houseInfo hInfo);
    public void deleteHouse(houseInfo hInfo);
    public void getUserInfoInHouse(String user_id);
    public void setUserRole(userInfo uInfo, String house_id);
    public void addMember(userInfo uInfo, String house_id);
    public void deleteMember(userInfo uInfo, String house_id);
    public void makeMemberAdmin(userInfo uInfo);
    public void getHouseVotes(List<String> house_ids);
    public void submitVote(votingInfo vInfo, String voteValue);
    public void editSettings(houseInfo hInfo);
}
