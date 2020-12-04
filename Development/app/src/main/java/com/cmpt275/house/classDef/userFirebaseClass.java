package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.classDef.documentClass.firebaseFeedbackDocument;
import com.cmpt275.house.classDef.documentClass.firebaseUserDocument;
import com.cmpt275.house.classDef.infoClass.feedbackInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.stringCallback;
import com.cmpt275.house.interfaceDef.Callbacks.uInfoCallback;
import com.cmpt275.house.interfaceDef.UsersBE;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.Objects;

public class userFirebaseClass implements UsersBE {

    //
    // Class Variables
    //
    private final FirebaseFirestore db;
    private final String TAG = "FirebaseUserAction";

    private final String NO_ERROR = "";
    private final String INVALID_PARAMETER_MESSAGE = "Looks like we couldn't access your information. Try signing in again";
    private final String DATABASE_ERROR_MESSAGE = "Oops! Looks like there was an error on our end. Sorry about that. Please try again";
    private final String UNKNOWN_ERROR_MESSAGE = "Oops! Looks something went wrong there. Sorry about that. Please try again";

    private final FirebaseAuth firebaseAuth;

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

    ////////////////////////////////////////////////////////////
    //
    // Check the user authentication status
    //
    ////////////////////////////////////////////////////////////
    public void getUserAuthStatus(stringCallback callback) {

        try{
            FirebaseUser cUser = firebaseAuth.getCurrentUser();
            if(cUser != null) {
                myFirebaseAuth.Auth.setCurrentUser(cUser);
                callback.onReturn(cUser.getUid(), true, NO_ERROR);
            }
            else {
                myFirebaseAuth.Auth.setCurrentUser(null);
                callback.onReturn(null, false, NO_ERROR);
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn( null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }



    ////////////////////////////////////////////////////////////
    //
    // Create a user account
    //
    ////////////////////////////////////////////////////////////
    public void createAccount(String name, String email, String password, uInfoCallback callback) {

        try{
            if (myFirebaseAuth.Auth.getCurrentUser() != null) {
                Log.d(TAG, "There is already a user signed in");

                callback.onReturn(null, false, "Looks like you already have an account and are signed in.");
            }
            else if (name == null || email == null || password == null) {
                Log.d(TAG, "Invalid user information passed");

                callback.onReturn(null, false, "Looks like you didn't provide us with the appropriate information. Please try again");
            }
            else {
                Log.d(TAG, "Creating account for " + name + " with email " + email);

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");

                            //Set the current user
                            myFirebaseAuth.Auth.setCurrentUser(firebaseAuth.getCurrentUser());

                            userInfo uInfo = new userInfo();
                            uInfo.notificationsAllowed = false;
                            uInfo.firebase_id = myFirebaseAuth.Auth.getCurrentUser().getUid();
                            uInfo.email = email;
                            uInfo.displayName = name;
                            uInfo.houses = null;
                            uInfo.moneyBalance = null;
                            uInfo.tasks = null;

                            firebaseUserDocument userData = new firebaseUserDocument(uInfo);

                            //Get the users info to return
                            db.collection("users").add(userData)
                                .addOnSuccessListener(documentReference -> {
                                    Log.d(TAG, "User created with id: " + documentReference.getId());

                                    //Add id to uInfo
                                    uInfo.id = documentReference.getId();

                                    callback.onReturn(uInfo, true, NO_ERROR);

                                })
                                .addOnFailureListener(e -> {
                                    Log.d(TAG, "Error adding new user to database");

                                    callback.onReturn(null, false, "There was an issue creating your account. Please Try again");
                                });
                        }
                        //Don't need to check if task failed here since it will result in
                        // duplicated error behaviour. Handled below instead
                    }).addOnFailureListener(e -> callback.onReturn(null, false, e.getLocalizedMessage()));
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Begin the reset user password procedure. Firebase Auth handles rest of procedure
    //
    ////////////////////////////////////////////////////////////
    public void resetPassword(String email, booleanCallback callback) {

        try {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Reset Email sent");

                        callback.onReturn( true, "Password Reset Email sent. Check your email for further instructions.");
                    }
                    //Failure of task is handled below
                }).addOnFailureListener(e -> callback.onReturn( false, e.getLocalizedMessage()));
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn( false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will change the users display name
    //
    ////////////////////////////////////////////////////////////
    public void modifyDisplayName(userInfo uInfo, String newName, uInfoCallback callback) {

        try {
            if(newName == null || uInfo.id == null) {
                Log.d(TAG, "Invalid parameters passed");

                callback.onReturn(null, false, "Looks like there was an issue collecting your new info. Try again");
            }
            else {
                Log.d(TAG, "Changing display name of user: " + uInfo.id + " to " + newName);

                //update the users name in their uInfo class
                uInfo.displayName = newName;

                //convert the uInfo class to a database class
                firebaseUserDocument updatedUser = new firebaseUserDocument(uInfo);


                //Get documents from the collection that have user_id specified
                db.collection("users").document(uInfo.id).update("displayName", updatedUser.getDisplayName())
                    .addOnCompleteListener(task -> {

                        if(task.isSuccessful()) {
                            Log.d(TAG, "User: " + uInfo.id + " successfully updated their name");

                            //return the new userInfo
                            callback.onReturn(uInfo, true, NO_ERROR);
                        }
                        else {
                            Log.d(TAG, "Error updated the users display name", task.getException());

                            //Call function to return task value
                            callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                        }
                    });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will change the users email in the firebase auth system
    //
    ////////////////////////////////////////////////////////////
    public void modifyEmail(String newEmail, userInfo uInfo, uInfoCallback callback) {

        try {
            if (newEmail == null || uInfo.id == null) {
                Log.d(TAG, "Invalid parameters passed");

                callback.onReturn(null, false, "Looks like there was an issue collecting your new info. Try again");
            } else {
                Log.d(TAG, "Changing email of user: " + uInfo.id + " to " + newEmail);

                //Make sure the user is updating their own email
                if (myFirebaseAuth.Auth.getCurrentUser().getUid().equals(uInfo.firebase_id)) {

                    myFirebaseAuth.Auth.getCurrentUser().updateEmail(newEmail)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Successfully updated users email");

                                //update the users name in their uInfo class
                                uInfo.email = newEmail;

                                callback.onReturn(uInfo, true, NO_ERROR);
                            }
                        }).addOnFailureListener(e -> callback.onReturn(null, false, e.getLocalizedMessage()));
                } else {
                    Log.d(TAG, "User is trying to update a different users email");

                    callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
                }
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will update the users notification settings
    //
    ////////////////////////////////////////////////////////////
    public void updateNotificationSettings(userInfo uInfo, boolean newNotificationSetting, uInfoCallback callback) {

        try {
            if (uInfo.id == null) {
                Log.d(TAG, "Invalid parameters passed");

                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            } else {
                Log.d(TAG, "Changing notification settings of user: " + uInfo.id + " to " + (newNotificationSetting ? "allowed" : "not allowed"));

                //update the users name in their uInfo class
                uInfo.notificationsAllowed = newNotificationSetting;

                //convert the uInfo class to a database class
                firebaseUserDocument updatedUser = new firebaseUserDocument(uInfo);


                //Get documents from the collection that have user_id specified
                db.collection("users").document(uInfo.id).update("notificationsAllowed", updatedUser.getNotificationsAllowed())
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "User: " + uInfo.id + " successfully updated their notification settings");

                            //return the new userInfo
                            callback.onReturn(uInfo, true, NO_ERROR);
                        } else {
                            Log.d(TAG, "Error updated the users notification settings", task.getException());

                            //Call function to return task value
                            callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                        }
                    });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will sign in the user using firebase authentication
    //
    ////////////////////////////////////////////////////////////
    public void signInUser(String email, String password, uInfoCallback callback) {

        try{
            Log.d(TAG, "Attempting sign in with email: " + email + " and password: " + password);

            if(myFirebaseAuth.Auth.getCurrentUser() == null) {

                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            myFirebaseAuth.Auth.setCurrentUser(firebaseAuth.getCurrentUser());

                            //Get the users info to return
                            db.collection("users").whereEqualTo("firebase_id", myFirebaseAuth.Auth.getCurrentUser().getUid()).limit(1).get()
                                .addOnCompleteListener(queryResult -> {

                                    if (queryResult.isSuccessful()) {
                                        userInfo uInfo = null;

                                        for (QueryDocumentSnapshot document : Objects.requireNonNull(queryResult.getResult())) {
                                            //Convert queried document to userData class
                                            firebaseUserDocument userData = document.toObject(firebaseUserDocument.class);
                                            uInfo = userData.toUserInfo(document.getId(), myFirebaseAuth.Auth.getCurrentUser().getEmail());

                                            Log.d(TAG, "User: " + document.getId() + " Successfully Logged In and info collected");
                                        }

                                        callback.onReturn(uInfo, true, NO_ERROR);
                                    }
                                    else {
                                        Log.d(TAG, "Error accessing the userInformation ", queryResult.getException());

                                        //Set current user to null to try again from scratch
                                        myFirebaseAuth.Auth.setCurrentUser(null);

                                        callback.onReturn(null, false, "We could not access your user information.\n Please try to sign in again");
                                    }
                                });
                        }
                        else {
                            //Log the error result
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            callback.onReturn(null, false, "Looks like the information you provided was incorrect. Please try againj");
                        }
                    });
            }
            else {
                //Get the users info to return
                db.collection("users").whereEqualTo("firebase_id", myFirebaseAuth.Auth.getCurrentUser().getUid()).limit(1).get()
                    .addOnCompleteListener(queryResult -> {

                        if (queryResult.isSuccessful()) {
                            userInfo uInfo = null;

                            for (QueryDocumentSnapshot document : Objects.requireNonNull(queryResult.getResult())) {
                                //Convert queried document to userData class
                                firebaseUserDocument userData = document.toObject(firebaseUserDocument.class);
                                uInfo = userData.toUserInfo(document.getId(), myFirebaseAuth.Auth.getCurrentUser().getEmail());

                                Log.d(TAG, "User: " + document.getId() + " Successfully Logged In and info collected");
                            }

                            callback.onReturn(uInfo, true, NO_ERROR);
                        }
                        else {
                            Log.d(TAG, "Error accessing the userInformation ", queryResult.getException());

                            //Set current user to null to try again from scratch
                            myFirebaseAuth.Auth.setCurrentUser(null);

                            callback.onReturn(null, false, "We could not access your user information.\n Please try to sign in again");
                        }
                    });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will access this specified users information based on user_id
    //
    ////////////////////////////////////////////////////////////
    public void getUserInfo(String user_id, uInfoCallback callback) {

        try{
            //Ensure the uInfo has a valid id
            if(user_id == null) {
                Log.w(TAG, "getUserInfo called with null userID");

                //We cannot access db without valid id for user. Return failure.
                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "getUserInfo called for user: " + user_id);

                //Get documents from the collection that have user_id specified
                db.collection("users").document(user_id).get()
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "get() user document successful for user: " + user_id);

                        //Convert user to userInfo class
                        firebaseUserDocument userData = documentReference.toObject(firebaseUserDocument.class);
                        assert userData != null;
                        userInfo uInfo = userData.toUserInfo(user_id, myFirebaseAuth.Auth.getCurrentUser().getEmail());

                        //Call function to return task value
                        callback.onReturn(uInfo, true, NO_ERROR);
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error getting user info document", e);

                        //Return null to indicate error in task
                        callback.onReturn(null, false, DATABASE_ERROR_MESSAGE);
                    });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }

    ////////////////////////////////////////////////////////////
    //
    // Will access this specified users information
    //
    ////////////////////////////////////////////////////////////
    public void getUserInfo_firebaseID(String firebase_id, uInfoCallback callback) {

        try{
            //Ensure the uInfo has a valid id
            if(firebase_id == null) {
                Log.w(TAG, "getUserInfo called with null firebase_id");

                //We cannot access db without valid id for user. Return failure.
                callback.onReturn(null, false, INVALID_PARAMETER_MESSAGE);
            }
            else {
                Log.d(TAG, "getUserInfo called for firebase user: " + firebase_id);

                //Get documents from the collection that have user_id specified
                db.collection("users").whereEqualTo("firebase_id", firebase_id).limit(1).get()
                    .addOnCompleteListener(queryResult -> {

                        if (queryResult.isSuccessful()) {
                            userInfo uInfo = null;

                            for (QueryDocumentSnapshot document : Objects.requireNonNull(queryResult.getResult())) {
                                //Convert queried document to userData class
                                firebaseUserDocument userData = document.toObject(firebaseUserDocument.class);
                                uInfo = userData.toUserInfo(document.getId(), myFirebaseAuth.Auth.getCurrentUser().getEmail());

                                Log.d(TAG, "User: " + document.getId() + " Successfully Logged In and info collected");
                            }

                            callback.onReturn(uInfo, true, NO_ERROR);
                        }
                        else {
                            Log.d(TAG, "Error accessing the userInformation ", queryResult.getException());

                            //Set current user to null to try again from scratch
                            myFirebaseAuth.Auth.setCurrentUser(null);

                            callback.onReturn(null, false, "We could not access your user information.\n Please try to sign in again");
                        }
                    });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(null, false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will submit feedback to help improve the app functionality
    //
    ////////////////////////////////////////////////////////////
    public void submitFeedback(feedbackInfo fInfo, booleanCallback callback) {

        try{
            if(fInfo == null || fInfo.user_id == null) {
                Log.w(TAG, "submitFeedback called with null parameters");

                callback.onReturn(false, "Looks like there was an error with submitting the feedback. Please try again");

            }
            else if(fInfo.feedback.equals("")) {
                Log.w(TAG, "submitFeedback called with empty feedback. No point in submitting");

                callback.onReturn(false, "We appreciate your feedback but we want to hear more before we submit it.");
            }
            else {
                //Set the date the feedback was submitted
                fInfo.date = new Date();

                //Convert feedback to database class
                firebaseFeedbackDocument feedbackData = new firebaseFeedbackDocument(fInfo);

                db.collection("feedback").add(feedbackData)
                    .addOnSuccessListener( documentReference -> {
                        Log.d(TAG, "Feedback submitted with ID: " + documentReference.getId());

                        //Set the id of the task
                        fInfo.id = documentReference.getId();

                        callback.onReturn(true, NO_ERROR);
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "Error adding feedback");

                        //Return null to indicate error in task
                        callback.onReturn(false, DATABASE_ERROR_MESSAGE);
                    });
            }
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn( false, UNKNOWN_ERROR_MESSAGE);
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will sign out the user from firebase auth
    //
    ////////////////////////////////////////////////////////////
    public void logout(userInfo uInfo, booleanCallback callback) {
        try{
            if(uInfo.id != null) {
                Log.d(TAG, "Signing out user: " + uInfo.id);
            }
            else {
                Log.w(TAG, "userId is null. User signed out anyways");
            }

            //Sign out user
            FirebaseAuth.getInstance().signOut();

            //Set current user to null
            myFirebaseAuth.Auth.setCurrentUser(null);


            //Submit success even if it failed since we set currentUser to null
            //  so the user info is gone anyways
            callback.onReturn(true, NO_ERROR);
        }
        catch(Exception e) {
            Log.e(TAG, "ERROR: Exception caught: " + e.getMessage());
            callback.onReturn(false, UNKNOWN_ERROR_MESSAGE);
        }
    }
}// END of userFirebaseClass
