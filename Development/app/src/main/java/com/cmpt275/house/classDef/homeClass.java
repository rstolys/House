package com.cmpt275.house.classDef;

import android.content.Context;
import android.util.Log;

import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.home;
import com.cmpt275.house.interfaceDef.updateUI;

public class homeClass implements home {
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

    ////////////////////////////////////////////////////////////
    //
    // Constructor
    //
    ////////////////////////////////////////////////////////////
    public homeClass(Context mContext) {
        display = new displayMessage();
        firebaseUserTask = new userFirebaseClass();
        this.mContext = mContext;
    }

    ////////////////////////////////////////////////////////////
    //
    // Constructor
    //
    ////////////////////////////////////////////////////////////
    public void updateUserInfo(String user_id, booleanCallback callback) {

        if (!user_id.equals(null)) {
            //Get new userInfo to update
            firebaseUserTask.getUserInfo(user_id, (uInfo, success, errorMessage) -> {
                Log.d("getUserInfo", "Returned with success: " + success);

                if(success) {
                    //Update the userInfo
                    this.uInfo = uInfo;

                    callback.onReturn(true, "");
                }
                else {
                    display.showToastMessage(mContext, "Could not load task and house info. Try loading page again", display.LONG);
                    callback.onReturn(false, "");
                }
            });

        } else {
            Log.d("updateUserInfo", "user_id provided was null");
        }
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
}
