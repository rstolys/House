package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.userInfo;

public interface settings {
    public boolean logout();
    public String  viewLegal();
    public userInfo viewSettings(int user_id);
    public userInfo changeDisplayName(String displayName);
    public userInfo changeEmail(String email);
    public userInfo changeNotifications(boolean allowNotifications);
    public boolean resetPassword(String email);
    public boolean provideFeedback(String feedback);
}
