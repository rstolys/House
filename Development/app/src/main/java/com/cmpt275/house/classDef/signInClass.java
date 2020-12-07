package com.cmpt275.house.classDef;

import android.content.Context;
import android.util.Log;

import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.Callbacks.updateCallback;
import com.cmpt275.house.interfaceDef.signIn;

public class signInClass implements signIn {
    //
    // Class Variables
    //
    private final userFirebaseClass firebaseUserTask;
    private final displayMessage display;

    private final Context mContext;

    private boolean userLoggedIn;
    public userInfo uInfo;


    //
    // Class Functions
    //

    ////////////////////////////////////////////////////////////
    //
    // Constructor
    //
    ////////////////////////////////////////////////////////////
    public signInClass(Context mContext) {
        firebaseUserTask = new userFirebaseClass();
        display = new displayMessage();

        this.mContext = mContext;
        userLoggedIn = false;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will sign in the user using firebase auth
    //
    ////////////////////////////////////////////////////////////
    public void signInUser(String email, String password, updateCallback callback) {

        //Can remove these in production
        email = email; //"rstolys@sfu.ca"
        password = password; //"066923384"

        if(email == null || email.length() < 1) {
            display.showToastMessage(mContext, "You email input is empty.", display.LONG);
        }
        else if(password == null || password.length() < 1) {
            display.showToastMessage(mContext, "You password input is empty.", display.LONG);
        }
        else {
            firebaseUserTask.signInUser(email, password, (uInfo, success, errorMessage) -> {
                Log.d("signInUser:", "Returned with success: " + success);
                //If the signIn was successful then set logged in to true

                if(success) {
                    userLoggedIn = true;
                    this.uInfo = uInfo;
                }
                else {
                    Log.d("signInUser:", "Error Message: " + errorMessage);
                    display.showToastMessage(mContext, errorMessage, display.LONG);
                }

                callback.onReturn(userLoggedIn);
            });
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will create a new user account
    //
    ////////////////////////////////////////////////////////////
    public void createAccount(String displayName, String email, String password) {

        //make sure all the required fields are valid
        if(displayName == null || displayName.length() < 1) {
            display.showToastMessage(mContext, "You display name must be at least 1 character long", display.LONG);
        }
        else if(email == null || email.length() < 1) {
            display.showToastMessage(mContext, "You email name must be at least 1 character long", display.LONG);
        }
        else if(password == null || password.length() <= 5) {
            //Firebase has minimum on password length. Will avoid error later if the password is short than 6 characters
            display.showToastMessage(mContext, "You password name must be at least 6 characters long", display.LONG);
        }
        else {
            //All values are valid -- Create a new account
            firebaseUserTask.createAccount(displayName, email, password, (uInfo, success, errorMessage) -> {
                Log.d("createAccount:", "Returned with success: " + success);

                if(success) {
                    Log.d("createAccount:", "User_id is: " + uInfo.id);
                    display.showToastMessage(mContext, "You account  was successfully created! You can now login", display.LONG);

                    this.uInfo = uInfo;

                    //Maybe we can automatically log the user in
                }
                else {
                    Log.d("createAccount:", "Error Message " + errorMessage);
                    display.showToastMessage(mContext, errorMessage, display.LONG);
                }
            });
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will initiate the forgot password procedure
    //
    ////////////////////////////////////////////////////////////
    public void forgotPassword(String email) {

        if(email == null || email.length() < 1) {
            Log.d("forgotPassword", "email provided is null or empty");

            display.showToastMessage(mContext, "Oops. We don't see an email to send a reset link to. Try entering your email again", display.LONG);
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


    ////////////////////////////////////////////////////////////
    //
    // Will automatically check the users auth state and move to home page if needed
    //
    ////////////////////////////////////////////////////////////
    public void checkAuthStatus(updateCallback callback) {
        //Check to see if user already has valid authentication
        firebaseUserTask.getUserAuthStatus( (firebase_id, success, errorMessage) -> {
            if(success) {
                Log.d("getUserAuthStatus:", "User is currently signed in. Collecting userInfo");

                firebaseUserTask.getUserInfo_firebaseID(firebase_id, (uInfo, success2, errorMessage2) -> {

                    if(success2) {
                        Log.d("getUserInfo_firebaseID:", "User info collected. Moving to home activity");
                        this.uInfo = uInfo;     //Set the uInfo to be passed

                        userLoggedIn = true;                      //Indicate user is signed in already
                    }
                    else {
                        Log.d("getUserInfo_firebaseID:", "User info could not be collected");
                        Log.d("getUserInfo_firebaseID:", "Reason: " + errorMessage2);

                        userLoggedIn = false;                      //Force user to signIn and collect information again
                    }

                    callback.onReturn(userLoggedIn);
                });
            }
            else {
                Log.d("getUserAuthStatus:", "User is NOT currently signed in");
                userLoggedIn = false;
                callback.onReturn(userLoggedIn);
            }
        });
    }


    ////////////////////////////////////////////////////////////
    //
    // Will return boolean indicating current status of user
    //
    ////////////////////////////////////////////////////////////
    public boolean isUserSignedIn() {
        return userLoggedIn;
    }
}
