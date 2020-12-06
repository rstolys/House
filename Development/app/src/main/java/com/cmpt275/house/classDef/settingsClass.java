package com.cmpt275.house.classDef;

import android.content.Context;
import android.util.Log;

import com.cmpt275.house.R;
import com.cmpt275.house.classDef.infoClass.feedbackInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.settings;
import com.cmpt275.house.interfaceDef.updateUI;
import com.google.android.gms.common.api.internal.IStatusCallback;

import java.util.Date;

public class settingsClass implements settings {

    //
    // Class Variables
    //
    private final userFirebaseClass firebaseUserTask;
    private final displayMessage display;

    private final Context mContext;

    public userInfo uInfo;


    //
    // Class Functions
    //
    /////////////////////////////////////////////////
    //
    // Constructor
    //
    /////////////////////////////////////////////////
    public settingsClass(Context mContext) {
        firebaseUserTask = new userFirebaseClass();
        display = new displayMessage();

        this.mContext = mContext;
    }


    /////////////////////////////////////////////////
    //
    // Will Logout the user
    //
    /////////////////////////////////////////////////
    public void logout(updateUI callback) {
        //Show we are logging out
        display.showToastMessage(mContext, "Logging out", display.LONG);

        //Logout user
        firebaseUserTask.logout(uInfo, (result, errorMessage) -> {
            Log.d("logout:", "User " + uInfo.displayName + " is logged out");

            callback.stateChanged(0);
        });
    }


    /////////////////////////////////////////////////
    //
    // Will get the legal messages to display for the user
    //
    /////////////////////////////////////////////////
    public void viewLegal() {
        Log.d("viewLegal:", "Called. Will set the legal information to be viewed.");
    }


    /////////////////////////////////////////////////
    //
    // Will load the user information to display the settings
    //
    /////////////////////////////////////////////////
    public void viewSettings(String user_id) {

        if(user_id == null) {          //Make sure  the user_id is valid
                display.showToastMessage(mContext, "Oops. Looks like something went wrong", display.LONG);
                //This should not occur, we should probably logout the user if this happens
        }
        else {
            firebaseUserTask.getUserInfo(user_id, (uInfo, success, errorMessage) -> {
                Log.d("getUserInfo:", "Returned with success " + success);

                if(success) {
                    this.uInfo = uInfo;
                    //Do stuff here...
                }
                else {
                    Log.d("getUserInfo:", "Error Message: " + errorMessage);
                    display.showToastMessage(mContext, errorMessage, display.LONG);
                }
            });
        }
    }


    /////////////////////////////////////////////////
    //
    // Will change the users display name
    //
    /////////////////////////////////////////////////
    public void changeDisplayName(String displayName) {

        if(displayName == null || displayName.length() < 1) {
            Log.d("changeDisplayName", "display name provided is null or empty");

            display.showToastMessage(mContext, "Your display name can't be empty", display.LONG);
        }
        else {
            firebaseUserTask.modifyDisplayName(uInfo, displayName, (uInfo, success, errorMessage) -> {
                Log.d("modifyDisplayName:", "Returned with success: " + success);

                if(success) {
                    Log.d("modifyDisplayName:", "New user display name: " + uInfo.displayName);
                    display.showToastMessage(mContext, "Display name successfully updated!", display.LONG);
                    this.uInfo = uInfo;

                    //Do Stuff here...
                }
                else {
                    Log.d("modifyDisplayName:", "Error Message: " + errorMessage);
                    display.showToastMessage(mContext, errorMessage, display.LONG);
                }
            });
        }
    }


    /////////////////////////////////////////////////
    //
    // Will change the users email
    //
    /////////////////////////////////////////////////
    public void changeEmail(String email) {

        if(email == null || email.length() < 1) {
            Log.d("changeEmail", "email provided is null or empty");

            display.showToastMessage(mContext, "Your email can't be empty", display.LONG);
        }
        else {
            firebaseUserTask.modifyEmail(email, uInfo, (uInfo, success, errorMessage) -> {
                Log.d("modifyEmail:", "Returned with success: " + success);

                if(success) {
                    Log.d("modifyEmail:", "New user email: " + uInfo.email);
                    display.showToastMessage(mContext, "Email successfully updated!", display.LONG);

                    this.uInfo = uInfo;         //Update the userInfo

                    //Do Stuff here...
                }
                else {
                    Log.d("modifyEmail:", "Error Message: " + errorMessage);
                    display.showToastMessage(mContext, errorMessage, display.LONG);
                }
            });
        }
    }


    /////////////////////////////////////////////////
    //
    // Will change the users notification settings
    //
    /////////////////////////////////////////////////
    public void changeNotifications(boolean allowNotifications) {

        firebaseUserTask.updateNotificationSettings(uInfo, allowNotifications,(uInfo, success, errorMessage) -> {
            Log.d("updateNotifications:", "Returned with success: " + success);

            if(success) {
                display.showToastMessage(mContext, "Notification Settings successfully updated!", display.LONG);
                this.uInfo = uInfo;
                //Do Stuff here
            }
            else {
                Log.d("updateNotifications:", "Error Message: " + errorMessage);
                display.showToastMessage(mContext, errorMessage, display.LONG);
            }
        });
    }


    /////////////////////////////////////////////////
    //
    // Will start the reset password procedure
    //
    /////////////////////////////////////////////////
    public void resetPassword(String email) {

        if(email == null || email.length() < 1) {
            Log.d("resetPassword", "email provided is null or empty");

            display.showToastMessage(mContext, "Oops. Looks like we ran into an issue. Try signing in again", display.LONG);

            //We should send user to signIn again since we are missing information we should have
        }
        else {
            firebaseUserTask.resetPassword(email, (result, errorMessage) -> {
                Log.d("resetPassword:", "Returned with result: " + result);

                if(result) {
                    display.showToastMessage(mContext, "Password Reset Email Sent!", display.LONG);
                }
                else {
                    Log.d("resetPassword:", "Error Message: " + errorMessage);
                    display.showToastMessage(mContext, errorMessage, display.LONG);
                }
            });
        }
    }


    /////////////////////////////////////////////////
    //
    // Will submit user generated feedback
    //
    /////////////////////////////////////////////////
    public void provideFeedback(String feedback, booleanCallback callback) {

        if(feedback == null || feedback.length() < 1) {
            Log.d("provideFeedback", "Feedback provided is null or empty");

            display.showToastMessage(mContext, "Oops. We want to hear more from you before we submit your feedback. Keep typing", display.LONG);
        }
        else {
            //Create new feedback info class to submit
            feedbackInfo fInfo = new feedbackInfo();
            fInfo.date = new Date();
            fInfo.feedback = feedback;
            fInfo.user_id = uInfo.id;

            //Submit feedback
            firebaseUserTask.submitFeedback(fInfo, (result, errorMessage) -> {
                Log.d("submitFeedback:", "Returned with result: " + result);

                if(result) {
                    display.showToastMessage(mContext, "Feedback successfully submitted. Thanks for helping make this app better!", display.LONG);

                    callback.onReturn(true, "");
                }
                else {
                    Log.d("submitFeedback:", "Error Message: " + errorMessage);
                    display.showToastMessage(mContext, errorMessage, display.LONG);

                    callback.onReturn(false, "");
                }
            });
        }
    }
}
