package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.houseInfo;
import com.cmpt275.house.classDef.userInfo;
import com.cmpt275.house.classDef.votingInfo;

public interface HouseBE {
    public int[] getCurrentHouses(userInfo uInfo);
    public houseInfo getHouseInfo(int house_id);
    public houseInfo createNewHouse(houseInfo hInfo);
    public boolean deleteHouse(houseInfo hInfo);
    public userInfo getUserInfoInHouse(int user_id);
    public userInfo setUserRole(userInfo uInfo, int house_id);
    public userInfo addMember(userInfo uInfo, int house_id);
    public boolean deleteMember(userInfo uInfo, int house_id);
    public userInfo makeMemberAdmin(userInfo uInfo);
    public votingInfo getHouseVotes(int[] house_ids);
    public votingInfo submitVote(votingInfo vInfo, int voteValue);
    public houseInfo editSettings(houseInfo hInfo);
}
