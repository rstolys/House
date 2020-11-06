package com.cmpt275.house.classDef;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class houseInfo implements Serializable {
    public String id;
    public String name;
    public List<String> voting_ids;
    public List<String> task_ids;
    public List<String> members;                    //List of member ids
    public Map<String, String> memberRoles;         //Map of {user_id, Member Role}
    public Map<String, String> memberNames;         //Map of {user_id, Member Name}
    public String description;
    public int punishmentMultiplier;
    public int maxMembers;
    public int houseNotifications;                  //Notification type
}
