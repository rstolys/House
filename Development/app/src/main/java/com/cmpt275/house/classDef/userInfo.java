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
    public Map<String, String> houses;              //List of {house_id: String, houseName: String}
    public Map<String, String> tasks;               //List of {task_id: String, taskName: String}
    public Map<String, Double> moneyBalance;        //List of {house_id: String, value: double}
}
