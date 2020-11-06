package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.settings;
import com.cmpt275.house.interfaceDef.settingsCallbacks;

class settingsClass implements settings, settingsCallbacks {

    //
    // Class Variables
    //
    private userInfo uInfo;

    //
    // Class Functions
    //
    public void logout() {
        return;
    }

    public void viewLegal() {
        return;
    }

    public void viewSettings(String user_id) {
        return;
    }

    public void changeDisplayName(String displayName) {
        return;
    }

    public void changeEmail(String email) {
        return;
    }

    public void changeNotifications(boolean allowNotifications) {
        return;
    }

    public void resetPassword(String email) {
        return;
    }

    public void provideFeedback(String feedback) {
        return;
    }


    public void onSettingsStringReturn(String str, String functionName) {return;}
    public void onUserInfoReturn(userInfo uInfo, String functionName) {return;}
    public void onSettingsBooleanReturn(boolean result, String functionName) {return;}
}
