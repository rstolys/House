package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.feedbackInfo;
import com.cmpt275.house.classDef.userInfo;

public interface UsersBE {
    public void createAccount(String name, String email, String password);
    public void resetPassword(String email);
    public void modifyDisplayName(userInfo uInfo);
    public void modifyEmail(String newEmail, userInfo uInfo);
    public void updateNotificationSettings(userInfo uInfo);
    public void signInUser(String email, String password);
    public void getUserInfo(String user_id);
    public void submitFeedback(feedbackInfo fInfo);
    public void logout(userInfo uInfo);
}
