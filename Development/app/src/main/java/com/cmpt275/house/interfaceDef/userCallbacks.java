package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.userInfo;

public interface userCallbacks {
    public void onUserInfoArrayReturn(userInfo uInfo);
    public void onUserInfoReturn(userInfo uInfo);
    public void onUserBooleanReturn(userInfo uInfo);
}
