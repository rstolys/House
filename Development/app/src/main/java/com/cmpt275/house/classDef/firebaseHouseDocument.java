package com.cmpt275.house.classDef;

import com.cmpt275.house.classDef.databaseObjects.houseMemberObj;
import com.cmpt275.house.classDef.databaseObjects.nameObj;

import java.util.List;
import java.util.Map;

public class firebaseHouseDocument {
    //
    // Class Variables
    //
    private String displayName;
    private List<String> voting_ids;
    private Map<String, nameObj> tasks;              //Map of {task_id, taskName}
    private Map<String, houseMemberObj> members;
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


        this.members = hInfo.members;
        this.description = hInfo.description;
        this.punishmentMultiplier = hInfo.punishmentMultiplier;
        this.maxMembers = hInfo.maxMembers;
        this.houseNotifications = hInfo.houseNotifications;


        return;
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


        returnHouse.members = members;
        returnHouse.description = description;
        returnHouse.punishmentMultiplier = punishmentMultiplier;
        returnHouse.maxMembers = maxMembers;
        returnHouse.houseNotifications = houseNotifications;

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
