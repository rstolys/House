package com.cmpt275.house.interfaceDef.Callbacks;

import com.cmpt275.house.classDef.infoClass.taskInfo;

public interface tInfoCallback {
    void onReturn(taskInfo tInfo, boolean success, String errorMessage);

}
