package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.taskInfo;

public interface taskCallbacks {
    public void onTaskInfoArrayReturn(taskInfo[] tInfos, String functionName);
    public void onTaskInfoReturn(taskInfo tInfo, String functionName);
    public void onTaskBooleanReturn(boolean result, String functionName);
}
