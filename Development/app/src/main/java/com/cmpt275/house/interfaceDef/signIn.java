package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.userInfo;

public interface signIn {
    public void createAccount(userInfo uInfo);
    public void forgotPassword(String email);
    public void signInUser(String email, String password);
    public boolean isUserSignedIn();
}
