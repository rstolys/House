package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.HouseBE;

import com.cmpt275.house.interfaceDef.houseCallbacks;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class houseFirebaseClass implements HouseBE {
    //
    // Class Variables
    //
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private houseCallbacks hCallback;
    private final String TAG = "FirebaseHouseAction";

    //
    // Class Functions
    //
    public void getCurrentHouses(userInfo uInfo) {return;}
    public void getHouseInfo(String house_id) {return;}
    public void createNewHouse(houseInfo hInfo) {return;}
    public void deleteHouse(houseInfo hInfo) {return;}
    public void getUserInfoInHouse(String user_id) {return;}
    public void setUserRole(userInfo uInfo, String house_id) {return;}
    public void addMember(userInfo uInfo, String house_id) {return;}
    public void deleteMember(userInfo uInfo, String house_id) {return;}
    public void makeMemberAdmin(userInfo uInfo) {return;}
    public void getHouseVotes(List<String> house_ids) {return;}
    public void submitVote(votingInfo vInfo, String voteValue) {return;}
    public void editSettings(houseInfo hInfo) {return;}
}
