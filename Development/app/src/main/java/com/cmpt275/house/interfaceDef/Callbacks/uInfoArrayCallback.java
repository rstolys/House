package com.cmpt275.house.interfaceDef.Callbacks;

import com.cmpt275.house.classDef.infoClass.userInfo;

public interface uInfoArrayCallback {
    void onReturn(userInfo[] uInfos, boolean success, String errorMessage);

}
