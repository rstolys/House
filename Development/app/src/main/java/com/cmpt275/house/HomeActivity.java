package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt275.house.classDef.homeClass;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class HomeActivity extends AppCompatActivity {

    private homeClass homeClass = new homeClass(this);

    private String passedUserInfo;
    public userInfo uInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get userInfo from last activity
        Intent lastIntent = getIntent();
        String serializedObject = lastIntent.getStringExtra("userInfo");

        if(serializedObject.equals("")){
            // If the serialized object is empty, error!
            Log.e("OnCreate Home", "userInfo not passed from last activity");
        } else {
            try {
                // Decode the string into a byte array
                byte b[] = Base64.decode( serializedObject, Base64.DEFAULT );

                // Convert byte array into userInfo object
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                homeClass.uInfo = (userInfo) si.readObject();
                Log.d("HOME_ACTIVITY", "UserInfo.displayName passed: " + homeClass.uInfo.displayName );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

       /* AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navBar_home, R.id.navBar_tasks, R.id.navBar_houses, R.id.navBar_Settings)
                .build();*/


        navView.setOnNavigationItemSelectedListener(navListener); //so we can implement it outside onCreate
        /*
        NavController navController = Navigation.findNavController(this, R.id.nav_graph);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
         */
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
                        so.writeObject(uInfo);
                        so.flush();
                        final byte[] byteArray = bo.toByteArray();
                        serializedUserInfo = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Now start appropriate activity
                    switch (item.getItemId()){
                        case R.id.navBar_home:
                            break;
                        case R.id.navBar_tasks:
                            Intent tasksIntent = new Intent(HomeActivity.this, TaskActivity.class);
                            tasksIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity(tasksIntent);
                            break;
                        case R.id.navBar_houses:
                            Intent houseIntent = new Intent(HomeActivity.this, HouseActivity.class);
                            houseIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity(houseIntent);
                            break;
                        case R.id.navBar_Settings:
                            Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                            settingsIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity(settingsIntent);
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
}
