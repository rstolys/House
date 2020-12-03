package com.cmpt275.house.classDef.documentClass;

import android.util.Log;

import com.cmpt275.house.classDef.infoClass.feedbackInfo;

import java.util.Date;

public class firebaseFeedbackDocument {
    //
    // Class Variables
    //
    private String user_id;
    private String feedback;
    private Date dateAdded;

    /////////////////////////////////////////////////////////
    //
    // Firebase constructor
    //
    /////////////////////////////////////////////////////////
    public firebaseFeedbackDocument() {
    }


    /////////////////////////////////////////////////////////
    //
    // Firebase constructor 2
    //
    /////////////////////////////////////////////////////////
    public firebaseFeedbackDocument(String user_id, String feedback, Date dateAdded) {
        /*
        this.user_id = user_id;
        this.feedback = feedback;
        this.dateAdded = dateAdded;
         */
    }


    /////////////////////////////////////////////////////////
    //
    // Fill class elements based on contents of feedbackInfo class
    //
    /////////////////////////////////////////////////////////
    public firebaseFeedbackDocument(feedbackInfo fInfo) {

        this.user_id = fInfo.user_id;
        this.feedback = fInfo.feedback;
        this.dateAdded = fInfo.date;
    }


    /////////////////////////////////////////////////////////
    //
    // Generate a feedbackInfo class from contents of this class
    //
    /////////////////////////////////////////////////////////
    public feedbackInfo toFeedbackInfo(String feedback_id) {

        //Create feedbackInfo class to return to caller
        feedbackInfo returnFeedback = new feedbackInfo();

        if(feedback_id == null)
            returnFeedback.id = "";
        else
            returnFeedback.id = feedback_id;

        returnFeedback.user_id = user_id;
        returnFeedback.feedback = feedback;
        returnFeedback.date = dateAdded;

        return returnFeedback;
    }


    //
    // Getters for each element of the class
    //


    public String getUser_id() {
        return user_id;
    }

    public String getFeedback() {
        return feedback;
    }

    public Date getDateAdded() {
        return dateAdded;
    }
}
