package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.updateUI;
import com.cmpt275.house.interfaceDef.updateUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signIn {
    ///
    /// Class Variables
    ///
    private updateUI ui;            //Interface to update the UI state

    private String email;
    private String password;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private static final String TAG = "EmailPassword";

    private String errorMessage = "";


    ///
    /// Class Functions
    ///
    public signIn(updateUI ui) {
        this.ui = ui;       //Set the class implementing our ui updates
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void signInUser() {

        //Call change state function **Used in Prototype**
        ui.stateChanged();

        /*
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            currentUser = firebaseAuth.getCurrentUser();

                            ui.changeState();       //Will update a state setting which will cause an update to the UI
                        }
                        else {
                            //Log the error result
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            // Display a message to the user.
                            errorMessage = "Authentication Failed";

                            ui.changeState();
                        }

                    }
                });
         */

        return;
    }

    public FirebaseUser getUserState() {
        //Get the current user state and assign to private variable
        currentUser = firebaseAuth.getCurrentUser();
        return currentUser;
    }

    public boolean isUserSignedIn() {
        return true; //(currentUser != null);
    }

}
