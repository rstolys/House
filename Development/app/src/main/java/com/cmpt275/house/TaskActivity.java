package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.homeClass;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class TaskActivity extends AppCompatActivity implements Observer {


    private final taskClass myTaskClass = new taskClass(this);
    private final displayMessage display = new displayMessage();

    private Intent newIntent;
    private Intent addButtonIntent;
    FragmentTransaction fragmentTransaction;

    private final statusMapping statusMap = new statusMapping();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        // Get userInfo from last activity
        Intent lastIntent = getIntent();
        String serializedObject = lastIntent.getStringExtra("userInfo");

        if(serializedObject.equals("")){
            // If the serialized object is empty, error!
            Log.e("OnCreate Task", "userInfo not passed from last activity");
        } else {
            try {
                // Decode the string into a byte array
                byte[] b = Base64.decode( serializedObject, Base64.DEFAULT );

                // Convert byte array into userInfo object
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                myTaskClass.uInfo = (userInfo) si.readObject();
                Log.d("TASK_ACTIVITY", "Userinfo.displayName passed: " + myTaskClass.uInfo.displayName );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        myTaskClass.addObserver(this);

        // Update the tasks in the taskClass from the database
        myTaskClass.getTasks(myTaskClass.uInfo.id, result -> {
            updateTasks();
        });


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navBar_tasks);
        navView.setOnNavigationItemSelectedListener(navListener); //so we can implement it outside onCreate

        Button addTask = (Button) findViewById(R.id.addTaskButton);
        addTask.setOnClickListener(v -> {

            //Check if the user belongs to a house
            if( myTaskClass.uInfo.houses.isEmpty()) {
                display.showToastMessage(this, "You need to join a house first", display.LONG);
            }
            else {
                // First prepare the userInfo to pass to next activity
                String serializedUserInfo = getSerializedUserInfo();

                addButtonIntent = new Intent(TaskActivity.this, NewTaskActivity.class);
                addButtonIntent.putExtra("userInfo", serializedUserInfo);
                startActivity(addButtonIntent);
            }
        });
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
                            newIntent = new Intent(TaskActivity.this, HomeActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_tasks:
                            newIntent = new Intent(TaskActivity.this, TaskActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_houses:
                            newIntent = new Intent(TaskActivity.this, HouseActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_settings:
                            newIntent = new Intent(TaskActivity.this, SettingsActivity.class);
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
    // Observer listener for changes from task class
    //      **Not used
    //
    /////////////////////////////////////////////////
    @Override
    public void update(Observable o, Object arg) {
        this.updateTasks();
    }


    /////////////////////////////////////////////////
    //
    // Update the tasks to the screen
    //
    /////////////////////////////////////////////////
    public void updateTasks() {

        // First check if there are any tasks on the screen
        FragmentManager fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        try {
            Log.d("UPDATE_TASKS", "There are " + fm.getBackStackEntryCount() + " backEntry");
            int numBackStack = fm.getBackStackEntryCount();
            for(; numBackStack>0; numBackStack--) {
                fm.popBackStack();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        Calendar calendar = new GregorianCalendar();

        Date now = new Date();

        //constants for sorting tasks in screen
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date dueToday = new Date();


        calendar.setTime(now);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        Date dueWeek = calendar.getTime();

        //calendar.setTime(now);
        calendar.add(Calendar.MONTH, 1);
        Date dueMonth= calendar.getTime();

        for(int i = 0; i < myTaskClass.tInfos.length; i++ ){
            TaskFrag taskFrag = new TaskFrag(myTaskClass.tInfos[i], myTaskClass.uInfo, i, myTaskClass);

            //sorting tasks

            /*DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(dueMonth);
            String dueDate = dateFormat.format(myTaskClass.tInfos.get(i).dueDate);

            Log.d("UPDATE_TASKS", "dueToday constant"+ strDate);
            Log.d("UPDATE_TASKS", "due date"+ dueDate);*/
            if(!Objects.requireNonNull(myTaskClass.tInfos[i].assignedTo.get(myTaskClass.uInfo.id)).approved)
                fragmentTransaction.add(R.id.toApprove_task_list, taskFrag);
            else if(myTaskClass.tInfos[i].status.equals(statusMap.COMPLETED))
                fragmentTransaction.add(R.id.completed_tasks_list, taskFrag);
            else if(myTaskClass.tInfos[i].dueDate.before(now))
                fragmentTransaction.add(R.id.overdue_tasks_list, taskFrag);
            else if(myTaskClass.tInfos[i].dueDate.before(dueToday))
                fragmentTransaction.add(R.id.due_today_tasks_list, taskFrag);
            else if(myTaskClass.tInfos[i].dueDate.before(dueWeek))
                fragmentTransaction.add(R.id.due_this_week_tasks_list, taskFrag);
            else if(myTaskClass.tInfos[i].dueDate.before(dueMonth))
                fragmentTransaction.add(R.id.due_this_month_tasks_list, taskFrag);
            else
                fragmentTransaction.add(R.id.due_later_tasks_list, taskFrag);


            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }


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
            so.writeObject(myTaskClass.uInfo);
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
