package com.cmpt275.house.classDef.documentClass;

import android.util.Log;

import com.cmpt275.house.classDef.infoClass.feedbackInfo;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class firebaseFeedbackDocumentTest {

    ////////////////////////////////////////////////////////////
    // Automated Test 21
    ////////////////////////////////////////////////////////////
    @Test
    public void testConstructor() {

        feedbackInfo fInfo = new feedbackInfo();
        fInfo.user_id = "testID";
        fInfo.feedback = "This is a unit Test";
        fInfo.date = new Date();

        //Normal Values
        firebaseFeedbackDocument feedbackDoc = new firebaseFeedbackDocument(fInfo);

        if(feedbackDoc.getDateAdded() != fInfo.date ||
                !feedbackDoc.getFeedback().equals(fInfo.feedback) ||
                !feedbackDoc.getUser_id().equals(fInfo.user_id)) {
            Assert.fail();
        }

        //Boundary Values
        boolean properException = false;
        try {
            feedbackDoc = new firebaseFeedbackDocument(null);
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
    // Automated Test 22
    ////////////////////////////////////////////////////////////
    @Test
    public void testToFeedbackInfo() {


        //Normal Values
        feedbackInfo fInfo = new feedbackInfo();
        fInfo.user_id = "testID";
        fInfo.feedback = "This is a unit Test";
        fInfo.date = new Date();
        fInfo.id = "feedbackID";

        firebaseFeedbackDocument feedbackDoc = new firebaseFeedbackDocument(fInfo);

        feedbackInfo fInfoReturned = feedbackDoc.toFeedbackInfo("feedbackID");


        if(!fInfo.id.equals(fInfoReturned.id) || !fInfo.feedback.equals(fInfoReturned.feedback) ||
                !fInfo.date.equals(fInfoReturned.date) || !fInfo.user_id.equals(fInfoReturned.user_id)) {
            Assert.fail();
        }


        //Boundary Values
        boolean properException = false;
        try {
            feedbackDoc = new firebaseFeedbackDocument(null);

            fInfoReturned = feedbackDoc.toFeedbackInfo(null);
        }
        catch (NullPointerException e) {
            properException = true;
        }
        catch (Exception e) {
            properException = false;
        }

        Assert.assertTrue(properException);
    }

}