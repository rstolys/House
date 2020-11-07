package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.UsersBE;
import com.cmpt275.house.interfaceDef.userCallbacks;
import com.google.firebase.firestore.FirebaseFirestore;

public class userFirebaseClass implements UsersBE {

    //
    // Class Variables
    //
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private userCallbacks uCallback;
    private final String TAG = "FirebaseUserAction";

    //
    // Class Functions
    //
    public void createAccount(String name, String email, String password) {return;}
    public void resetPassword(String email) {return;}
    public void modifyDisplayName(userInfo uInfo) {return;}
    public void modifyEmail(String newEmail, userInfo uInfo) {return;}
    public void updateNotificationSettings(userInfo uInfo) {return;}
    public void signInUser(String email, String password) {return;}
    public void getUserInfo(String user_id) {return;}
    public void submitFeedback(feedbackInfo fInfo) {return;}
    public void logout(userInfo uInfo) {return;}
}
