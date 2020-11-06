package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.taskInfo;
import com.cmpt275.house.classDef.userInfo;

public interface settingsCallbacks {
    public void onSettingsStringReturn(String str, String functionName);
    public void onUserInfoReturn(userInfo uInfos, String functionName);
    public void onSettingsBooleanReturn(boolean result, String functionName);
}
