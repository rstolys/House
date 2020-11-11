package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.mapping;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class firebaseTaskDocument {
    //
    // Class Variables
    //
    private Map<String, String> assignedTo;
    private Map<String, String> createdBy;
    private String description;
    private int difficultyScore;
    private double costAssociated;
    private Date dueDate;
    private Map<String, String> house;
    private List<String> itemList;
    private String displayName;
    private Date notificationTime;
    private int status;
    private List<Integer> tag;



    //
    // Class Functions
    //

    /////////////////////////////////////////////////////////
    //
    // Firebase constructor
    //
    /////////////////////////////////////////////////////////
    public firebaseTaskDocument() {
        //Do nothing
    }

    /////////////////////////////////////////////////////////
    //
    // Firebase constructor 2
    //
    /////////////////////////////////////////////////////////
    public firebaseTaskDocument(Map<String, String> assignedTo, Map<String, String> createdBy, String description, int difficultyScore, double costAssociated, Date dueDate, Map<String, String> house, List<String> itemList, String displayName, Date notificationTime, int status, List<Integer> tag) {
        //Do nothing
    }


    /////////////////////////////////////////////////////////
    //
    // Constructor to fill class elements based on taskInfo class
    //
    /////////////////////////////////////////////////////////
    public firebaseTaskDocument(taskInfo tInfo) {

        //Set the values based on the task info provided
        assignedTo = tInfo.assignedTo;

        createdBy = new HashMap<>();
        createdBy.put(tInfo.createdBy_id, tInfo.createdBy);

        description = tInfo.description;
        difficultyScore = tInfo.difficultyScore;
        costAssociated = tInfo.costAssociated;
        dueDate = tInfo.dueDate;

        house = new HashMap<>();
        house.put(tInfo.house_id, tInfo.houseName);

        itemList = tInfo.itemList;
        displayName = tInfo.name;
        notificationTime = tInfo.notificationTime;
        status = tInfo.status;

        mapping mapTags = new tagMapping();
        tag = mapTags.mapList_StringToInt(tInfo.tag);

        return;
    }

    /////////////////////////////////////////////////////////
    //
    // Will create a taskInfo class from the contents of this class
    //
    /////////////////////////////////////////////////////////
    public taskInfo toTaskInfo(String task_id) {
        //Define new taskInfo class
        taskInfo returnTask = new taskInfo();

        returnTask.id = task_id;
        returnTask.name = displayName;
        returnTask.description = description;

        //There will only be 1 element in map
        for(Map.Entry<String, String> entry : createdBy.entrySet()) {
            returnTask.createdBy_id = entry.getKey();
            returnTask.createdBy = entry.getValue();
        }

        returnTask.status = status;
        returnTask.assignedTo = assignedTo;

        //There  will only be 1 house in map
        for(Map.Entry<String, String> entry : house.entrySet()) {
            returnTask.house_id = entry.getKey();
            returnTask.houseName = entry.getValue();
        }

        returnTask.costAssociated = costAssociated;
        returnTask.difficultyScore = difficultyScore;
        returnTask.dueDate = dueDate;
        returnTask.itemList = itemList;
        returnTask.notificationTime = notificationTime;

        mapping mapTags = new tagMapping();
        returnTask.tag = mapTags.mapList_IntToString(tag);


        return returnTask;
    }


    //
    // Getter functions for firebase to access variables
    //


    public Map<String, String> getAssignedTo() {
        return assignedTo;
    }

    public Map<String, String> getCreatedBy() {
        return createdBy;
    }

    public String getDescription() {
        return description;
    }

    public int getDifficultyScore() {
        return difficultyScore;
    }

    public double getCostAssociated() {
        return costAssociated;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Map<String, String> getHouse() {
        return house;
    }

    public List<String> getItemList() {
        return itemList;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Date getNotificationTime() {
        return notificationTime;
    }

    public int getStatus() {
        return status;
    }

    public List<Integer> getTag() {
        return tag;
    }
}
