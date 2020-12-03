package com.cmpt275.house.classDef.documentClass;

import com.cmpt275.house.classDef.databaseObjects.taskAssignObj;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.mappingClass.statusMapping;
import com.cmpt275.house.classDef.mappingClass.tagMapping;
import com.cmpt275.house.interfaceDef.mapping;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class firebaseTaskDocument {
    //
    // Class Variables
    //
    private Map<String, taskAssignObj> assignedTo;
    private String createdBy;
    private String createdBy_id;
    private String description;
    private int difficultyScore;
    private double costAssociated;
    private Date dueDate;
    private String houseName;
    private String house_id;
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
    public firebaseTaskDocument(Map<String, taskAssignObj> assignedTo, String createdBy, String createdBy_id, String description, int difficultyScore, double costAssociated, Date dueDate, String houseName, String house_id, List<String> itemList, String displayName, Date notificationTime, int status, List<Integer> tag) {
        /*
        this.assignedTo = assignedTo;
        this.createdBy = createdBy;
        this.createdBy_id = createdBy_id;
        this.description = description;
        this.difficultyScore = difficultyScore;
        this.costAssociated = costAssociated;
        this.dueDate = dueDate;
        this.houseName = houseName;
        this.house_id = house_id;
        this.itemList = itemList;
        this.displayName = displayName;
        this.notificationTime = notificationTime;
        this.status = status;
        this.tag = tag;
         */
    }

    /////////////////////////////////////////////////////////
    //
    // Constructor to fill class elements based on taskInfo class
    //
    /////////////////////////////////////////////////////////
    public firebaseTaskDocument(taskInfo tInfo) {

        //Set the values based on the task info provided
        this.displayName = tInfo.displayName;
        this.description = tInfo.description;
        this.createdBy = tInfo.createdBy;
        this.createdBy_id = tInfo.createdBy_id;

        //Map status string to int
        mapping statusMap = new statusMapping();
        this.status = statusMap.mapStringToInt(tInfo.status);

        this.assignedTo = tInfo.assignedTo;

        this.houseName = tInfo.houseName;
        this.house_id = tInfo.house_id;
        this.costAssociated = tInfo.costAssociated;
        this.difficultyScore = tInfo.difficultyScore;
        this.dueDate = tInfo.dueDate;
        this.notificationTime = tInfo.notificationTime;
        this.itemList = tInfo.itemList;

        //Map tag string to int
        mapping tagMap = new tagMapping();
        this.tag = tagMap.mapList_StringToInt(tInfo.tag);

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
        returnTask.displayName = displayName;
        returnTask.description = description;
        returnTask.createdBy = createdBy;
        returnTask.createdBy_id = createdBy_id;

        //Map status to string
        mapping statusMap = new statusMapping();
        returnTask.status = statusMap.mapIntToString(status);

        returnTask.assignedTo = assignedTo;
        returnTask.houseName = houseName;
        returnTask.house_id = house_id;
        returnTask.costAssociated = costAssociated;
        returnTask.difficultyScore = difficultyScore;
        returnTask.dueDate =  dueDate;
        returnTask.itemList = itemList;
        returnTask.notificationTime = notificationTime;

        //Map tags to string
        mapping tagMap = new tagMapping();
        returnTask.tag = tagMap.mapList_IntToString(tag);

        return returnTask;
    }


    //
    // Getter functions for firebase to access variables
    //


    public Map<String, taskAssignObj> getAssignedTo() {
        return assignedTo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedBy_id() {
        return createdBy_id;
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

    public String getHouseName() {
        return houseName;
    }

    public String getHouse_id() {
        return house_id;
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
