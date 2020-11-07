package com.cmpt275.house.classDef;

import org.json.JSONArray;

import java.io.Serializable;

public class houseInfo implements Serializable {
    public String id;
    public String name;
    public String[] voting_ids;
    public String[] task_ids;
    public JSONArray members;
    public String description;
    public int punishmentMultiplier;
    public int maxMembers;
    public int houseNotifications;          //Notification type
}
