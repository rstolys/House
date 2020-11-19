package com.cmpt275.house.classDef.infoClass;

import com.cmpt275.house.classDef.databaseObjects.houseMemberObj;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class houseInfo implements Serializable {
    public String id;
    public String displayName;
    public List<String> voting_ids = new ArrayList<String>();
    public Map<String, String> tasks = new HashMap<String, String>();
    public Map<String, houseMemberObj> members = new HashMap<String, houseMemberObj>();
    public String description;
    public int punishmentMultiplier;
    public int maxMembers;
    public String houseNotifications;                  //Notification type -- kept as int to allow modification of display on front end
}
