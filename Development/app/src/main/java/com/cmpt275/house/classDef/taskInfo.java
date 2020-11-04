package com.cmpt275.house.classDef;

public class taskInfo {
    public int id;
    public String name;
    public String description;
    public String createdBy;
    public int status;
    public String[] assignedTo;         //array of displayNames
    public String houseName;
    public double costAssociated;
    public boolean taskHasCost;
    public int difficultyScore;
    public int dueDate;                 //Date in ms from epoch -- can use java Date
    public String[] itemList;
    public int notificationTime;        //Date in ms from epoch -- can use java Date
    public String tag;
}
