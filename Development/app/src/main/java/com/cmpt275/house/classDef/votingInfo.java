package com.cmpt275.house.classDef;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.Map;

public class votingInfo implements Serializable {
    public String id;
    public String name;
    public String task_id;                  //Will have to go get task info
    public int yesVotes;
    public int noVotes;
    public String house_id;                 //Will have to go get house info
    public Map<String, String> voters;      //Map of {id, name}
    public String type;                     //Voting type
}
