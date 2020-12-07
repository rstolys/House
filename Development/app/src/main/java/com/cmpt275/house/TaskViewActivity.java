package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.mappingClass.statusMapping;
import com.cmpt275.house.classDef.taskClass;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TaskViewActivity extends AppCompatActivity {

    taskInfo tInfo;
    userInfo uInfo;

    private taskClass myTaskClass = new taskClass(this);

    private Intent newIntent;

    private final displayMessage display = new displayMessage();
    private final statusMapping statusMap = new statusMapping();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        // Get userInfo and taskInfo from last activity
        Intent lastIntent = getIntent();
        String serializedObject = lastIntent.getStringExtra("userInfo");
        if(serializedObject.equals("")){
            // If the serialized object is empty, error!
            Log.e("OnCreate Task View", "userInfo not passed from last activity");
        }
        else {
            try {
                // Decode the string into a byte array
                byte[] b = Base64.decode( serializedObject, Base64.DEFAULT );

                // Convert byte array into userInfo object
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                uInfo = (userInfo) si.readObject();
                Log.d("TASK_ACTIVITY", "Userinfo.displayName passed: " + uInfo.displayName );
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        String serializedObject2 = lastIntent.getStringExtra("taskInfo");
        if(serializedObject2.equals("")){
            // If the serialized object is empty, error!
            Log.e("OnCreate Task View", "userInfo not passed from last activity");
        }
        else {
            try {
                // Decode the string into a byte array
                byte[] b = Base64.decode( serializedObject2, Base64.DEFAULT );

                // Convert byte array into taskInfo object
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                tInfo = (taskInfo) si.readObject();
                Log.d("TASK_ACTIVITY", "taskInfo.displayName passed: " + tInfo.displayName );
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        //displaying actual data
        TextView title = (TextView) findViewById(R.id.task_title);
        title.setText(tInfo.displayName);

        TextView description = (TextView) findViewById(R.id.task_description);
        description.setText(tInfo.description);

        TextView dueDate = (TextView) findViewById(R.id.task_due_date);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        dueDate.setText(dateFormat.format(tInfo.dueDate));

        TextView house = (TextView) findViewById(R.id.house_name);
        house.setText(tInfo.houseName);

        TextView assignee =  (TextView) findViewById(R.id.assignee_name);
        assignee.setText(uInfo.displayName);

        TextView assignedBy =(TextView) findViewById(R.id.assigned_by_name);
        assignedBy.setText(tInfo.createdBy);


        //Setup Complete task and extend task buttons
        Button completeTaskButton = (Button) findViewById(R.id.complete_taskButton);
        Button extendTaskButton = (Button) findViewById(R.id.extend_task_button);

        if(tInfo.status.equals(statusMap.COMPLETED)) {
            completeTaskButton.setText("Completed");
            extendTaskButton.setEnabled(false);
        }
        else {
            //Setup complete task click listener
            completeTaskButton.setOnClickListener(v -> {
                display.showToastMessage(this, "Completing Task...", display.SHORT);
                myTaskClass.completeTask(tInfo, -1, result -> {
                    completeTaskButton.setText("Completed");
                });
            });

            //Setup request extension click listener
            extendTaskButton.setOnClickListener(v -> {
                display.showToastMessage(this, "Requesting extension...", display.SHORT);
                myTaskClass.requestExtension(tInfo, result -> {
                    if(result) {
                        extendTaskButton.setEnabled(false);
                    }
                });
            });
        }




        //Setup delete task click listener
        Button deleteTaskButton = (Button) findViewById(R.id.delete_task_button);
        deleteTaskButton.setOnClickListener(v -> {

            //Confirm with user before deleting the task
            display.createTwoBtnAlert(this, "Delete Task", "Are you sure  you want to delete this task", "Yes", "Cancel",
                    (result, errorMessage) -> {
                        if(result) {
                            display.showToastMessage(this, "Deleting Task...", display.SHORT);
                            myTaskClass.deleteTask(tInfo, deleted -> {
                                if(deleted) {
                                    //Move back to tasks activity
                                    newIntent = new Intent(TaskViewActivity.this, TaskActivity.class);
                                    newIntent.putExtra("userInfo", getSerializedUserInfo());
                                    startActivity( newIntent );
                                }
                            });
                        }
                    });
        });


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navBar_tasks);
        navView.setOnNavigationItemSelectedListener(navListener); //so we can implement it outside onCreate
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // First prepare the userInfo to pass to next activity
                    String serializedUserInfo = getSerializedUserInfo();

                    // Now start appropriate activity
                    switch (item.getItemId()){
                        case R.id.navBar_home:
                            newIntent = new Intent(TaskViewActivity.this, HomeActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_tasks:
                            newIntent = new Intent(TaskViewActivity.this, TaskActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_houses:
                            newIntent = new Intent(TaskViewActivity.this, HouseActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_settings:
                            newIntent = new Intent(TaskViewActivity.this, SettingsActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                    }

                    return true;
                }
            };


    /*
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
     */


    /////////////////////////////////////////////////
    //
    // Get the serialized user info to pass between activities
    //
    /////////////////////////////////////////////////
    private String getSerializedUserInfo() {

        String serializedUserInfo = "";
        try {
            // Convert object data to encoded string
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(uInfo);
            so.flush();
            final byte[] byteArray = bo.toByteArray();
            serializedUserInfo = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        catch (IOException e) {
            e.printStackTrace();
            serializedUserInfo = "";
        }

        return serializedUserInfo;
    }

}
