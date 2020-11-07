package com.cmpt275.house.classDef;

public class userFirebaseClass {

    //
    // Class Variables
    //
    //NONE

    //
    // Class Functions
    //
    public void createAccount(String name, String email, String password) {return;}
    public void resetPassword(String email) {return;}
    public userInfo modifyDisplayName(userInfo uInfo) {return null;}
    public userInfo modifyEmail(String newEmail, userInfo uInfo) {return null;}
    public userInfo updateNotificationSettings(userInfo uInfo) {return null;}
    public userInfo signInUser(String email, String password) {return null;}
    public userInfo getUserInfo(int user_id) {return null;}
    public boolean submitFeedback(feedbackInfo fInfo) {return false;}
    public boolean logout(userInfo uInfo) {return false;}
}
