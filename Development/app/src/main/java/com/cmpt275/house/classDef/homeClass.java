package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.home;

class homeClass implements home {
    //
    // Class Variables
    //
    private userInfo uInfo;

    //
    // Class Functions
    //
    public boolean logout() {
        return false;
    }

    public taskInfo[] viewTasks(int user_id) {
        return new taskInfo[0];
    }

    public houseInfo[] viewHouses(int user_id) {
        return new houseInfo[0];
    }
}
