package com.cmpt275.house.classDef;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class firebaseUserDocument {
    //
    // Class Variables
    //
    private String firebase_id;
    private String displayName;
    private boolean notificationsAllowed;
    private Map<String, Double> moneyBalance;       //List of {house_id: String, value: double}
    private Map<String, String> houses;              //List of {house_id: String, houseName: String}
    private Map<String, String> tasks;               //List of {task_id: String, taskName: String}

}
