package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.settings;

class settingsClass implements settings {

    //
    // Class Variables
    //
    private userInfo uInfo;

    //
    // Class Functions
    //
    public boolean logout() {
        return false;
    }

    public String viewLegal() {
        return null;
    }

    public userInfo viewSettings(int user_id) {
        return null;
    }

    public userInfo changeDisplayName(String displayName) {
        return null;
    }

    public userInfo changeEmail(String email) {
        return null;
    }

    public userInfo changeNotifications(boolean allowNotifications) {
        return null;
    }

    public boolean resetPassword(String email) {
        return false;
    }

    public boolean provideFeedback(String feedback) {
        return false;
    }
}
