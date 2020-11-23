package com.cmpt275.house.classDef;

import android.util.Log;

import com.cmpt275.house.classDef.documentClass.firebaseUserDocument;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.interfaceDef.Callbacks.uInfoCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class myFirebaseAuth {

    static class Auth {
        //
        // Class Variables
        //
        private static FirebaseUser currentUser;

        //
        // Class Functions
        //
        ////////////////////////////////////////////////////////////
        //
        // Constructor
        //
        ////////////////////////////////////////////////////////////
        public Auth() {
            currentUser = null;
        }


        ////////////////////////////////////////////////////////////
        //
        // Set the current user
        //
        ////////////////////////////////////////////////////////////
        public static void setCurrentUser(FirebaseUser cUser) {
            currentUser = cUser;
        }


        ////////////////////////////////////////////////////////////
        //
        // Access the current user
        //
        ////////////////////////////////////////////////////////////
        public static FirebaseUser getCurrentUser() {
            return currentUser;
        }

    }
}
