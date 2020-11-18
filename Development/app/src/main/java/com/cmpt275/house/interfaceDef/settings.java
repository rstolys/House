package com.cmpt275.house.interfaceDef;

public interface settings {
    public void logout();
    public void viewLegal();
    public void viewSettings(String user_id);
    public void changeDisplayName(String displayName);
    public void changeEmail(String email);
    public void changeNotifications(boolean allowNotifications);
    public void resetPassword(String email);
    public void provideFeedback(String feedback);
}
