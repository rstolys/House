package com.cmpt275.house.classDef.documentClass;

import com.cmpt275.house.classDef.databaseObjects.voterObj;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.classDef.mappingClass.voteTypeMapping;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class firebaseVotingDocumentTest {

    ////////////////////////////////////////////////////////////
    // Automated Test 29
    ////////////////////////////////////////////////////////////
    @Test
    public void testConstructor() {

        votingInfo vInfo = generateVInfo();

        //Mapping Class
        voteTypeMapping typeMap = new voteTypeMapping();

        firebaseVotingDocument votingDoc = new firebaseVotingDocument(vInfo);

        if(!votingDoc.getTaskName().equals(vInfo.taskName))
            Assert.fail();

        if(!votingDoc.getTask_id().equals(vInfo.task_id))
            Assert.fail();

        if(!votingDoc.getVoters().equals(vInfo.voters))
            Assert.fail();

        if(!votingDoc.getHouseName().equals(vInfo.houseName))
            Assert.fail();

        if(!votingDoc.getHouse_id().equals(vInfo.house_id))
            Assert.fail();

        if(votingDoc.getType() != typeMap.mapStringToInt(vInfo.type))
            Assert.fail();

        if(!votingDoc.getEndOfVote().equals(vInfo.endOfVote))
            Assert.fail();


        boolean properException = false;
        try {
            votingDoc = new firebaseVotingDocument(null);
        }
        catch (NullPointerException e) {
            properException = true;
        }
        catch (Exception e) {
            properException = false;
        }


        Assert.assertTrue(properException);
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 30
    ////////////////////////////////////////////////////////////
    @Test
    public void testToTaskInfo() {

        votingInfo vInfo = generateVInfo();

        firebaseVotingDocument votingDoc = new firebaseVotingDocument(vInfo);

        votingInfo vInfoRet = votingDoc.toVotingInfo("voteID");

        if(!vInfoRet.id.equals(vInfo.id))
            Assert.fail();

        if(!vInfoRet.task_id.equals(vInfo.task_id))
            Assert.fail();

        if(!vInfoRet.taskName.equals(vInfo.taskName))
            Assert.fail();

        if(!vInfoRet.houseName.equals(vInfo.houseName))
            Assert.fail();

        if(!vInfoRet.house_id.equals(vInfo.house_id))
            Assert.fail();

        if(vInfoRet.voters != vInfo.voters)
            Assert.fail();

        if(!vInfoRet.endOfVote.equals(vInfo.endOfVote))
            Assert.fail();

        if(!vInfoRet.type.equals(vInfo.type))
            Assert.fail();

        if(vInfoRet.noVotes != vInfo.noVotes)
            Assert.fail();

        if(vInfoRet.yesVotes != vInfo.yesVotes)
            Assert.fail();


        boolean properException = false;
        try {
            votingDoc = new firebaseVotingDocument(null);

            vInfoRet = votingDoc.toVotingInfo(null);
        }
        catch (NullPointerException e) {
            properException = true;
        }
        catch (Exception e) {
            properException = false;
        }


        Assert.assertTrue(properException);
    }



    /////////////////////////////////////////////////////////
    //
    // Generate a specific task info class to use in tests
    //
    /////////////////////////////////////////////////////////
    private votingInfo generateVInfo() {

        //Mapping Class
        voteTypeMapping typeMap = new voteTypeMapping();

        votingInfo vInfo = new votingInfo();

        vInfo.id = "voteID";
        vInfo.taskName = "testTask";
        vInfo.task_id = "taskID";
        vInfo.house_id = "houseID";
        vInfo.houseName = "houseName";
        vInfo.type = typeMap.DISPUTE_COMPLETION;
        vInfo.yesVotes = 1;
        vInfo.noVotes = 0;
        vInfo.endOfVote = new Date();
        vInfo.voters.put("userID", new voterObj("Ryan Stolys", true, true));

        return vInfo;
    }
}