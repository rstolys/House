package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.home;

class Home implements home {
    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public taskInfo[] viewTasks(int user_id) {
        return new taskInfo[0];
    }

    @Override
    public houseInfo[] viewHouses(int user_id) {
        return new houseInfo[0];
    }
}
