package com.cmpt275.house.classDef.documentClass;

import android.util.Log;

import com.cmpt275.house.classDef.databaseObjects.houseMemberObj;
import com.cmpt275.house.classDef.databaseObjects.nameObj;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.mappingClass.notificationMapping;
import com.cmpt275.house.classDef.mappingClass.roleMapping;
import com.cmpt275.house.interfaceDef.mapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class firebaseHouseDocument {
    //
    // Class Variables
    //
    private String displayName;
    private List<String> voting_ids;
    private Map<String, nameObj> tasks = new HashMap<String, nameObj>();              //Map of {task_id, taskName}
    private Map<String, houseMemberObj> members = new HashMap<String, houseMemberObj>();
    private String description;
    private int punishmentMultiplier;
    private int maxMembers;
    private int houseNotifications;


    /////////////////////////////////////////////////////////
    //
    // Firebase constructor
    //
    /////////////////////////////////////////////////////////
    public firebaseHouseDocument() {
    }


    /////////////////////////////////////////////////////////
    //
    // Firebase constructor 2
    //
    /////////////////////////////////////////////////////////
    public firebaseHouseDocument(String displayName, List<String> voting_ids, Map<String, nameObj> tasks, Map<String, houseMemberObj> members, String description, int punishmentMultiplier, int maxMembers, int houseNotifications) {
        /*
        this.displayName = displayName;
        this.voting_ids = voting_ids;
        this.tasks = tasks;
        this.members = members;
        this.description = description;
        this.punishmentMultiplier = punishmentMultiplier;
        this.maxMembers = maxMembers;
        this.houseNotifications = houseNotifications;
         */
    }

    /////////////////////////////////////////////////////////
    //
    // Construct instance of class with elements of houseInfo Class
    //
    /////////////////////////////////////////////////////////
    public firebaseHouseDocument(houseInfo hInfo) {

        this.displayName  = hInfo.displayName;
        this.voting_ids = hInfo.voting_ids;

        //Create the tasks mapping objects to store info
        if(hInfo.tasks == null) {
            tasks = null;
        }
        else {
            for(String task_id : hInfo.tasks.keySet()) {
                nameObj taskElement = new nameObj(hInfo.tasks.get(task_id), true);
                this.tasks.put(task_id, taskElement);
            }
        }

        if(hInfo.members != null) {
            mapping roleMap = new roleMapping();
            for(String member_id : hInfo.members.keySet()) {
                this.members.put(member_id, new houseMemberObj(hInfo.members.get(member_id).name, true, roleMap.mapStringToInt(hInfo.members.get(member_id).role)));
            }
        }
        else {
            this.members = null;
        }

        this.description = hInfo.description;
        this.punishmentMultiplier = hInfo.punishmentMultiplier;
        this.maxMembers = hInfo.maxMembers;

        if(hInfo.houseNotifications != null) {
            mapping houseNotificationMap = new notificationMapping();
            this.houseNotifications = houseNotificationMap.mapStringToInt(hInfo.houseNotifications);
        }
    }


    /////////////////////////////////////////////////////////
    //
    // Create a houseInfo class filled with info from class attributes
    //
    /////////////////////////////////////////////////////////
    public houseInfo toHouseInfo(String house_id) {

        //Create houseInfo class to return
        houseInfo returnHouse = new houseInfo();

        returnHouse.id = house_id;
        returnHouse.displayName = displayName;
        returnHouse.voting_ids = voting_ids;

        //Get task info to convert map elements
        if(tasks == null) {
            returnHouse.tasks = null;
        }
        else {
            for(String task_id : tasks.keySet()) {
                returnHouse.tasks.put(task_id, tasks.get(task_id).name);
            }
        }


        if(members != null) {
            mapping roleMap = new roleMapping();
            for(String member_id : members.keySet()) {
                returnHouse.members.put(member_id, new houseMemberInfoObj(members.get(member_id).name, roleMap.mapIntToString(members.get(member_id).role)));
            }
        }
        else {
            returnHouse.members = null;
        }

        returnHouse.description = description;
        returnHouse.punishmentMultiplier = punishmentMultiplier;
        returnHouse.maxMembers = maxMembers;

        mapping houseNotificationMap = new notificationMapping();
        returnHouse.houseNotifications = houseNotificationMap.mapIntToString(houseNotifications);

        return returnHouse;
    }


    //
    // Getters for each attribute
    //


    public String getDisplayName() {
        return displayName;
    }

    public List<String> getVoting_ids() {
        return voting_ids;
    }

    public Map<String, nameObj> getTasks() {
        return tasks;
    }

    public Map<String, houseMemberObj> getMembers() {
        return members;
    }

    public String getDescription() {
        return description;
    }

    public int getPunishmentMultiplier() {
        return punishmentMultiplier;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public int getHouseNotifications() {
        return houseNotifications;
    }
}
