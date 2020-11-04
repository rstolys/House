package com.cmpt275.house.classDef;

import org.json.JSONArray;

public class userInfo {
    public String id;
    public String displayName;
    public String email;
    public boolean notificationsAllowed;
    public JSONArray houses;                //Array of {house_id: int, houseRole: int}
    public int[] tasks;                     //Array of task ids
    public JSONArray moneyBalance;          //Array of {house_id: int, value: double}
}
