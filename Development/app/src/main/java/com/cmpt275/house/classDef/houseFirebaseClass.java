package com.cmpt275.house.classDef;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmpt275.house.interfaceDef.HouseBE;

import com.cmpt275.house.interfaceDef.houseCallbacks;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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

    ////////////////////////////////////////////////////////////
    //
    // Constructor which implements the callback functions
    //
    ////////////////////////////////////////////////////////////
    public houseFirebaseClass(houseCallbacks hCallback) {
        this.hCallback = hCallback;
    }


    ////////////////////////////////////////////////////////////
    //
    // Gets the current houses for the specified user
    //
    ////////////////////////////////////////////////////////////
    public void getCurrentHouses(userInfo uInfo) {

        Log.d(TAG, "getCurrentHouses(user) for user " + uInfo.id);

        //Ensure the uInfo has a valid id
        if(uInfo.id == null) {
            //We cannot access db without valid id for user. Return failure.
            hCallback.onHouseInfoArrayReturn(null, "getCurrentHouses(user)");
        }
        else {
            //Get documents from the collection that have house_id specified
            db.collection("houses").whereEqualTo("members." + uInfo.id + ".exists", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> queryResult) {

                        if(queryResult.isSuccessful()) {
                            Log.d(TAG, "Successfully Query in getCurrentHouses(user)");

                            List<houseInfo> houseInfoList = new ArrayList<houseInfo>();
                            for(QueryDocumentSnapshot document : queryResult.getResult()) {

                                firebaseHouseDocument houseData = document.toObject(firebaseHouseDocument.class);
                                houseInfoList.add(houseData.toHouseInfo(document.getId()));

                                Log.d(TAG, "Collected Task Document: " + document.getId());
                            }

                            //Convert list to array and return
                            houseInfo[] houseInfoArray = new houseInfo[houseInfoList.size()];
                            houseInfoList.toArray(houseInfoArray);
                            hCallback.onHouseInfoArrayReturn(houseInfoArray, "getCurrentHouses(user)");

                        }
                        else {
                            Log.d(TAG, "getCurrentHouses(user): Error getting houses for user: ", queryResult.getException());

                            //Call function to return task value
                            hCallback.onHouseInfoArrayReturn(null, "getCurrentHouses(user)");
                        }
                    }
                });
        }

        return;
    }


    public void getHouseInfo(String house_id) {return;}


    ////////////////////////////////////////////////////////////
    //
    // Creates a new house from the houseInfo class
    //
    ////////////////////////////////////////////////////////////
    public void createNewHouse(houseInfo hInfo) {

        //Make sure there is at least one house member
        if(hInfo.members.isEmpty()) {
            Log.d(TAG, "House Needs at least one member");

            hCallback.onHouseInfoReturn(null, "createNewHouse");
        }
        else {
            //Create custom class to generate a new document
            firebaseHouseDocument newHouse = new firebaseHouseDocument(hInfo);

            //Copy tInfo to be able to return it
            houseInfo finalHInfo = hInfo;

            //Add the new task to the tasks collection
            db.collection("houses").add(newHouse)
                .addOnSuccessListener( documentReference -> {
                    Log.d(TAG, "House added with ID: " + documentReference.getId());

                    //Set the id of the house
                    finalHInfo.id = documentReference.getId();

                    //Return the house info
                    hCallback.onHouseInfoReturn(finalHInfo, "createNewHouse");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);

                    //Return null to indicate error in task
                    hCallback.onHouseInfoReturn(null, "createNewHouse");
                });
        }

        return;
    }


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
