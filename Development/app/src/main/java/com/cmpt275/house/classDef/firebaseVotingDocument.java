package com.cmpt275.house.classDef;

import java.util.Date;
import java.util.Map;

public class firebaseVotingDocument {
    //
    // Class Variables
    //
    private String displayName;
    private Map<String, String> task;
    private Map<String, String> voters;     //Map of {user_id, user name}
    private Map<String, String> house;      //Map of {house_id, house name}
    private int yesVotes;
    private int noVotes;
    private int type;
    private Date endOfVote;
}
