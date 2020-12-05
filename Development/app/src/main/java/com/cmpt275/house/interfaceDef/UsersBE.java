package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.feedbackInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.stringCallback;
import com.cmpt275.house.interfaceDef.Callbacks.uInfoCallback;

//TODO: Update documentation to match new interface
public interface UsersBE {
    public void getUserAuthStatus(stringCallback callback);
    public void createAccount(String name, String email, String password, uInfoCallback callback);
    public void resetPassword(String email, booleanCallback callback);
    public void modifyDisplayName(userInfo uInfo, String newName, uInfoCallback callback);
    public void modifyEmail(String newEmail, userInfo uInfo, uInfoCallback callback);
    public void updateNotificationSettings(userInfo uInfo, boolean newNotificationSettings, uInfoCallback callback);
    public void signInUser(String email, String password, uInfoCallback callback);
    public void getUserInfo(String user_id, uInfoCallback callback);
    public void getUserInfo_firebaseID(String firebase_id, uInfoCallback callback);
    public void submitFeedback(feedbackInfo fInfo, booleanCallback callback);
    public void logout(userInfo uInfo, booleanCallback callback);
}
