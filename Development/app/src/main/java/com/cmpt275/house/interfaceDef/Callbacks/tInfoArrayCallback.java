package com.cmpt275.house.interfaceDef.Callbacks;

import com.cmpt275.house.classDef.infoClass.taskInfo;

public interface tInfoArrayCallback {
    public void onReturn(taskInfo[] tInfos, boolean success, String errorMessage);

}
