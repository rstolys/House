package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.feedbackInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;
import com.cmpt275.house.interfaceDef.Callbacks.stringCallback;
import com.cmpt275.house.interfaceDef.Callbacks.uInfoCallback;

public interface UsersBE {
    void getUserAuthStatus(stringCallback callback);
    void createAccount(String name, String email, String password, uInfoCallback callback);
    void resetPassword(String email, booleanCallback callback);
    void modifyDisplayName(userInfo uInfo, String newName, uInfoCallback callback);
    void modifyEmail(String newEmail, userInfo uInfo, uInfoCallback callback);
    void updateNotificationSettings(userInfo uInfo, boolean newNotificationSettings, uInfoCallback callback);
    void signInUser(String email, String password, uInfoCallback callback);
    void getUserInfo(String user_id, uInfoCallback callback);
    void getUserInfo_firebaseID(String firebase_id, uInfoCallback callback);
    void submitFeedback(feedbackInfo fInfo, booleanCallback callback);
    void logout(userInfo uInfo, booleanCallback callback);
}
