package com.cmpt275.house.classDef.documentClass;

import com.cmpt275.house.classDef.databaseObjects.taskAssignObj;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.mappingClass.statusMapping;
import com.cmpt275.house.classDef.mappingClass.tagMapping;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class firebaseTaskDocumentTest {

    ////////////////////////////////////////////////////////////
    // Automated Test 25
    ////////////////////////////////////////////////////////////
    @Test
    public void testConstructor() {

        taskInfo tInfo = generateTInfo();

        //Create Mapping classes
        tagMapping tagMap = new tagMapping();
        statusMapping statusMap = new statusMapping();

        firebaseTaskDocument taskDoc = new firebaseTaskDocument(tInfo);

        if(taskDoc.getCostAssociated() != tInfo.costAssociated)
            Assert.fail();

        if(!taskDoc.getCreatedBy().equals(tInfo.createdBy))
            Assert.fail();

        if(!taskDoc.getCreatedBy_id().equals(tInfo.createdBy_id))
            Assert.fail();

        if(!taskDoc.getDescription().equals(tInfo.description))
            Assert.fail();

        if(taskDoc.getDifficultyScore() != tInfo.difficultyScore)
            Assert.fail();

        if(!taskDoc.getDisplayName().equals(tInfo.displayName))
            Assert.fail();

        if(!taskDoc.getDueDate().equals(tInfo.dueDate))
            Assert.fail();

        if(!taskDoc.getHouse_id().equals(tInfo.house_id))
            Assert.fail();

        if(!taskDoc.getHouseName().equals(tInfo.houseName))
            Assert.fail();

        if(!taskDoc.getNotificationTime().equals(tInfo.notificationTime))
            Assert.fail();

        if(taskDoc.getStatus() != statusMap.mapStringToInt(tInfo.status))
            Assert.fail();

        if(!taskDoc.getTag().equals(tagMap.mapList_StringToInt(tInfo.tag)))
            Assert.fail();


        for(int i = 0; i < taskDoc.getItemList().size(); i++) {

            if(!taskDoc.getItemList().get(i).equals(tInfo.itemList.get(i))) {
                Assert.fail();
                break;
            }
        }

        boolean properException = false;
        try {
            taskDoc = new firebaseTaskDocument(null);
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
    // Automated Test 26
    ////////////////////////////////////////////////////////////
    @Test
    public void testToTaskInfo() {

        taskInfo tInfo = generateTInfo();

        firebaseTaskDocument taskDoc = new firebaseTaskDocument(tInfo);

        taskInfo tInfoRet = taskDoc.toTaskInfo("taskID");
        
        if(tInfoRet.costAssociated != tInfo.costAssociated)
            Assert.fail();

        if(!tInfoRet.createdBy.equals(tInfo.createdBy))
            Assert.fail();

        if(!tInfoRet.createdBy_id.equals(tInfo.createdBy_id))
            Assert.fail();

        if(!tInfoRet.description.equals(tInfo.description))
            Assert.fail();

        if(tInfoRet.difficultyScore != tInfo.difficultyScore)
            Assert.fail();

        if(!tInfoRet.displayName.equals(tInfo.displayName))
            Assert.fail();

        if(!tInfoRet.dueDate.equals(tInfo.dueDate))
            Assert.fail();

        if(!tInfoRet.house_id.equals(tInfo.house_id))
            Assert.fail();

        if(!tInfoRet.houseName.equals(tInfo.houseName))
            Assert.fail();

        if(!tInfoRet.notificationTime.equals(tInfo.notificationTime))
            Assert.fail();

        if(!tInfoRet.tag.equals(tInfo.tag))
            Assert.fail();

        if(!tInfoRet.status.equals(tInfo.status))
            Assert.fail();

        for(int i = 0; i < tInfoRet.itemList.size(); i++) {

            if(!tInfoRet.itemList.get(i).equals(tInfo.itemList.get(i))) {
                Assert.fail();
                break;
            }
        }

        boolean properException = false;
        try {
            taskDoc = new firebaseTaskDocument(null);

            tInfoRet = taskDoc.toTaskInfo(null);
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
    private taskInfo generateTInfo() {
        taskInfo tInfo = new taskInfo();
        tInfo.id = "taskID";
        tInfo.displayName = "Emulator Test Task";
        tInfo.description = "I really hope this works";
        tInfo.createdBy = "Ryan Stolys";
        tInfo.createdBy_id = "NO_IDs_HAVE_BEEN_SET";

        //Set status value
        statusMapping statusMap = new statusMapping();
        tInfo.status = statusMap.NOT_COMPLETE;

        tInfo.assignedTo.put("NO_ID", new taskAssignObj("Ryan Stolys", true, false));
        tInfo.houseName = "DevHouse";
        tInfo.house_id = "NO_IDs_HAVE_BEEN_SET";
        tInfo.costAssociated = 0;
        tInfo.difficultyScore = 5;
        tInfo.dueDate = new Date();
        tInfo.itemList.add("Test1");
        tInfo.itemList.add("Test2");
        tInfo.itemList.add("Test3");
        tInfo.notificationTime = new Date();
        tInfo.tag.add("Garbage");
        tInfo.tag.add("Kitchen");
        tInfo.tag.add("Cleaning");

        return tInfo;
    }

}