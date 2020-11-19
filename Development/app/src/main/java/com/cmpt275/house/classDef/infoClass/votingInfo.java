package com.cmpt275.house.classDef.infoClass;

import com.cmpt275.house.classDef.databaseObjects.voterObj;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class votingInfo implements Serializable {
    public String id;
    public String taskName;
    public String task_id;
    public int yesVotes;
    public int noVotes;
    public String house_id;
    public String houseName;
    public Map<String, voterObj> voters = new HashMap<String, voterObj>();
    public String type;                     //Voting type
    public Date endOfVote;
}
