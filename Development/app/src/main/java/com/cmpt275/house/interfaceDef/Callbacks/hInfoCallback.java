package com.cmpt275.house.interfaceDef.Callbacks;

import com.cmpt275.house.classDef.infoClass.houseInfo;

public interface hInfoCallback {
    void onReturn(houseInfo hInfo, boolean success, String errorMessage);
}
