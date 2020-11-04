package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.feedbackInfo;
import com.cmpt275.house.classDef.userInfo;

public interface UsersBE {
    public void createAccount(String name, String email, String password);
    public void resetPassword(String email);
    public userInfo modifyDisplayName(userInfo uInfo);
    public userInfo modifyEmail(String newEmail, userInfo uInfo);
    public userInfo updateNotificationSettings(userInfo uInfo);
    public userInfo signInUser(String email, String password);
    public userInfo getUserInfo(int user_id);
    public boolean submitFeedback(feedbackInfo fInfo);
    public boolean logout(userInfo uInfo);
}
