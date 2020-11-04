package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.signIn;

import com.cmpt275.house.interfaceDef.updateUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signInClass implements signIn {
    //
    // Class Variables
    //
    private updateUI ui;            //Interface to update the UI state

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private static final String TAG = "EmailPassword";

    private String errorMessage = "";


    //
    // Class Functions
    //
    public signInClass(updateUI ui) {
        this.ui = ui;       //Set the class implementing our ui updates
    }

    public void signInUser(String email, String Password) {

        //Call change state function **Used in Prototype**
        ui.stateChanged(0);

        /*
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            currentUser = firebaseAuth.getCurrentUser();

                            ui.stateChanged(0);       //Will update a state setting which will cause an update to the UI
                        }
                        else {
                            //Log the error result
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            // Display a message to the user.
                            errorMessage = "Authentication Failed";

                            ui.stateChanged(0);
                        }

                    }
                });
         */


        return;
    }

    public void createAccount(String email, String displayName, String password) {
        //Do nothing for now
        ui.stateChanged(0);
    }

    public void forgotPassword(String email) {
        //Do nothing for now -- will call firebase forgot password
    }

    public boolean isUserSignedIn() {
        return true; //(currentUser != null);
    }

}
