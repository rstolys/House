package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.classDef.infoClass.feedbackInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class userFirebaseClassTest {

    private userFirebaseClass firebaseUserAction;

    @Before
    public void initTests() {
        firebaseUserAction = new userFirebaseClass();
    }

    ////////////////////////////////////////////////////////////
    // Automated Test 31
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void createAccount() {
        CountDownLatch latch = new CountDownLatch(2);

        firebaseUserAction.createAccount("Automated Test User", "ryanstolys@sfu.ca", "123456", (uInfo, success, errorMessage) -> {
            Log.d("createAccountTest", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });

        firebaseUserAction.createAccount("Automated Test User To Fail", "rstolys@sfu.ca", "123456", (uInfo, success, errorMessage) -> {
            Log.d("createAccountTest", "Expected error message: " + errorMessage);

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
    // Automated Test 32
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void signInUser() {
        CountDownLatch latch = new CountDownLatch(2);

        firebaseUserAction.signInUser("rstolys@sfu.ca", "066923384", (uInfo, success, errorMessage) -> {
            Log.d("signInUserTest", "Normal error message: " + errorMessage);

            assertTrue(success);

            latch.countDown();
        });

        firebaseUserAction.signInUser("badEmail@sfu.ca", "123456", (uInfo, success, errorMessage) -> {
            Log.d("signInUserTest", "Expected error message: " + errorMessage);

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
    // Automated Test 33
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void logout() {
        CountDownLatch latch = new CountDownLatch(2);

        //First signIn the user
        firebaseUserAction.signInUser("rstolys@sfu.ca", "066923384", (uInfo, success, errorMessage) -> {
            if(success) {
                //Test normal logout
                firebaseUserAction.logout(uInfo, (result, errorMessage2) -> {
                    Log.d("logoutTest", "Normal error message: " + errorMessage2);

                    assertTrue(result);

                    latch.countDown();
                });

                //Test abnormal logout --  user has already been logged out
                firebaseUserAction.logout(uInfo, (result, errorMessage2) -> {
                    Log.d("logoutTest", "Expected error message: " + errorMessage2);

                    assertTrue(result);

                    latch.countDown();
                });
            }
            else {
                fail();
                latch.countDown();
                latch.countDown();
            }


        });


        try {
            latch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 34
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void changeAccountInfo() {
        CountDownLatch latch = new CountDownLatch(2);

        userInfo uInfo = new userInfo();
        uInfo.id = "heCbONKnDl2LVojcPVgc"; //Test user generated in database
        uInfo.email = "ryanstolys@sfu.ca";
        uInfo.displayName = "Automated Test User";

        String newName = "Test User New Name";
        //Change display name
        firebaseUserAction.modifyDisplayName(uInfo, newName, (newUInfo, success, errorMessage) -> {
            Log.d("changeAccountInfo", "Display Name modified returned with: " + success);

            if(success) {
                assertEquals(newUInfo.displayName, newName);
            }
            else {
                fail();
            }

            latch.countDown();
        });


        //Call with null userInfo
        firebaseUserAction.modifyDisplayName(null, "Test User New Name", (newUInfo, success, errorMessage) -> {
            Log.d("changeAccountInfo", "Display Name modified returned with: " + success);

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
    // Automated Test 35
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void resetPassword() {
        CountDownLatch latch = new CountDownLatch(2);

        userInfo uInfo = new userInfo();
        uInfo.id = "heCbONKnDl2LVojcPVgc"; //Test user generated in database
        uInfo.email = "ryanstolys@sfu.ca";
        uInfo.displayName = "Automated Test User";

        //Send reset password email with valid email
        firebaseUserAction.resetPassword("rstolys@sfu.ca", (result, errorMessage) -> {
            Log.d("resetPassword", "returned with result: " + result);

            assertTrue(result);

            latch.countDown();
        });


        //Send reset password email with invalid email
        firebaseUserAction.resetPassword("invalidUserEmail@sfu.ca", (result, errorMessage) -> {
            Log.d("resetPassword", "returned with result: " + result);

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
    // Automated Test 36
    ////////////////////////////////////////////////////////////
    @Test @Ignore("Async")
    public void submitFeedback() {
        CountDownLatch latch = new CountDownLatch(2);

        feedbackInfo fInfo = new feedbackInfo();
        fInfo.user_id = "heCbONKnDl2LVojcPVgc"; //Test user generated in database
        fInfo.feedback = "This is test feedback to send to the database";
        fInfo.date = new Date();


        //Submit valid feedback to system
        firebaseUserAction.submitFeedback(fInfo, (result, errorMessage) -> {
            Log.d("submitFeedback", "returned with result: " + result);

            assertTrue(result);

            latch.countDown();
        });


        //Submit invalid (empty) feedback to system
        //Remove feedback
        fInfo.feedback = "";
        firebaseUserAction.submitFeedback(fInfo, (result, errorMessage) -> {
            Log.d("resetPassword", "returned with result: " + result);

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
}