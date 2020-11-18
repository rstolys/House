package com.cmpt275.house.classDef.documentClass;

import com.cmpt275.house.classDef.databaseObjects.voterObj;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.classDef.mappingClass.voteTypeMapping;
import com.cmpt275.house.interfaceDef.mapping;

import java.util.Date;
import java.util.Map;

public class firebaseVotingDocument {
    //
    // Class Variables
    //
    private String taskName;
    private String task_id;
    private Map<String, voterObj> voters;
    private String houseName;
    private String house_id;
    private int type;
    private Date endOfVote;


    /////////////////////////////////////////////////////////
    //
    // Firebase constructor
    //
    /////////////////////////////////////////////////////////
    public firebaseVotingDocument() {
    }


    /////////////////////////////////////////////////////////
    //
    // Firebase constructor 2
    //
    /////////////////////////////////////////////////////////
    public firebaseVotingDocument(String taskName, String task_id, Map<String, voterObj> voters, String houseName, String house_id, int type, Date endOfVote) {
        /*
        this.taskName = taskName;
        this.task_id = task_id;
        this.voters = voters;
        this.houseName = houseName;
        this.house_id = house_id;
        this.type = type;
        this.endOfVote = endOfVote;
         */
    }

    /////////////////////////////////////////////////////////
    //
    // Generate class instance with values from votingInfo class
    //
    /////////////////////////////////////////////////////////
    public firebaseVotingDocument(votingInfo vInfo) {

        this.taskName = vInfo.taskName;
        this.task_id = vInfo.task_id;
        this.houseName = vInfo.houseName;
        this.house_id = vInfo.house_id;
        this.voters = vInfo.voters;
        this.endOfVote = vInfo.endOfVote;

        //Map type from string to int
        if(vInfo.type != null) {
            mapping typeMapping = new voteTypeMapping();
            this.type = typeMapping.mapStringToInt(vInfo.type);
        }

        return;
    }

    /////////////////////////////////////////////////////////
    //
    // Generate votingInfo class from values in class
    //
    /////////////////////////////////////////////////////////
    public votingInfo toVotingInfo(String voting_id) {

        //Create votingInfo class to return to caller
        votingInfo returnVote = new votingInfo();

        returnVote.id = voting_id;
        returnVote.taskName = taskName;
        returnVote.task_id = task_id;
        returnVote.houseName = houseName;
        returnVote.house_id = house_id;
        returnVote.endOfVote = endOfVote;
        returnVote.voters = voters;

        //Update the yesVotes and noVotes based on values in voters
        if(voters == null) {
            returnVote.yesVotes = 0;
            returnVote.noVotes = 0;
        }
        else {
            for(String user_id : voters.keySet()) {
                if (voters.get(user_id).yesVote)
                    returnVote.yesVotes++;
                else
                    returnVote.noVotes++;
            }
        }

        //Map the type of vote to a string
        mapping voteMap = new voteTypeMapping();
        returnVote.type = voteMap.mapIntToString(type);


        return returnVote;
    }


    //
    // Getters for each class attribute
    //

    public String getTaskName() {
        return taskName;
    }

    public String getTask_id() {
        return task_id;
    }

    public Map<String, voterObj> getVoters() {
        return voters;
    }

    public String getHouseName() {
        return houseName;
    }

    public String getHouse_id() {
        return house_id;
    }

    public int getType() {
        return type;
    }

    public Date getEndOfVote() {
        return endOfVote;
    }
}
