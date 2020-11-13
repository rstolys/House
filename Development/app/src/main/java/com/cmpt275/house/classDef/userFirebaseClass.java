package com.cmpt275.house.classDef;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmpt275.house.interfaceDef.UsersBE;
import com.cmpt275.house.interfaceDef.userCallbacks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class userFirebaseClass implements UsersBE {

    //
    // Class Variables
    //
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private userCallbacks uCallback;
    private final String TAG = "FirebaseUserAction";

    //TODO: Add attributes to documentation
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    //
    // Class Functions
    //
    public userFirebaseClass(userCallbacks uCallback) {
        this.uCallback = uCallback;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void createAccount(String name, String email, String password) {return;}
    public void resetPassword(String email) {return;}
    public void modifyDisplayName(userInfo uInfo) {return;}
    public void modifyEmail(String newEmail, userInfo uInfo) {return;}
    public void updateNotificationSettings(userInfo uInfo) {return;}


    ////////////////////////////////////////////////////////////
    //
    // Will sign in the user using firebase authentication
    //
    ////////////////////////////////////////////////////////////
    public void signInUser(String email, String password) {

        Log.d(TAG, "Attempting sign in with email: " + email + " and password: " + password);

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");

                        currentUser = firebaseAuth.getCurrentUser();

                        //Get the users info to return
                        db.collection("users").whereEqualTo("firebase_id", currentUser.getUid()).limit(1).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> queryResult) {

                                    if(queryResult.isSuccessful()) {
                                        userInfo uInfo = null;

                                        for(QueryDocumentSnapshot document : queryResult.getResult()) {
                                            //Convert queried document to userData class
                                            firebaseUserDocument userData = document.toObject(firebaseUserDocument.class);
                                            uInfo = userData.toUserInfo(document.getId(), currentUser.getEmail());

                                            Log.d(TAG, "User: " + document.getId() + " Successfully Logged In and info collected");
                                        }

                                        uCallback.onUserInfoReturn(uInfo, "signInUser");
                                    }
                                    else {
                                        Log.d(TAG, "Error accessing the userInformation ", queryResult.getException());

                                        uCallback.onUserInfoReturn(null, "signInUser");
                                    }
                                }
                            });
                    }
                    else {
                        //Log the error result
                        Log.w(TAG, "signInWithEmail:failure", task.getException());

                        uCallback.onUserInfoReturn(null, "signInUser");
                    }
                }
            });

        return;
    }


    public void getUserInfo(String user_id) {return;}
    public void submitFeedback(feedbackInfo fInfo) {return;}
    public void logout(userInfo uInfo) {return;}
}
