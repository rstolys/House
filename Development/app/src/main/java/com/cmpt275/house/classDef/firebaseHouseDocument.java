package com.cmpt275.house.classDef;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class firebaseHouseDocument {
    //
    // Class Variables
    //
    private String displayName;
    private List<String> voting_ids;
    private Map<String, String> tasks;              //Map of {task_id, taskName}
    private Map<String, String> memberRoles;        //Map of {user_id, Member Role}
    private Map<String, String> memberNames;        //Map of {user_id, Member Name}
    private String description;
    private int punishmentMultiplier;
    private int maxMembers;
    private int houseNotifications;

}
