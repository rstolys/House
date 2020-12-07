package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.hInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.hInfoCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoCallback;

public interface HouseBE {
    void getCurrentHouses(userInfo uInfo, hInfoArrayCallback callback);
    void getAllHouses(hInfoArrayCallback callback);
    void getHouseInfo(String house_id, hInfoCallback callback);
    void createNewHouse(houseInfo hInfo, hInfoCallback callback);
    void deleteHouse(houseInfo hInfo, String callerID, booleanCallback callback);
    void setUserRole(houseInfo hInfo, String user_id, String role, hInfoCallback callback);
    void addMember(houseInfo hInfo, String user_id, String role, String userName, hInfoCallback callback);
    void removeMember(houseInfo hInfo, String callerID, String memberID, booleanCallback callback);
    void getHouseVotes(String house_id, vInfoArrayCallback callback);
    void submitVote(votingInfo vInfo, String userName, String user_id, boolean yesVote, vInfoCallback callback);
    void editSettings(houseInfo hInfo, boolean displayNameChanges, hInfoCallback callback);
    void inviteUserToHouse(String newUserEmail, houseInfo hInfo, String callerID, hInfoCallback callback);
}
