package com.cmpt275.house.classDef;

import org.json.JSONArray;

import java.io.Serializable;

public class votingInfo implements Serializable {
    public String id;
    public String name;
    public int task_id;
    public int yesVotes;
    public int noVotes;
    public JSONArray house;             //Will be array of {id: int, name: String}
    public JSONArray voters;            //Will be array of {id: int, name: String}
    public String type;                 //Voting type
}
