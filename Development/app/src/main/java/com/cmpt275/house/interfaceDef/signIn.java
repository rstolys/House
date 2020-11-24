package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.infoClass.userInfo;

public interface signIn {
    public void createAccount(String displayName, String email,  String password);
    public void forgotPassword(String email);
    public void signInUser(String email, String password);
    public boolean isUserSignedIn();
}
