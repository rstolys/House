package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewTaskActivity extends AppCompatActivity{

    public Intent newIntent;
    public taskInfo newTaskInfo = new taskInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtask);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener); //so we can implement it outside onCreate


        Button saveButton = findViewById(R.id.new_task_save_button);
        saveButton.setOnClickListener(v -> {
            EditText taskTitle = findViewById(R.id.new_task_name);
            newTaskInfo.displayName = String.valueOf(taskTitle.getText());

            EditText taskDescription = findViewById(R.id.new_task_description);
            newTaskInfo.description = String.valueOf(taskDescription);
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.navBar_home:
                            newIntent = new Intent(NewTaskActivity.this, HomeActivity.class);
                            newIntent.putExtra("userInfo", 0);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_tasks:
                            // Do we really want to go back to tasks page here?
                            newIntent = new Intent(NewTaskActivity.this, TaskActivity.class);
                            newIntent.putExtra("userInfo", 0);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_houses:
                            newIntent = new Intent(NewTaskActivity.this, HouseActivity.class);
                            newIntent.putExtra("userInfo", 0);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_Settings:
                            newIntent = new Intent(NewTaskActivity.this, SettingsActivity.class);
                            newIntent.putExtra("userInfo", 0);
                            startActivity( newIntent );
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