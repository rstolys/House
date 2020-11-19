package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.hInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.hInfoCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoArrayCallback;
import com.cmpt275.house.interfaceDef.Callbacks.vInfoCallback;

//TODO: Update documentation to match new interface
public interface HouseBE {
    public void getCurrentHouses(userInfo uInfo, hInfoArrayCallback callback);
    public void getHouseInfo(String house_id, hInfoCallback callback);
    public void createNewHouse(houseInfo hInfo, hInfoCallback callback);
    public void deleteHouse(houseInfo hInfo, String callerID, booleanCallback callback);
    public void setUserRole(houseInfo hInfo, String user_id, String role, hInfoCallback callback);
    public void addMember(houseInfo hInfo, String user_id, String role, String userName, hInfoCallback callback);
    public void removeMember(houseInfo hInfo, String callerID, String memberID, booleanCallback callback);
    public void getHouseVotes(String house_id, vInfoArrayCallback callback);
    public void submitVote(votingInfo vInfo, String userName, String user_id, boolean yesVote, vInfoCallback callback);
    public void editSettings(houseInfo hInfo, boolean displayNameChanges, hInfoCallback callback);
}
