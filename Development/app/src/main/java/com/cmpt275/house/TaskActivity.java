package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cmpt275.house.R;
import com.cmpt275.house.classDef.taskInfo;
import com.cmpt275.house.classDef.userInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TaskActivity extends AppCompatActivity {

    taskInfo tInfo;
    userInfo uInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        //Will get the task info
        //tInfo = (taskInfo) getIntent().getSerializableExtra("taskInfo");
        //uInfo = (userInfo) getIntent().getSerializableExtra("userInfo");

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navBar_home, R.id.navBar_tasks, R.id.navBar_houses, R.id.navBar_Settings)
                .build();*/
        navView.setOnNavigationItemSelectedListener(navListener); //so we can implement it outside onCreate

         /*
        NavController navController = Navigation.findNavController(this, R.id.bottom_nav_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
         */

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.navBar_home:
                            startActivity(new Intent(TaskActivity.this, HomeActivity.class));
                            break;
                        case R.id.navBar_tasks:
                            break;
                        case R.id.navBar_houses:
                            startActivity(new Intent(TaskActivity.this, HouseActivity.class));
                            overridePendingTransition(0,0);
                            break;
                        case R.id.navBar_Settings:
                            startActivity(new Intent(TaskActivity.this, SettingsActivity.class));
                            overridePendingTransition(0,0);
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
