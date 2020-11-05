package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.home;
import com.cmpt275.house.interfaceDef.houseCallbacks;
import com.cmpt275.house.interfaceDef.taskCallbacks;

class homeClass implements home, houseCallbacks, taskCallbacks {
    //
    // Class Variables
    //
    private userInfo uInfo;

    //
    // Class Functions
    //
    public void logout() {
        return;
    }

    public void viewTasks(int user_id) {
        return;
    }

    public void viewHouses(int user_id) {
        return;
    }

    //houseCallbacks
    public void onHouseInfoArrayReturn(houseInfo[] hInfos, String functionName) {return;}
    public void onHouseInfoReturn(houseInfo hInfo, String functionName) {return;}
    public void onHouseBooleanReturn(boolean result, String functionName) {return;}

    //taskCallbacks
    public void onTaskInfoArrayReturn(taskInfo[] tInfos, String functionName) {return;}
    public void onTaskInfoReturn(taskInfo tInfo, String functionName) {return;}
    public void onTaskBooleanReturn(boolean result, String functionName) {return;}
}
