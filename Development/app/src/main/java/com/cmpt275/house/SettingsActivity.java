package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.settingsClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class SettingsActivity extends AppCompatActivity {

    private Intent newIntent;
    private settingsClass setting = new settingsClass(this);
    private displayMessage display = new displayMessage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get userInfo from last activity
        Intent lastIntent = getIntent();
        String serializedObject = lastIntent.getStringExtra("userInfo");

        if(serializedObject.equals("")){
            // If the serialized object is empty, error!
            Log.e("OnCreate Home", "userInfo not passed from last activity");
        }
        else {
            try {
                // Decode the string into a byte array
                byte[] b = Base64.decode( serializedObject, Base64.DEFAULT );

                // Convert byte array into userInfo object
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                setting.uInfo = (userInfo) si.readObject();
                Log.d("SETTINGS_ACTIVITY", "UserInfo.displayName passed: " + setting.uInfo.displayName );
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);       //so we can implement it outside onCreate
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    // First prepare the userInfo to pass to next activity
                    String serializedUserInfo = "";
                    try {
                        // Convert object data to encoded string
                        ByteArrayOutputStream bo = new ByteArrayOutputStream();
                        ObjectOutputStream so = new ObjectOutputStream(bo);
                        so.writeObject(setting.uInfo);
                        so.flush();
                        final byte[] byteArray = bo.toByteArray();
                        serializedUserInfo = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Start appropriate activity
                    switch (item.getItemId()){
                        case R.id.navBar_home:
                            newIntent = new Intent(SettingsActivity.this, HomeActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_tasks:
                            newIntent = new Intent(SettingsActivity.this, TaskActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_houses:
                            newIntent = new Intent(SettingsActivity.this, HouseActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_settings:
                            break;
                    }

                    return true;
                }
            };


    @Override
    protected void onStart() {
        super.onStart();
        //Will be called after onCreate -- activity is now visible to the user
        // Should contain final preparations before becoming interactive

        //Load the values into the page
        TextView displayNameInput = (TextView) findViewById(R.id.settings_displayName);
        displayNameInput.setText(setting.uInfo.displayName);

        TextView emailInput = (TextView) findViewById(R.id.settings_Email);
        emailInput.setText(setting.uInfo.email);

        ToggleButton notificationToggleBtn = (ToggleButton) findViewById(R.id.notification_choice);
        notificationToggleBtn.setChecked(setting.uInfo.notificationsAllowed);
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


    /////////////////////////////////////////////////
    //
    // Will initiate the reset password procedure
    //
    /////////////////////////////////////////////////
    public void resetPasswordBtnClicked(View view) {

        //Get confirmation from the user to reset the password
        display.createTwoBtnAlert(this, "Password Reset", "Are you sure you want to reset your password", "Yes", "No",
                (result, errorMessage) -> {
                    Log.d("resetPasswordBtnClicked", "Password Reset Dialogue selected with value: " + result);

                    if(result) {
                        Log.d("resetPasswordBtnClicked", "Sending reset password email for: " + setting.uInfo.email);

                        //Reset the password for the user
                        setting.resetPassword(setting.uInfo.email);
                    }
        });
    }


    /////////////////////////////////////////////////
    //
    // Will submit feedback about the app
    //
    /////////////////////////////////////////////////
    public void submitFeedback(View view) {

        //Collect the feedback submitted by the user
        EditText feedback = findViewById(R.id.feedback_provided);

        //Reset the password for the user
        setting.provideFeedback(feedback.getText().toString());
    }


    /////////////////////////////////////////////////
    //
    // Will submit feedback about the app
    //
    /////////////////////////////////////////////////
    public void saveSettingInfo(View view) {

        //Collect the displayName of the user
        EditText displayName = findViewById(R.id.settings_displayName);
        String newDisplayName = displayName.getText().toString();

        //Collect the email of the user
        EditText email = findViewById(R.id.settings_Email);
        String newEmail = email.getText().toString();

        //Collect the state of the notifications of the user
        ToggleButton notificationToggleBtn = (ToggleButton) findViewById(R.id.notification_choice);
        boolean notificationsAllowed = notificationToggleBtn.isChecked();

        //Determine if values have changed
        boolean displayNameChanged = !newDisplayName.equals(setting.uInfo.displayName);
        boolean emailChanged = !newEmail.equals(setting.uInfo.email);
        boolean notificationsChanged = !notificationsAllowed == setting.uInfo.notificationsAllowed;

        if(displayNameChanged || emailChanged || notificationsChanged) {
            //If the displayName has changed - change it
            if(displayNameChanged) {
                setting.changeDisplayName(newDisplayName);
            }

            //If the email has changed - change it
            if(emailChanged) {
                setting.changeEmail(newEmail);
            }

            if(notificationsChanged) {
                setting.changeNotifications(notificationsAllowed);
            }
        }
        else {
            display.showToastMessage(this, "Settings Saved", display.LONG);
        }
    }


    /////////////////////////////////////////////////
    //
    // Will submit feedback about the app
    //
    /////////////////////////////////////////////////
    public void logoutOfApp(View view) {
        //Logout of app
        setting.logout((typeOfChange -> {
            //Send user to the main activity
            newIntent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(newIntent);
        }));
    }

    /////////////////////////////////////////////////
    //
    // Will hide the keyboard on the call
    //
    /////////////////////////////////////////////////
    public void hideKeyboard(View view) {

        //Hide  the keyboard from the user
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
