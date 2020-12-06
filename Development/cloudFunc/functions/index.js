//////////////////////////////////////////////////////////////////
// index.js -- invite user functionality included
//
// Ryan Stolys - Dec 5, 2020 
//  - File created, version 1.0 developed
//
// Copyright © Ryan Stolys - All Rights Resevered
//////////////////////////////////////////////////////////////////

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();



////////////////////////////////////////////////////////////
//
// Get user emails from 
//
// data for user is JSON of user email
//
////////////////////////////////////////////////////////////
exports.inviteUser = functions.https.onCall(async (data, context) => {
    //Response to send back to the client
    var response = {success: false, userID: "", userDisplayName: "", errorMessage: ""};

    try{
        console.log("inviteUser: Called by " + context.auth.uid);
        console.log("inviteUser: Inviting user with email " + data.email);

        //Check if email is in system
        try {
            const userRecord = await admin.auth().getUserByEmail(data.email)
            //User does exist in system, get userID to return to system 
            const userDocs = await admin.firestore().collection("users").where("firebase_id", "==", userRecord.toJSON().uid).get();

            //Will only be 1 user document
            userDocs.forEach(function(user) {
                response.success = true;
                response.userID = user.id;
                response.userDisplayName = user.data().displayName;
                response.errorMessage = "";
            });
        }
        catch(error) {
            console.log("inviteUser: Fetching email threw error: " + error.message);

            //This likely means the email doesn't exist
            response.success = false;
            response.userID = "";
            response.userDisplayName = "";
            response.errorMessage = "Looks like that user does not exist. Try adding a someone else.";
        }
    }
    catch(error) {
        console.log("ERROR: Exception caught: " + error.message);

        //Set response to send to client
        response.success = false;
        response.userID = "";
        response.userDisplayName = "";
        response.errorMessage = "Looks like something went wrong on our end. Sorry about that!";
    }

    return response;
  });
