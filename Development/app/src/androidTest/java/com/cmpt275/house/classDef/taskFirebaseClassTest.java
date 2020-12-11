package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.classDef.databaseObjects.taskAssignObj;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.mappingClass.notificationMapping;
import com.cmpt275.house.classDef.mappingClass.roleMapping;
import com.cmpt275.house.classDef.mappingClass.statusMapping;
import com.cmpt275.house.classDef.mappingClass.tagMapping;
import com.cmpt275.house.classDef.mappingClass.voteTypeMapping;
import com.cmpt275.house.interfaceDef.mapping;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class taskFirebaseClassTest {

    private taskFirebaseClass firebaseTaskAction;
    private tagMapping tagMap;
    private statusMapping statusMap;
    private voteTypeMapping voteMap;

    @Before
    public void initTests() {
        firebaseTaskAction = new taskFirebaseClass();
        tagMap = new tagMapping();
        statusMap = new statusMapping();
        voteMap = new voteTypeMapping();
    }

    ////////////////////////////////////////////////////////////
    // Automated Test 43
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void createTask() {
        CountDownLatch latch = new CountDownLatch(2);

        taskInfo tInfo = new taskInfo();
        tInfo.id = null;
        tInfo.displayName = "Unit Test Task";
        tInfo.description = "This is the task created from the unit test";
        tInfo.createdBy = "Test User New Name";
        tInfo.createdBy_id = "heCbONKnDl2LVojcPVgc";

        //Set status value
        tInfo.status = statusMap.NOT_COMPLETE;

        //Assign task to someone else -- approved is false meaning they have to approve it
        tInfo.assignedTo.put("w4OFKQrvL28T3WlXVP4X", new taskAssignObj("Ryan Stolys", true, false));
        tInfo.houseName = "Unit Test House";
        tInfo.house_id = "m6BYJ7yePCokRcmTTvsO";
        tInfo.costAssociated = 0;
        tInfo.difficultyScore = 5;
        tInfo.dueDate = new Date();
        tInfo.itemList.add("Test1");
        tInfo.itemList.add("Test2");
        tInfo.itemList.add("Test3");
        tInfo.notificationTime = new Date();
        tInfo.tag.add(tagMap.GARBAGE);
        tInfo.tag.add(tagMap.KITCHEN);
        tInfo.tag.add(tagMap.CLEANING);


        //Normal Behaviour
        firebaseTaskAction.createTask(tInfo, (tInfoRet, success, errorMessage) -> {
            Log.d("createTask", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });

        //Test boundary condition -- null tInfo
        firebaseTaskAction.createTask(null, (tInfoRet, success, errorMessage) -> {
            Log.d("createTask", "Expected error message: " + errorMessage);

            assertFalse(success);

            latch.countDown();
        });


        try {
            latch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 44
    ////////////////////////////////////////////////////////////
    @Test //@Ignore("Async")
    public void testCompleteTask() {
        CountDownLatch latch = new CountDownLatch(2);

        //Define task info to be completed
        taskInfo tInfo = new taskInfo();

        tInfo.id = "CnxLWmFdwbIRS9mRySSI";
        tInfo.displayName = "Unit Test Task";
        tInfo.description = "This is the task created from the unit test";
        tInfo.createdBy = "Test User New Name";
        tInfo.createdBy_id = "heCbONKnDl2LVojcPVgc";

        //Set status value
        tInfo.status = statusMap.COMPLETED;     //***Change status to completed

        tInfo.assignedTo.put("heCbONKnDl2LVojcPVgc", new taskAssignObj("Test User New Name", true, false));
        tInfo.houseName = "Unit Test House";
        tInfo.house_id = "m6BYJ7yePCokRcmTTvsO";
        tInfo.costAssociated = 0;
        tInfo.difficultyScore = 5;
        tInfo.dueDate = new Date();
        tInfo.itemList.add("Test1");
        tInfo.itemList.add("Test2");
        tInfo.itemList.add("Test3");
        tInfo.notificationTime = new Date();
        tInfo.tag.add(tagMap.GARBAGE);
        tInfo.tag.add(tagMap.KITCHEN);
        tInfo.tag.add(tagMap.CLEANING);


        //Test normal operation
        firebaseTaskAction.setTaskInfo(tInfo, "status", (tInfoRet, success, errorMessage) -> {
            Log.d("setTaskInfo", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });


        //Test boundary operation

        //Set status value
        tInfo.id = null;
        firebaseTaskAction.setTaskInfo(tInfo, "status", (tInfoRet, success, errorMessage) -> {
            Log.d("setTaskInfo", "Expected error message: " + errorMessage);

            assertFalse(success);

            latch.countDown();
        });


        try {
            latch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 45
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void setTaskInfo() {
        CountDownLatch latch = new CountDownLatch(2);

        //Define task info to be completed
        taskInfo tInfo = new taskInfo();

        tInfo.id = "CnxLWmFdwbIRS9mRySSI";      //Same id as before
        tInfo.displayName = "Unit Test Task - edit name";
        tInfo.description = "This is the task created from the unit test - now editted";
        tInfo.createdBy = "Test User New Name";
        tInfo.createdBy_id = "heCbONKnDl2LVojcPVgc";

        //Set status value
        tInfo.status = statusMap.COMPLETED;     //***Change status to completed

        tInfo.assignedTo.put("heCbONKnDl2LVojcPVgc", new taskAssignObj("Test User New Name", true, false));
        tInfo.houseName = "Unit Test House";
        tInfo.house_id = "m6BYJ7yePCokRcmTTvsO";
        tInfo.costAssociated = 10;//0;
        tInfo.difficultyScore = 2;//5;
        tInfo.dueDate = new Date();
        tInfo.itemList.add("Test1");
        tInfo.itemList.add("Test3");
        tInfo.notificationTime = new Date();
        tInfo.tag.add(tagMap.GROCERIES);
        tInfo.tag.add(tagMap.GARAGE);
        tInfo.tag.add(tagMap.COOKING);

        //Test normal operation
        firebaseTaskAction.setTaskInfo(tInfo, false, "", (tInfoRet, success, errorMessage) -> {
            Log.d("setTaskInfo", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });


        //Test boundary operation -- null taskInfo
        firebaseTaskAction.setTaskInfo(null, false, "", (tInfoRet, success, errorMessage) -> {
            Log.d("setTaskInfo", "Expected error message: " + errorMessage);

            assertFalse(success);

            latch.countDown();
        });


        try {
            latch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    ////////////////////////////////////////////////////////////
    // Automated Test 46
    ////////////////////////////////////////////////////////////
    @Test //@Ignore("Async")
    public void deleteTask() {
        CountDownLatch latch = new CountDownLatch(2);

        //Define task info to be completed
        taskInfo tInfo = new taskInfo();

        tInfo.id = "cSqwJyUv9VSATXQLvEt9";
        tInfo.displayName = "Unit Test Task";
        tInfo.description = "This is the task created from the unit test";
        tInfo.createdBy = "Test User New Name";
        tInfo.createdBy_id = "heCbONKnDl2LVojcPVgc";

        //Set status value
        tInfo.status = statusMap.NOT_COMPLETE;

        tInfo.assignedTo.put("heCbONKnDl2LVojcPVgc", new taskAssignObj("Test User New Name", true, false));
        tInfo.houseName = "Unit Test House";
        tInfo.house_id = "m6BYJ7yePCokRcmTTvsO";
        tInfo.costAssociated = 0;
        tInfo.difficultyScore = 5;
        tInfo.dueDate = new Date();
        tInfo.itemList.add("Test1");
        tInfo.itemList.add("Test2");
        tInfo.itemList.add("Test3");
        tInfo.notificationTime = new Date();
        tInfo.tag.add(tagMap.GARBAGE);
        tInfo.tag.add(tagMap.KITCHEN);
        tInfo.tag.add(tagMap.CLEANING);

        //Test normal operation
        firebaseTaskAction.deleteTask(tInfo,  (result, errorMessage) -> {
            Log.d("deleteTask", "Normal error message: " + errorMessage);

            assertTrue(result);

            latch.countDown();
        });


        //Test boundary operation -- null task id
        tInfo.id = null;
        firebaseTaskAction.deleteTask(tInfo,  (result, errorMessage) -> {
            Log.d("deleteTask", "Expected error message: " + errorMessage);

            assertFalse(result);

            latch.countDown();
        });


        try {
            latch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 47
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void approveTaskAssignment() {
        CountDownLatch latch = new CountDownLatch(2);

        //Define task info to be completed
        taskInfo tInfo = new taskInfo();

        tInfo.id = "GizR2d6auiLlN4VgKVee";
        tInfo.displayName = "Unit Test Task";
        tInfo.description = "This is the task created from the unit test";
        tInfo.createdBy = "Test User New Name";
        tInfo.createdBy_id = "heCbONKnDl2LVojcPVgc";

        //Set status value
        tInfo.status = statusMap.COMPLETED;     //***Change status to completed

        //Set task approval to true
        tInfo.assignedTo.put("w4OFKQrvL28T3WlXVP4X", new taskAssignObj("Ryan Stolys", true, true));
        tInfo.houseName = "Unit Test House";
        tInfo.house_id = "m6BYJ7yePCokRcmTTvsO";
        tInfo.costAssociated = 0;
        tInfo.difficultyScore = 5;
        tInfo.dueDate = new Date();
        tInfo.itemList.add("Test1");
        tInfo.itemList.add("Test2");
        tInfo.itemList.add("Test3");
        tInfo.notificationTime = new Date();
        tInfo.tag.add(tagMap.GARBAGE);
        tInfo.tag.add(tagMap.KITCHEN);
        tInfo.tag.add(tagMap.CLEANING);


        //Test normal operation
        firebaseTaskAction.setTaskInfo(tInfo, "assignedTo", (tInfoRet, success, errorMessage) -> {
            Log.d("setTaskInfo", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });


        //Test boundary operation -- null task info
        firebaseTaskAction.setTaskInfo(null, "assignedTo", (tInfoRet, success, errorMessage) -> {
            Log.d("setTaskInfo", "Expected error message: " + errorMessage);

            assertFalse(success);

            latch.countDown();
        });


        try {
            latch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 48
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void requestExtension() {
        CountDownLatch latch = new CountDownLatch(2);

        //Define task info to be completed
        taskInfo tInfo = new taskInfo();

        tInfo.id = "GizR2d6auiLlN4VgKVee";
        tInfo.displayName = "Unit Test Task";
        tInfo.description = "This is the task created from the unit test";
        tInfo.createdBy = "Test User New Name";
        tInfo.createdBy_id = "heCbONKnDl2LVojcPVgc";

        //Set status value
        tInfo.status = statusMap.COMPLETED;     //***Change status to completed

        //Set task approval to true
        tInfo.assignedTo.put("w4OFKQrvL28T3WlXVP4X", new taskAssignObj("Ryan Stolys", true, true));
        tInfo.houseName = "Unit Test House";
        tInfo.house_id = "m6BYJ7yePCokRcmTTvsO";
        tInfo.costAssociated = 0;
        tInfo.difficultyScore = 5;

        //                                      ms/day * 7days
        long newDate = (new Date().getTime()) + 86400000*7;
        tInfo.dueDate = new Date(newDate);
        tInfo.itemList.add("Test1");
        tInfo.itemList.add("Test2");
        tInfo.itemList.add("Test3");
        tInfo.notificationTime = new Date();
        tInfo.tag.add(tagMap.GARBAGE);
        tInfo.tag.add(tagMap.KITCHEN);
        tInfo.tag.add(tagMap.CLEANING);


        //Test normal operation
        firebaseTaskAction.requestExtension(tInfo, (tInfoRet, success, errorMessage) -> {
            Log.d("requestExtension", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });


        //Test boundary operation -- null task info
        firebaseTaskAction.requestExtension(null, (tInfoRet, success, errorMessage) -> {
            Log.d("requestExtension", "Expected error message: " + errorMessage);

            assertFalse(success);

            latch.countDown();
        });


        try {
            latch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 49
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void viewPreviousTasks() {
        CountDownLatch latch = new CountDownLatch(2);

        //Define task info to be completed
        houseInfo hInfo = new houseInfo();
        hInfo.id = "m6BYJ7yePCokRcmTTvsO";
            //Only info needed for house

        //Test normal operation
        firebaseTaskAction.getCurrentTasks(hInfo, (tInfoArrayRet, success, errorMessage) -> {
            Log.d("getCurrentTasks", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });


        //Test boundary operation -- null house id
        hInfo.id = null;
        firebaseTaskAction.getCurrentTasks(hInfo, (tInfoArrayRet, success, errorMessage) -> {
            Log.d("getCurrentTasks", "Expected error message: " + errorMessage);

            assertFalse(success);

            latch.countDown();
        });


        try {
            latch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 50
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void disputeTask() {
        CountDownLatch latch = new CountDownLatch(2);

        //Define task info to be completed
        taskInfo tInfo = new taskInfo();

        tInfo.id = "GizR2d6auiLlN4VgKVee";
        tInfo.displayName = "Unit Test Task";
        tInfo.description = "This is the task created from the unit test";
        tInfo.createdBy = "Test User New Name";
        tInfo.createdBy_id = "heCbONKnDl2LVojcPVgc";

        //Set status value
        tInfo.status = statusMap.COMPLETED;     //***Change status to completed

        //Set task approval to true
        tInfo.assignedTo.put("w4OFKQrvL28T3WlXVP4X", new taskAssignObj("Ryan Stolys", true, true));
        tInfo.houseName = "Unit Test House";
        tInfo.house_id = "m6BYJ7yePCokRcmTTvsO";
        tInfo.costAssociated = 0;
        tInfo.difficultyScore = 5;

        tInfo.dueDate = new Date();
        tInfo.itemList.add("Test1");
        tInfo.itemList.add("Test2");
        tInfo.itemList.add("Test3");
        tInfo.notificationTime = new Date();
        tInfo.tag.add(tagMap.GARBAGE);
        tInfo.tag.add(tagMap.KITCHEN);
        tInfo.tag.add(tagMap.CLEANING);


        //Test normal operation
        firebaseTaskAction.disputeTask(tInfo, (tInfoRet, success, errorMessage) -> {
            Log.d("disputeTask", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });


        //Test boundary operation -- null task info
        firebaseTaskAction.disputeTask(null, (tInfoRet, success, errorMessage) -> {
            Log.d("disputeTask", "Expected error message: " + errorMessage);

            assertFalse(success);

            latch.countDown();
        });


        try {
            latch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }





}