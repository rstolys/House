package com.cmpt275.house.interfaceDef.Callbacks;

import com.cmpt275.house.classDef.infoClass.userInfo;

public interface uInfoCallback {
    void onReturn(userInfo uInfo, boolean success, String errorMessage);
}
