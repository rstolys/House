package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.Callbacks.uInfoCallback;
import com.cmpt275.house.interfaceDef.signIn;

import com.cmpt275.house.interfaceDef.updateUI;

public class signInClass implements signIn {
    //
    // Class Variables
    //
    private updateUI ui;            //Interface to update the UI state
    private boolean userLoggedIn;
    private userFirebaseClass firebaseTask;

    private static final String TAG = "SignInClass";
    public userInfo uInfo;



    //
    // Class Functions
    //

    ////////////////////////////////////////////////////////////
    //
    // Constructor
    //
    ////////////////////////////////////////////////////////////
    public signInClass(updateUI ui) {
        firebaseTask = new userFirebaseClass();

        this.ui = ui;       //Set the class implementing our ui updates
        userLoggedIn = false;
    }

    public void signInUser(String email, String Password) {

        signInClass self = this;

        //Call change state function **Used in Prototype**
        firebaseTask.signInUser("ryanstolys@gmail.com", "123456", new uInfoCallback() {
            @Override
            public void onReturn(userInfo uInfo, boolean success) {
                Log.d("signInUser:", "Returned with success: " + success);;
                //If the signIn was successful then set logged in to true
                if(success) {
                    userLoggedIn = true;
                    self.uInfo = uInfo;
                }

                //Tell ui to check if it needs to be updated
                ui.stateChanged(0);
            }
        });

        return;
    }

    public void createAccount(userInfo uInfo) {
        //Do nothing for now
        ui.stateChanged(0);
    }

    public void forgotPassword(String email) {
        //Do nothing for now -- will call firebase forgot password
    }

    public boolean isUserSignedIn() {
        return userLoggedIn;
    }
}
