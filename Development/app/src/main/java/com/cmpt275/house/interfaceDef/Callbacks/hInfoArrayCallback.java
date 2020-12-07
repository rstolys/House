package com.cmpt275.house.interfaceDef.Callbacks;

import com.cmpt275.house.classDef.infoClass.houseInfo;

public interface hInfoArrayCallback {
    void onReturn(houseInfo[] hInfos, boolean success, String errorMessage);
}
