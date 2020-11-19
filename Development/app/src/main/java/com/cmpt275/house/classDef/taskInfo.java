package com.cmpt275.house.classDef;

import com.cmpt275.house.classDef.databaseObjects.taskAssignObj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class taskInfo implements Serializable {
    public String id;
    public String displayName;
    public String description;
    public String createdBy;
    public String createdBy_id;
    public String status;
    public Map<String, taskAssignObj> assignedTo = new HashMap<String, taskAssignObj>();
    public String houseName;
    public String house_id;
    public double costAssociated;
    public int difficultyScore;
    public Date dueDate;
    public List<String> itemList = new ArrayList<String>();
    public Date notificationTime;
    public List<String> tag = new ArrayList<String>();

}
