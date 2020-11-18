package com.cmpt275.house.classDef;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmpt275.house.classDef.documentClass.firebaseUserDocument;
import com.cmpt275.house.classDef.infoClass.feedbackInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.fInfoCallback;
import com.cmpt275.house.interfaceDef.Callbacks.uInfoCallback;
import com.cmpt275.house.interfaceDef.UsersBE;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class userFirebaseClass implements UsersBE {

    //
    // Class Variables
    //
    private final FirebaseFirestore db;
    private final String TAG = "FirebaseUserAction";

    //TODO: Add attributes to documentation
    private final FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    //
    // Class Functions
    //

    ////////////////////////////////////////////////////////////
    //
    // Constructor
    //
    ////////////////////////////////////////////////////////////
    public userFirebaseClass() {
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void createAccount(String name, String email, String password, uInfoCallback callback) {}
    public void resetPassword(String email, booleanCallback callback) {}
    public void modifyDisplayName(userInfo uInfo, uInfoCallback callback) {}
    public void modifyEmail(String newEmail, userInfo uInfo, uInfoCallback callback) {}
    public void updateNotificationSettings(userInfo uInfo, uInfoCallback callback) {}


    ////////////////////////////////////////////////////////////
    //
    // Will sign in the user using firebase authentication
    //
    ////////////////////////////////////////////////////////////
    public void signInUser(String email, String password, uInfoCallback callback) {

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

                                        for(QueryDocumentSnapshot document : Objects.requireNonNull(queryResult.getResult())) {
                                            //Convert queried document to userData class
                                            firebaseUserDocument userData = document.toObject(firebaseUserDocument.class);
                                            uInfo = userData.toUserInfo(document.getId(), currentUser.getEmail());

                                            Log.d(TAG, "User: " + document.getId() + " Successfully Logged In and info collected");
                                        }

                                        callback.onReturn(uInfo, true);
                                    }
                                    else {
                                        Log.d(TAG, "Error accessing the userInformation ", queryResult.getException());

                                        callback.onReturn(null, false);
                                    }
                                }
                            });
                    }
                    else {
                        //Log the error result
                        Log.w(TAG, "signInWithEmail:failure", task.getException());

                        callback.onReturn(null, false);
                    }
                }
            });
    }


    public void getUserInfo(String user_id, uInfoCallback callback) {}
    public void submitFeedback(feedbackInfo fInfo, fInfoCallback callback) {}
    public void logout(userInfo uInfo, booleanCallback callback) {}
}
