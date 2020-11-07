package com.cmpt275.house.classDef;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class taskInfo implements Serializable {
    public String id;
    public String name;
    public String description;
    public String createdBy;
    public String createdBy_id;
    public int status;
    public Map<String, String> assignedTo;             //Map of: {user_id, name}
    public String houseName;
    public String house_id;
    public double costAssociated;
    public int difficultyScore;
    public Date dueDate;
    public List<String> itemList;
    public Date notificationTime;
    public List<String> tag;

}
