package com.cmpt275.house.classDef;

import org.json.JSONArray;

public class houseInfo {
    public int id;
    public String name;
    public int[] voting_ids;
    public int[] task_ids;
    public JSONArray members;
    public String description;
    public int punishmentMultiplier;
    public int maxMembers;
    public int houseNotifications;          //Notification type
}
