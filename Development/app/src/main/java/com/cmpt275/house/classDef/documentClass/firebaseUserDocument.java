package com.cmpt275.house.classDef.documentClass;

import com.cmpt275.house.classDef.databaseObjects.nameObj;
import com.cmpt275.house.classDef.infoClass.userInfo;

import java.util.Map;

public class firebaseUserDocument {
    //
    // Class Variables
    //
    private String firebase_id;
    private String displayName;
    private boolean notificationsAllowed;
    private Map<String, Double> moneyBalance;       //Map of {house_id: String, value: double}
    private Map<String, nameObj> houses;
    private Map<String, nameObj> tasks;


    /////////////////////////////////////////////////////////
    //
    // Firebase constructor
    //
    /////////////////////////////////////////////////////////
    public firebaseUserDocument() {}

    /////////////////////////////////////////////////////////
    //
    // Firebase constructor 2
    //
    /////////////////////////////////////////////////////////
    public firebaseUserDocument(String firebase_id, String displayName, boolean notificationsAllowed, Map<String, Double> moneyBalance, Map<String, nameObj> houses, Map<String, nameObj> tasks) {
        /*
        this.firebase_id = firebase_id;
        this.displayName = displayName;
        this.notificationsAllowed = notificationsAllowed;
        this.moneyBalance = moneyBalance;
        this.houses = houses;
        this.tasks = tasks;
         */
    }

    /////////////////////////////////////////////////////////
    //
    // Constructor to fill class elements based on userInfo class
    //
    /////////////////////////////////////////////////////////
    public firebaseUserDocument(userInfo uInfo) {

        this.firebase_id = uInfo.firebase_id;
        this.displayName = uInfo.displayName;
        this.notificationsAllowed = uInfo.notificationsAllowed;

        //Convert houses maps to objects for database
        if(uInfo.houses == null) {
            houses = null;
        }
        else {
            for(String house_id : uInfo.houses.keySet()) {
                nameObj houseName = new nameObj(uInfo.houses.get(house_id), true);
                this.houses.put(house_id, houseName);
            }
        }


        //Convert task maps to objects for database
        if(uInfo.tasks == null) {
            tasks = null;
        }
        else {
            for(String task_id : uInfo.tasks.keySet()) {
                nameObj taskName = new nameObj(uInfo.tasks.get(task_id), true);
                this.tasks.put(task_id, taskName);
            }
        }

        this.moneyBalance = uInfo.moneyBalance;
    }


    /////////////////////////////////////////////////////////
    //
    // Will create a userInfo class from the contents of this class
    //
    /////////////////////////////////////////////////////////
    public userInfo toUserInfo(String user_id, String email) {

        //Create userInfo class to return to caller
        userInfo returnUser = new userInfo();

        returnUser.id = user_id;
        returnUser.firebase_id = firebase_id;
        returnUser.email = email;
        returnUser.displayName = displayName;
        returnUser.notificationsAllowed = notificationsAllowed;

        //Create house map from name object
        if(houses == null) {
            returnUser.houses = null;
        }
        else {
            for(String house_id : houses.keySet()) {
                returnUser.houses.put(house_id, houses.get(house_id).name);
            }
        }


        //Create task map from name object
        if(tasks == null) {
            returnUser.tasks = null;
        }
        else {
            for(String task_id : tasks.keySet()) {
                returnUser.tasks.put(task_id, tasks.get(task_id).name);
            }
        }

        returnUser.moneyBalance = moneyBalance;

        return returnUser;
    }


    //
    // Getters for each element
    //


    public String getFirebase_id() {
        return firebase_id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean getNotificationsAllowed() {
        return notificationsAllowed;
    }

    public Map<String, Double> getMoneyBalance() {
        return moneyBalance;
    }

    public Map<String, nameObj> getHouses() {
        return houses;
    }

    public Map<String, nameObj> getTasks() {
        return tasks;
    }
}
