package com.cmpt275.house.classDef.documentClass;


import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.mappingClass.notificationMapping;
import com.cmpt275.house.classDef.mappingClass.roleMapping;

import org.junit.Assert;
import org.junit.Test;

public class firebaseHouseDocumentTest {

    /////////////////////////////////////////////////////////
    //
    // Test constructor
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testConstructor() {

        houseInfo hInfo = generateHInfo();

        //Create Mapping classes
        notificationMapping notifyMap = new notificationMapping();
        roleMapping roleMap = new roleMapping();

        firebaseHouseDocument houseDoc = new firebaseHouseDocument(hInfo);


        if(!houseDoc.getDescription().equals(hInfo.description))
            Assert.fail();

        if(!houseDoc.getDisplayName().equals(hInfo.displayName))
            Assert.fail();

        if(houseDoc.getHouseNotifications() != notifyMap.mapStringToInt(hInfo.houseNotifications))
            Assert.fail();

        if(houseDoc.getMaxMembers() != hInfo.maxMembers)
            Assert.fail();

        if(houseDoc.getPunishmentMultiplier() != hInfo.punishmentMultiplier)
            Assert.fail();


        for(String key : houseDoc.getTasks().keySet()) {
            if(!hInfo.tasks.containsKey(key)) {
                Assert.fail();
                break;
            }

            if(!houseDoc.getTasks().get(key).name.equals(hInfo.tasks.get(key))) {
                Assert.fail();
                break;
            }
        }


        for(String key : houseDoc.getMembers().keySet()) {
            if(!hInfo.members.containsKey(key)) {
                Assert.fail();
                break;
            }

            if(!houseDoc.getMembers().get(key).name.equals(hInfo.members.get(key).name)) {
                Assert.fail();
                break;
            }

            if(!houseDoc.getMembers().get(key).role.equals(roleMap.mapStringToInt(hInfo.members.get(key).role))) {
                Assert.fail();
                break;
            }
        }


        Assert.assertTrue(true);
    }


    /////////////////////////////////////////////////////////
    //
    // Test conversion from class to hInfos
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testToTaskInfo() {

        houseInfo hInfo = generateHInfo();

        firebaseHouseDocument houseDoc = new firebaseHouseDocument(hInfo);

        houseInfo hInfoRet = houseDoc.toHouseInfo("houseID");

        if(hInfoRet.punishmentMultiplier != hInfo.punishmentMultiplier)
            Assert.fail();

        if(hInfoRet.maxMembers != hInfo.maxMembers)
            Assert.fail();

        if(!hInfoRet.houseNotifications.equals(hInfo.houseNotifications))
            Assert.fail();

        if(!hInfoRet.description.equals(hInfo.description))
            Assert.fail();

        if(!hInfoRet.voting_ids.equals(hInfo.voting_ids))
            Assert.fail();

        if(!hInfoRet.tasks.equals(hInfo.tasks))
            Assert.fail();

        if(!hInfoRet.displayName.equals(hInfo.displayName))
            Assert.fail();

        if(!hInfoRet.id.equals(hInfo.id))
            Assert.fail();

        for(String key : hInfoRet.members.keySet()) {
            if(!hInfo.members.containsKey(key)) {
                Assert.fail();
                break;
            }

            if(!hInfoRet.members.get(key).name.equals(hInfo.members.get(key).name)) {
                Assert.fail();
                break;
            }

            if(!hInfoRet.members.get(key).role.equals(hInfo.members.get(key).role)) {
                Assert.fail();
                break;
            }
        }



        //All conditions are true
        Assert.assertTrue(true);
    }



    /////////////////////////////////////////////////////////
    //
    // Generate a specific task info class to use in tests
    //
    /////////////////////////////////////////////////////////
    private houseInfo generateHInfo() {
        houseInfo hInfo = new houseInfo();

        //Mapping Class
        notificationMapping notifyMap = new notificationMapping();
        roleMapping roleMap = new roleMapping();

        hInfo.id = "houseID";
        hInfo.displayName = "test House";
        hInfo.voting_ids.add("voteID");
        hInfo.tasks.put("taskID", "taskName");
        hInfo.members.put("userID", new houseMemberInfoObj("Ryan Stolys", roleMap.ADMIN));
        hInfo.description = "This is a test house";
        hInfo.punishmentMultiplier = 3;
        hInfo.maxMembers = 4;
        hInfo.houseNotifications = notifyMap.WEEKLY;

        return hInfo;
    }
}