package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.houseInfo;
import com.cmpt275.house.classDef.taskInfo;

public interface home {
    public void logout();
    public void viewTasks(int user_id);
    public void viewHouses(int user_id);
}
