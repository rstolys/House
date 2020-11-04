package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.settings;

class Settings implements settings {
    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public String viewLegal() {
        return null;
    }

    @Override
    public userInfo viewSettings(int user_id) {
        return null;
    }

    @Override
    public userInfo changeDisplayName(String displayName) {
        return null;
    }

    @Override
    public userInfo changeEmail(String email) {
        return null;
    }

    @Override
    public userInfo changeNotifications(boolean allowNotifications) {
        return null;
    }

    @Override
    public boolean resetPassword(String email) {
        return false;
    }

    @Override
    public boolean provideFeedback(String feedback) {
        return false;
    }
}
