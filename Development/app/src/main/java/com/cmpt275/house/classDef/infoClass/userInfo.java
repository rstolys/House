package com.cmpt275.house.classDef.infoClass;

import com.cmpt275.house.classDef.databaseObjects.nameObj;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class userInfo implements Serializable {
    public String id;
    public String displayName;
    public String email;
    public boolean notificationsAllowed;
    public Map<String, String> houses = new HashMap<String, String>();
    public Map<String, String> tasks = new HashMap<String, String>();
    public Map<String, Double> moneyBalance = new HashMap<String, Double>();
}
