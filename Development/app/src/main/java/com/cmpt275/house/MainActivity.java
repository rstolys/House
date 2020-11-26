package com.cmpt275.house;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.notifications;
import com.cmpt275.house.classDef.settingsClass;
import com.cmpt275.house.classDef.signInClass;
import com.cmpt275.house.classDef.taskClass;
import com.cmpt275.house.interfaceDef.house;
import com.cmpt275.house.interfaceDef.settings;
import com.cmpt275.house.interfaceDef.task;
import com.cmpt275.house.interfaceDef.updateUI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements updateUI {

    private signInClass auth = new signInClass(this, this);
    private task taskAction = new taskClass(this);
    private house houseAction = new houseClass(this);
    private settings settingsAction = new settingsClass(this);
    private displayMessage display = new displayMessage();
    private notifications notify = new notifications();

    private String userInfoString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create notification channel
        notify.createNotificationChannel(this);

        //Check if the user is already signed in
        //auth.checkAuthStatus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Will be called after onCreate -- activity is now visible to the user
        // Should contain final preparations before becoming interactive
    }

    @Override
    protected void onResume() {
        super.onResume();
        //App will capture all of the users input
        // Most the core functionality should be implemented here (of the signIn Page)
        // onPause will always follow onResume()
    }

    @Override
    protected void onPause() {
        super.onPause();
        //App has lost focus and entered the paused state
        //Occurs when user taps back or recents button
        //Do NOT save application user data, make network calls or execute database transactions from here

        //Next callback will either be onResume() or onStop()
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Our activity is no longer visible to the user
        //Next callback will be onRestart() or onDestroy()
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //When app in stopped state is about to restart
        // Should restore the state of the activity
        //Next callback will always be onStart()
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //System invokes this before the app is destroyed
        //Usually ensures all the activities resources are released
    }

    ////////////////////////////////////////////////
    /// Will be called on click of sign In button to initiate the sign in procedure
    ////////////////////////////////////////////////
    public void signInUser(View view) {
        //get input values
        EditText email = findViewById(R.id.emailOfUser);
        EditText password = findViewById(R.id.passwordOfUser);

        //Show a message to the user
        display.showMessage(this, "Signing In", display.LONG);

        //Call function to signIn the user
        auth.signInUser(email.getText().toString(), password.getText().toString());
    }

    ////////////////////////////////////////////////
    /// Will be called at end of async functions that need to update the ui
    ////////////////////////////////////////////////
    public void stateChanged(int typeOfChange) {
        // Will be called by functions required to update the ui

        // Check if the user is signed in -- open home page
        if(auth.isUserSignedIn()) {

            // Create new intent to go to Home Page
            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);

            // Initialize serialized userInfo
            String serializedUserInfo = "";
            try {
                // Convert object data to encoded string
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                ObjectOutputStream so = new ObjectOutputStream(bo);
                so.writeObject(auth.uInfo);
                so.flush();
                final byte[] byteArray = bo.toByteArray();
                serializedUserInfo = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Pass userInfo as extra data with the intent
            homeIntent.putExtra("userInfo", serializedUserInfo);

            // Start the activity
            startActivity(homeIntent);
        }
    }

}

