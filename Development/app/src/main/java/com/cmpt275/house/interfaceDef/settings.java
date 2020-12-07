package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.interfaceDef.Callbacks.updateCallback;

public interface settings {
    void logout(updateCallback callback);
    void viewLegal();
    void viewSettings(String user_id);
    void changeDisplayName(String displayName);
    void changeEmail(String email);
    void changeNotifications(boolean allowNotifications);
    void resetPassword(String email);
    void provideFeedback(String feedback, updateCallback callback);
}
