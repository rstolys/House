package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.houseInfo;

public interface houseCallbacks {
    public void onHouseInfoArrayReturn(houseInfo[] hInfos, String functionName);
    public void onHouseInfoReturn(houseInfo hInfo, String functionName);
    public void onHouseBooleanReturn(boolean result, String functionName);
}
