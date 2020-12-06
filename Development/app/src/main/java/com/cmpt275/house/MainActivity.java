package com.cmpt275.house;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.houseFirebaseClass;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.userInfo;
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

public class MainActivity extends AppCompatActivity implements updateUI {

    private final signInClass auth = new signInClass(this, this);
    private final displayMessage display = new displayMessage();
    private final notifications notify = new notifications();

    private createAccountDialogFrag createAccountDialog;

    private String userInfoString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create notification channel
        notify.createNotificationChannel(this);

        //Check if the user is already signed in
        auth.checkAuthStatus();
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
        display.showToastMessage(this, "Signing In", display.SHORT);

        //Call function to signIn the user
        auth.signInUser(email.getText().toString(), password.getText().toString());
    }


    ////////////////////////////////////////////////
    /// Will be called on click of forgot password button
    ////////////////////////////////////////////////
    public void forgotPassword(View view) {

        //Get the entered email for the user
        EditText emailInput = findViewById(R.id.emailOfUser);
        String email = emailInput.getText().toString();

        //Get confirmation from the user to reset the password
        display.createTwoBtnAlert(this, "Password Reset", "Do you want to send a reset email to: " + email + "?", "Send", "Cancel",
            (result, errorMessage) -> {
                Log.d("resetPasswordBtnClicked", "Password Reset Dialogue selected with value: " + result);

                if (result) {
                    Log.d("resetPasswordBtnClicked", "Sending reset password email for: " + email);

                    //Reset the password for the user
                    auth.forgotPassword(email);
                }
            });
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


    /////////////////////////////////////////////////
    //
    // Will open the create account fragment
    //
    /////////////////////////////////////////////////
    public void startCreateNewAccount(View view) {
        FragmentManager fm = getSupportFragmentManager();
        createAccountDialog = createAccountDialogFrag.newInstance(this);
        createAccountDialog.show(fm, "fragment_create_account");
    }


    /////////////////////////////////////////////////
    //
    // Will create an account and close the dialog
    //
    /////////////////////////////////////////////////
    public void createAccount(View view) {
        Log.d("createAccount:", "Creating an account");
        createAccountDialog.createAccount(view);
    }


    /////////////////////////////////////////////////
    //
    // Will close the dialog since it is no longer needed
    //
    /////////////////////////////////////////////////
    public void closeCreateAccountDialog(View view) {

        //Close the dialog window
        createAccountDialog.dismiss();

        //Check if the user is already signed in
        auth.checkAuthStatus();
    }


    /////////////////////////////////////////////////
    //
    // Will hide the keyboard on the call
    //
    /////////////////////////////////////////////////
    public void hideKeyboard(View view) {

        //Hide  the keyboard from the user
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



}

