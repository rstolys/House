package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.signIn;

import com.cmpt275.house.interfaceDef.updateUI;

public class signInClass implements signIn {
    //
    // Class Variables
    //
    private final updateUI ui;            //Interface to update the UI state
    private boolean userLoggedIn;
    private final userFirebaseClass firebaseTask;

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

        String email2 = email; //"rstolys@sfu.ca"
        String password2 = Password; //"066923384"

        firebaseTask.signInUser(email2, password2, (uInfo, success, errorMessage) -> {
            Log.d("signInUser:", "Returned with success: " + success);
            //If the signIn was successful then set logged in to true

            if(success) {
                userLoggedIn = true;
                this.uInfo = uInfo;
            }

            //Tell ui to check if it needs to be updated
            ui.stateChanged(0);
        });
    }

    public void createAccount(userInfo uInfo) {
        //Do nothing for now

        //Create a new account
        firebaseTask.createAccount("Jayden Cole", "jcole@sfu.ca", "1234567", (uInfoReturn, success, errorMessage) -> {
            Log.d("createAccount:", "Returned with success: " + success);

            if(success) {
                Log.d("createAccount:", "User_id is: " + uInfoReturn.id);
            }
        });
    }

    public void forgotPassword(String email) {
        //Do nothing for now -- will call firebase forgot password
    }

    public boolean isUserSignedIn() {
        return userLoggedIn;
    }
}
