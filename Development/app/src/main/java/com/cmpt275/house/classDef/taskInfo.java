package com.cmpt275.house.classDef;

import java.util.Date;

public class taskInfo {
    public String id;
    public String name;
    public String description;
    public String createdBy;
    public int status;
    public String[] assignedTo;         //array of displayNames
    public String houseName;
    public String house_id;
    public double costAssociated;
    public int difficultyScore;
    public Date dueDate;                 //Date in ms from epoch -- can use java Date
    public String[] itemList;
    public Date notificationTime;        //Date in ms from epoch -- can use java Date
    public Integer[] tag;

}
