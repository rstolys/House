package com.cmpt275.house.classDef;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.cmpt275.house.classDef.taskInfo;

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
    private String name;
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
    public firebaseTaskDocument(Map<String, String> assignedTo, Map<String, String> createdBy, String description, int difficultyScore, double costAssociated, Date dueDate, Map<String, String> house, List<String> itemList, String name, Date notificationTime, int status, List<Integer> tag) {
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
        name = tInfo.name;
        notificationTime = tInfo.notificationTime;
        status = tInfo.status;

        tagMapping mapTags = new tagMapping();
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
        returnTask.name = name;
        returnTask.description = description;

        //There will only be 1 element in map
        /*
        createdBy.forEach((createdBy_id, createdBy) -> {
            returnTask.createdBy = createdBy;
            returnTask.createdBy_id = createdBy_id;
        });
        */


        returnTask.status = status;
        returnTask.assignedTo = assignedTo;

        //There  will only be 1 house in map
        /*
        house.forEach((house_id, houseName) -> {
            returnTask.houseName = houseName;
            returnTask.house_id = house_id;
        });
        */

        returnTask.costAssociated = costAssociated;
        returnTask.difficultyScore = difficultyScore;
        returnTask.dueDate = dueDate;
        returnTask.itemList = itemList;
        returnTask.notificationTime = notificationTime;

        tagMapping mapTags = new tagMapping();
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

    public String getName() {
        return name;
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
