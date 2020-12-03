package com.cmpt275.house.classDef.documentClass;

import com.cmpt275.house.classDef.infoClass.userInfo;

import org.junit.Assert;
import org.junit.Test;

public class firebaseUserDocumentTest {

    /////////////////////////////////////////////////////////
    //
    // Test constructor
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testConstructor() {

        userInfo uInfo = generateUInfo();


        firebaseUserDocument userDoc = new firebaseUserDocument(uInfo);

        if(!userDoc.getDisplayName().equals(uInfo.displayName))
            Assert.fail();

        if(userDoc.getNotificationsAllowed() != uInfo.notificationsAllowed)
            Assert.fail();

        if(!userDoc.getFirebase_id().equals(uInfo.firebase_id))
            Assert.fail();

        if(userDoc.getMoneyBalance() != uInfo.moneyBalance)
            Assert.fail();

        for(String key : userDoc.getHouses().keySet()) {
            if(!uInfo.houses.containsKey(key)) {
                Assert.fail();
                break;
            }

            if(!userDoc.getHouses().get(key).name.equals(uInfo.houses.get(key))) {
                Assert.fail();
                break;
            }
        }


        for(String key : userDoc.getTasks().keySet()) {
            if(!uInfo.tasks.containsKey(key)) {
                Assert.fail();
                break;
            }

            if(!userDoc.getTasks().get(key).name.equals(uInfo.tasks.get(key))) {
                Assert.fail();
                break;
            }
        }


        boolean properException = false;
        try {
            userDoc = new firebaseUserDocument(null);
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
    // Test conversion from class to uInfos
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testToUserInfo() {

        userInfo uInfo = generateUInfo();

        firebaseUserDocument userDoc = new firebaseUserDocument(uInfo);

        userInfo uInfoRet = userDoc.toUserInfo("userID", "test@sfu.ca");

        if(!uInfoRet.displayName.equals(uInfo.displayName))
            Assert.fail();

        if(!uInfoRet.id.equals(uInfo.id))
            Assert.fail();

        if(!uInfoRet.firebase_id.equals(uInfo.firebase_id))
            Assert.fail();

        if(uInfoRet.notificationsAllowed != uInfo.notificationsAllowed)
            Assert.fail();

        if(!uInfoRet.email.equals(uInfo.email))
            Assert.fail();

        if(!uInfoRet.houses.equals(uInfo.houses))
            Assert.fail();

        if(!uInfoRet.tasks.equals(uInfo.tasks))
            Assert.fail();

        if(!uInfoRet.moneyBalance.equals(uInfo.moneyBalance))
            Assert.fail();


        boolean properException = false;
        try {
            userDoc = new firebaseUserDocument(null);

            uInfoRet = userDoc.toUserInfo(null, null);
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
    private userInfo generateUInfo() {

        userInfo uInfo = new userInfo();
        uInfo.id = "userID";
        uInfo.displayName = "Emulator Test Task";
        uInfo.email = "test@sfu.ca";
        uInfo.firebase_id = "tesfirebaseID";
        uInfo.notificationsAllowed = true;

        uInfo.tasks.put("TaskID", "TestTask");
        uInfo.houses.put("houseID", "testHouse");
        uInfo.moneyBalance.put("houseID", 23.34);

        return uInfo;
    }
}