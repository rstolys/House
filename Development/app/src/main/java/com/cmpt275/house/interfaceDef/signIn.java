package com.cmpt275.house.interfaceDef;

public interface signIn {
    public void createAccount(String email, String displayName, String password);
    public void forgotPassword(String email);
    public void signInUser(String email, String password);
    public boolean isUserSignedIn();
}
