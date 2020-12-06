package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.mappingClass.notificationMapping;
import com.cmpt275.house.classDef.mappingClass.roleMapping;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class houseFirebaseClassTest {

    private houseFirebaseClass firebaseHouseAction;
    private roleMapping roleMap;
    private notificationMapping notifyMap;

    @Before
    public void initTests() {
        firebaseHouseAction = new houseFirebaseClass();
        roleMap = new roleMapping();
        notifyMap = new notificationMapping();
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 37
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void createNewHouse() {
        CountDownLatch latch = new CountDownLatch(2);

        //Define house info to be created
        houseInfo hInfo = new houseInfo();
        hInfo.id = null;
        hInfo.displayName = "Unit Test House";
        hInfo.voting_ids = null;
        hInfo.tasks = null;

        hInfo.members = new HashMap<String, houseMemberInfoObj>();
        hInfo.members.put("heCbONKnDl2LVojcPVgc", new houseMemberInfoObj("Test User New Name", roleMap.ADMIN));

        hInfo.description = "This is a test house";
        hInfo.punishmentMultiplier = 5;
        hInfo.maxMembers = 4;
        hInfo.houseNotifications = notifyMap.WEEKLY;

        //Test normal operation
        firebaseHouseAction.createNewHouse(hInfo, (hInfoRet, success, errorMessage) -> {
            Log.d("createNewHouse", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });

        //Test boundary operation
        firebaseHouseAction.createNewHouse(null, (hInfoRet, success, errorMessage) -> {
            Log.d("createNewHouse", "Expected error message: " + errorMessage);

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
    // Automated Test 38
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void setUserRole() {
        CountDownLatch latch = new CountDownLatch(2);

        //Define house info to be created
        houseInfo hInfo = new houseInfo();
        hInfo.id = "nA64NO8sC8GCIP1HkEQx";
        hInfo.displayName = "Unit Test House";
        hInfo.voting_ids = null;
        hInfo.tasks = null;

        hInfo.members = new HashMap<String, houseMemberInfoObj>();
        hInfo.members.put("heCbONKnDl2LVojcPVgc", new houseMemberInfoObj("Test User New Name", roleMap.ADMIN));
        hInfo.members.put("thisUserWantsToJoin", new houseMemberInfoObj("Change User Role", roleMap.REQUEST));

        hInfo.description = "This is a test house";
        hInfo.punishmentMultiplier = 5;
        hInfo.maxMembers = 4;
        hInfo.houseNotifications = notifyMap.WEEKLY;

        //Test normal operation -- Approve this member
        firebaseHouseAction.setUserRole(hInfo, "thisUserWantsToJoin", roleMap.MEMBER, (hInfoRet, success, errorMessage) -> {
            Log.d("setUserRole", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });


        //Test boundary operation -- member does not exist
        firebaseHouseAction.setUserRole(hInfo, "thisUserDoesNotExist", roleMap.MEMBER, (hInfoRet, success, errorMessage) -> {
            Log.d("setUserRole", "Expected error message: " + errorMessage);

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
    // Automated Test 39
    ////////////////////////////////////////////////////////////
    @Test //@Ignore("Async")
    public void inviteUserToHouse() {
        CountDownLatch latch = new CountDownLatch(2);

        //Define house info to be created
        houseInfo hInfo = new houseInfo();
        hInfo.id = "m6BYJ7yePCokRcmTTvsO";
        hInfo.displayName = "Unit Test House";
        hInfo.voting_ids = null;
        hInfo.tasks = null;

        hInfo.members = new HashMap<String, houseMemberInfoObj>();
        hInfo.members.put("heCbONKnDl2LVojcPVgc", new houseMemberInfoObj("Test User New Name", roleMap.ADMIN));

        hInfo.description = "This is a test house";
        hInfo.punishmentMultiplier = 5;
        hInfo.maxMembers = 4;
        hInfo.houseNotifications = notifyMap.WEEKLY;


        //Test normal operation -- Add user to this house -- Will add developers account
        firebaseHouseAction.inviteUserToHouse("miller_solis_sanchez@sfu.ca", hInfo, "heCbONKnDl2LVojcPVgc", (hInfoRet, success, errorMessage) -> {
            Log.d("inviteUserToHouse", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });


        //Test boundary operation -- user to be added does not exist
        firebaseHouseAction.inviteUserToHouse("invalidEmail@sfu.ca", hInfo, "heCbONKnDl2LVojcPVgc", (hInfoRet, success, errorMessage) -> {
            Log.d("inviteUserToHouse", "Expected error message: " + errorMessage);

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
    // Automated Test 40
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void editSettings() {
        CountDownLatch latch = new CountDownLatch(2);

        //Define house info to be created
        houseInfo hInfo = new houseInfo();
        hInfo.id = "nA64NO8sC8GCIP1HkEQx";
        hInfo.displayName = "Unit Test House";
        hInfo.voting_ids = null;
        hInfo.tasks = null;

        hInfo.members = new HashMap<String, houseMemberInfoObj>();
        hInfo.members.put("heCbONKnDl2LVojcPVgc", new houseMemberInfoObj("Test User New Name", roleMap.ADMIN));

        hInfo.description = "New edited house description"; //"This is a test house";
        hInfo.punishmentMultiplier = 2; //5;
        hInfo.maxMembers = 3; //4;
        hInfo.houseNotifications = notifyMap.MONTHLY; //notifyMap.WEEKLY;


        //Test normal operation -- Change house settings
        firebaseHouseAction.editSettings(hInfo, false, (hInfoRet, success, errorMessage) -> {
            Log.d("editSettings", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });


        //Test boundary operation -- null house info class
        firebaseHouseAction.editSettings(null, false, (hInfoRet, success, errorMessage) -> {
            Log.d("editSettings", "Expected error message: " + errorMessage);

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
    // Automated Test 41
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void removeMember() {
        CountDownLatch latch = new CountDownLatch(2);

        //Define house info to be created
        houseInfo hInfo = new houseInfo();
        hInfo.id = "nA64NO8sC8GCIP1HkEQx";
        hInfo.displayName = "Unit Test House";
        hInfo.voting_ids = null;
        hInfo.tasks = null;

        hInfo.members = new HashMap<String, houseMemberInfoObj>();
        hInfo.members.put("heCbONKnDl2LVojcPVgc", new houseMemberInfoObj("Test User New Name", roleMap.ADMIN));
        hInfo.members.put("88gT9kFL2ffaIzQGwpHB", new houseMemberInfoObj("Jayden Cole", roleMap.MEMBER));


        hInfo.description = "New edited house description"; //"This is a test house";
        hInfo.punishmentMultiplier = 2; //5;
        hInfo.maxMembers = 3; //4;
        hInfo.houseNotifications = notifyMap.MONTHLY; //notifyMap.WEEKLY;


        //Test normal operation
        firebaseHouseAction.removeMember(hInfo, "heCbONKnDl2LVojcPVgc", "88gT9kFL2ffaIzQGwpHB", (result, errorMessage) -> {
            Log.d("removeMember", "Normal error message: " + errorMessage);

            assertTrue(result);

            latch.countDown();
        });


        //Test boundary operation -- not an admin member making request
        firebaseHouseAction.removeMember(hInfo, "88gT9kFL2ffaIzQGwpHB", "heCbONKnDl2LVojcPVgc", (result, errorMessage) -> {
            Log.d("removeMember", "Expected error message: " + errorMessage);

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
    // Automated Test 42
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void addMember() {

        CountDownLatch latch = new CountDownLatch(2);

        //Define house info to be created
        houseInfo hInfo = new houseInfo();
        hInfo.id = "nA64NO8sC8GCIP1HkEQx";
        hInfo.displayName = "Unit Test House";
        hInfo.voting_ids = null;
        hInfo.tasks = null;

        hInfo.members = new HashMap<String, houseMemberInfoObj>();
        hInfo.members.put("heCbONKnDl2LVojcPVgc", new houseMemberInfoObj("Test User New Name", roleMap.ADMIN));
        hInfo.members.put("88gT9kFL2ffaIzQGwpHB", new houseMemberInfoObj("Jayden Cole", roleMap.MEMBER));


        hInfo.description = "New edited house description"; //"This is a test house";
        hInfo.punishmentMultiplier = 2; //5;
        hInfo.maxMembers = 3; //4;
        hInfo.houseNotifications = notifyMap.MONTHLY; //notifyMap.WEEKLY;


        //Test normal operation
        firebaseHouseAction.addMember(hInfo, "88gT9kFL2ffaIzQGwpHB", roleMap.REQUEST, "Jayden Cole", (hInfoRet, success, errorMessage) -> {
            Log.d("removeMember", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });

        //Test boundary operation -- house is already full
        hInfo.maxMembers = 2;
        firebaseHouseAction.addMember(hInfo, "88gT9kFL2ffaIzQGwpHB", roleMap.REQUEST, "Jayden Cole", (hInfoRet, success, errorMessage) -> {
            Log.d("removeMember", "Expected error message: " + errorMessage);

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