package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.classDef.infoClass.feedbackInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.settings;

public class settingsClass implements settings {

    //
    // Class Variables
    //
    private userInfo uInfo;
    private final userFirebaseClass firebaseTask;

    //
    // Class Functions
    //

    /////////////////////////////////////////////////
    //
    // Constructor
    //
    /////////////////////////////////////////////////
    public settingsClass() {
        uInfo = new userInfo();
        firebaseTask = new userFirebaseClass();
    }

    public void logout() {
        userInfo myUInfo = new userInfo();

        myUInfo.id = "w4OFKQrvL28T3WlXVP4X";

        firebaseTask.logout(myUInfo, (result, errorMessage) -> {
            Log.d("logout:", "Returned with success: " + result);
        });
    }

    public void viewLegal() {
        userInfo myUInfo = new userInfo();

        myUInfo.id = "w4OFKQrvL28T3WlXVP4X";
        myUInfo.firebase_id = "njjv0YpC09gCUAUVfJ7btc6NXpB3";

        firebaseTask.getUserInfo("w4OFKQrvL28T3WlXVP4X", (uInfo, success, errorMessage) -> {
            Log.d("getUserInfo:", "Returned with success: " + success);

            if(success) {
                Log.d("getUserInfo:", "User ID: " + uInfo.id);
            }
        });
    }

    public void viewSettings(String user_id) {}

    public void changeDisplayName(String displayName) {

        userInfo myUInfo = new userInfo();

        myUInfo.id = "w4OFKQrvL28T3WlXVP4X";

        firebaseTask.modifyDisplayName(myUInfo, "Ryan Test Stolys", (uInfo, success, errorMessage) -> {
            Log.d("modifyDisplayName:", "Returned with success: " + success);

            if(success) {
                Log.d("modifyDisplayName:", "New user display name: " + uInfo.displayName);
            }
        });
    }

    public void changeEmail(String email) {

        userInfo myUInfo = new userInfo();

        myUInfo.id = "w4OFKQrvL28T3WlXVP4X";
        myUInfo.firebase_id = "njjv0YpC09gCUAUVfJ7btc6NXpB3";

        firebaseTask.modifyEmail("rstolys@sfu.ca", myUInfo, (uInfo, success, errorMessage) -> {
            Log.d("modifyEmail:", "Returned with success: " + success);
        });
    }

    public void changeNotifications(boolean allowNotifications) {

        userInfo myUInfo = new userInfo();

        myUInfo.id = "w4OFKQrvL28T3WlXVP4X";

        firebaseTask.updateNotificationSettings(myUInfo, allowNotifications,(uInfo, success, errorMessage) -> {
            Log.d("updateNotifications:", "Returned with success: " + success);
        });
    }

    public void resetPassword(String email) {

        firebaseTask.resetPassword("ryanstolys@gmail.com", (result, errorMessage) -> {
            Log.d("resetPassword:", "Returned with result: " + result);

            if(!result) {
                Log.d("resetPassword:", "Error Message: " + errorMessage);
            }
        });
    }

    public void provideFeedback(String feedback) {

        feedbackInfo myFInfo = new feedbackInfo();

        myFInfo.feedback = feedback;
        myFInfo.user_id = "w4OFKQrvL28T3WlXVP4X";

        firebaseTask.submitFeedback(myFInfo, (fInfo, success, errorMessage) -> {
            Log.d("submitFeedback:", "Returned with result: " + success);

            if(!success) {
                Log.d("submitFeedback:", "Error Message: " + errorMessage);
            }
        });
    }
}
