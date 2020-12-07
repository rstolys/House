package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.interfaceDef.Callbacks.updateCallback;

public interface signIn {
    void createAccount(String displayName, String email,  String password);
    void forgotPassword(String email);
    void signInUser(String email, String password, updateCallback callback);
    boolean isUserSignedIn();
    void checkAuthStatus(updateCallback callback);
}
