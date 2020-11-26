package com.cmpt275.house.classDef;

import android.content.Context;
import android.util.Log;

import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.home;

public class homeClass implements home {
    //
    // Class Variables
    //
    private final userFirebaseClass firebaseUserTask;
    private final taskFirebaseClass firebaseTaskTask;
    private final houseFirebaseClass firebaseHouseTask;
    private final displayMessage display;

    private final Context mContext;

    public userInfo uInfo;
    public taskInfo tInfos [];
    public houseInfo hInfos [];


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
        firebaseTaskTask = new taskFirebaseClass();
        firebaseHouseTask = new houseFirebaseClass();

        this.mContext = mContext;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will logout the user
    //
    ////////////////////////////////////////////////////////////
    public void logout() {
        //Show we are logging out
        display.showToastMessage(mContext, "Logging out", display.LONG);

        //Logout user
        firebaseUserTask.logout(uInfo, (result, errorMessage) -> {
            if(result) {
                Log.d("logout:", "User " + uInfo.displayName + " is logged out");
            }

            //Update the UI to move back to main activity
        });
    }


    ////////////////////////////////////////////////////////////
    //
    // Will get the current tasks to display for the user
    //
    ////////////////////////////////////////////////////////////
    public void viewTasks(String user_id) {

        //Make sure  the user_id is valid
        if(user_id == null) {
            display.showToastMessage(mContext, "Oops. Looks like something went wrong", display.LONG);
            //This should not occur, we should probably logout the user if this happens
        }
        else {
            firebaseTaskTask.getCurrentTasks(uInfo, (tInfos, success, errorMessage) -> {
                Log.d("getCurrentTasks:", "Returned with success " + success);

                if(success) {
                    this.tInfos = tInfos;
                    //Do stuff here...
                }
                else {
                    display.showToastMessage(mContext, errorMessage, display.LONG);
                }
            });
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will get the current houses to display for the user
    //
    ////////////////////////////////////////////////////////////
    public void viewHouses(String user_id) {

        //Make sure  the user_id is valid
        if(user_id == null) {
            display.showToastMessage(mContext, "Oops. Looks like something went wrong.", display.LONG);
            //This should not occur, we should probably logout the user if this happens
        }
        else {
            firebaseHouseTask.getCurrentHouses(uInfo, (hInfos, success, errorMessage) -> {
                Log.d("getCurrentTasks:", "Returned with success " + success);

                if(success) {
                    this.hInfos = hInfos;
                    //Do stuff here...
                }
                else {
                    display.showToastMessage(mContext, errorMessage, display.LONG);
                }
            });
        }
    }
}
