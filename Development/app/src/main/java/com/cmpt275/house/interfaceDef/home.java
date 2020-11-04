package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.classDef.houseInfo;
import com.cmpt275.house.classDef.taskInfo;

public interface home {
    public boolean logout();
    public taskInfo[] viewTasks(int user_id);
    public houseInfo[] viewHouses(int user_id);
}
