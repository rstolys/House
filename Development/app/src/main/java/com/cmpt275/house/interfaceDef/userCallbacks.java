package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.userInfo;

public interface userCallbacks {
    public void onUserInfoArrayReturn(userInfo[] uInfos, String functionName);
    public void onUserInfoReturn(userInfo uInfo, String functionName);
    public void onUserBooleanReturn(userInfo uInfo, String functionName);
}
