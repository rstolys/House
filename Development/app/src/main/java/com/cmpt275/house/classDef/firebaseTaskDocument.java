package com.cmpt275.house.classDef;

import com.cmpt275.house.classDef.taskInfo;

import java.util.Date;
import java.util.Arrays;
import java.util.List;

public class firebaseTaskDocument {
    //
    // Class Variables
    //
    private List<String> assignedTo;
    private double costAssociated;
    private String createdBy;
    private String description;
    private int difficultyScore;
    private Date dueDate;
    private String house_id;
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
    public firebaseTaskDocument(List<String> assignedTo, double costAssociated, int createdBy, String description, int difficultyScore, Date dueDate, int house_id, List<String> itemList, String name, Date notificationTime, int status, List<Integer> tag) {
        //Do nothing
    }



    /////////////////////////////////////////////////////////
    //
    // Constructor to fill class elements based on taskInfo class
    //
    /////////////////////////////////////////////////////////
    public firebaseTaskDocument(taskInfo tInfo) {
        //Set the values based on the task info provided

        //set assignedTo
        if(tInfo.assignedTo.length > 0)
            assignedTo = Arrays.asList(tInfo.assignedTo);
        else
            assignedTo = null;

        //Set costAssociated, createdBy, description and difficultyScore
        costAssociated = tInfo.costAssociated;
        createdBy = tInfo.createdBy;
        description = tInfo.description;
        difficultyScore = tInfo.difficultyScore;

        //Set the dueDate, house_id
        dueDate = tInfo.dueDate;
        house_id = tInfo.house_id;

        //Set the item list
        if(tInfo.itemList.length > 0)
            itemList = Arrays.asList(tInfo.itemList);
        else
            itemList = null;

        //Set the name, notificationTime and status
        name = tInfo.name;
        notificationTime = tInfo.notificationTime;
        status = tInfo.status;

        //Set the tag list
        if(tInfo.tag.length > 0)
            tag = Arrays.asList(tInfo.tag);
        else
            tag = null;


        return;
    }

    //
    // Getter functions for firebase to access variables
    //


    public List<String> getAssignedTo() {
        return assignedTo;
    }

    public double getCostAssociated() {
        return costAssociated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getDescription() {
        return description;
    }

    public int getDifficultyScore() {
        return difficultyScore;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getHouse_id() {
        return house_id;
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
