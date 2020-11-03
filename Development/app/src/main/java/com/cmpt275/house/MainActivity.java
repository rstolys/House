package com.cmpt275.house;

import com.cmpt275.house.classDef.signIn;
import com.cmpt275.house.interfaceDef.updateUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements updateUI {

    private signIn auth = new signIn(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
         */
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

        //Call function to signIn the user
        auth.signInUser(email.getText().toString(), password.getText().toString());
    }

    ////////////////////////////////////////////////
    /// Will be called at end of async functions that need to update the ui
    ////////////////////////////////////////////////
    public void stateChanged(int typeOfChange) {
        //Will be called by functions required to update the ui

        //Check if the user is signed in -- open home page
        if(auth.isUserSignedIn()) {
            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
            homeIntent.putExtra("type", 0); //Specify startup type
            startActivity(homeIntent);
        }
    }
}

