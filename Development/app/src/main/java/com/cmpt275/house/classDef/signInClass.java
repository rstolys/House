package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.interfaceDef.signIn;

import com.cmpt275.house.interfaceDef.updateUI;
import com.cmpt275.house.interfaceDef.userCallbacks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signInClass implements signIn, userCallbacks {
    //
    // Class Variables
    //
    private updateUI ui;            //Interface to update the UI state
    private boolean userLoggedIn;
    private userFirebaseClass firebaseTask;

    private static final String TAG = "SignInClass";



    //
    // Class Functions
    //
    public signInClass(updateUI ui) {
        firebaseTask = new userFirebaseClass(this);

        this.ui = ui;       //Set the class implementing our ui updates
        userLoggedIn = false;
    }

    public void signInUser(String email, String Password) {

        //Call change state function **Used in Prototype**
        firebaseTask.signInUser("ryanstolys@gmail.com", "123456");

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


    //userCallbacks
    public void onUserInfoArrayReturn(userInfo[] uInfos, String functionName) {return;}
    public void onUserInfoReturn(userInfo uInfo, String functionName) {
        Log.d("UserInfoReturn:", "Return from " + functionName);
        if(uInfo != null)
            Log.d("UserInfoReturn:", "uInfo is " + uInfo.id);

        switch(functionName) {
            case "signInUser":
                if(uInfo != null)
                    userLoggedIn = true;

                ui.stateChanged(0);     //Tell ui to check if it needs to be updated
                break;
        }

        return;
    }
    public void onUserBooleanReturn(userInfo uInfo, String functionName) {return;}

}
