package com.cmpt275.house.classDef;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class userInfo implements Serializable {
    public String id;
    public String displayName;
    public String email;
    public boolean notificationsAllowed;
    public Map<String, String> houses;            //List of {house_id: String, houseRole: String}
    public List<String> task_ids;                    //List of task ids -- will have to go get the tasks
    public Map<String, Double> moneyBalance;      //List of {house_id: String, value: double}
}
