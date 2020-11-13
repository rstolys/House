package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cmpt275.house.classDef.taskClass;
import com.cmpt275.house.classDef.userInfo;
import com.cmpt275.house.interfaceDef.task;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HouseActivity extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_houses);

        //userInfo uInfo = getIntent().key("userInfo");

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener); //so we can implement it outside onCreate

        Button addHouseButton = findViewById(R.id.add_house_button);
        addHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                HouseFrag houseFrag = new HouseFrag();
                fragmentTransaction.add(R.id.my_houses_list, houseFrag);
                //HouseFrag houseFrag1 = new HouseFrag();
                //fragmentTransaction.add(R.id.my_houses_list, houseFrag1);
                fragmentTransaction.commit();
            }
        });
/*
        HouseFrag houseFrag = new HouseFrag();
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction().add(R.id.my_houses_list, houseFrag).commit();
*/
        /*Button leaveHouse1Button = findViewById(R.id.leave_house1_button);
        leaveHouse1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*leaveHouse1Button.setText("Left House");

                TableRow house1_row1 = findViewById(R.id.house1_row1);
                TableRow house1_row2 = findViewById(R.id.house1_row2);
                TableRow house1_row3 = findViewById(R.id.house1_row3);
                TableRow house1_row4 = findViewById(R.id.house1_row4);
                TableRow house1_row5 = findViewById(R.id.house1_row5);
                TableRow house1_row6 = findViewById(R.id.house1_row6);
                TableRow house1_row7 = findViewById(R.id.house1_row7);

                house1_row1.setBackgroundColor(0);
                house1_row2.setBackgroundColor(0);
                house1_row3.setBackgroundColor(0);
                house1_row4.setBackgroundColor(0);
                house1_row5.setBackgroundColor(0);
                house1_row6.setBackgroundColor(0);
                house1_row7.setBackgroundColor(0);

                TextView house1_name = findViewById(R.id.house1_name);
                TextView house1_description = findViewById(R.id.house1_description);
                TextView house1_members_title = findViewById(R.id.house1_members_title);
                TextView house1_members_list = findViewById(R.id.house1_members_list);
                TextView house1_task1_title = findViewById(R.id.house1_task1_title);
                TextView house1_task1_name = findViewById(R.id.house1_task1_name);
                TextView house1_due_date_title = findViewById(R.id.house1_due_date_title);
                TextView house1_task1_due_date = findViewById(R.id.house1_task1_due_date);
                TextView house1_task2_title = findViewById(R.id.house1_task2_title);
                TextView house1_task2_name = findViewById(R.id.house1_task2_name);
                TextView house1_due_date_title2 = findViewById(R.id.house1_due_date_title2);
                TextView house1_task2_due_date = findViewById(R.id.house1_task2_due_date2);
                TextView house1_task3_title = findViewById(R.id.house1_task3_title);
                TextView house1_task3_name = findViewById(R.id.house1_task3_name);
                TextView house1_due_date_title3 = findViewById(R.id.house1_due_date_title3);
                TextView house1_task3_due_date = findViewById(R.id.house1_task3_due_date);

                house1_name.setText("");
                house1_description.setText("");
                house1_members_title.setText("");
                house1_members_list.setText("");
                house1_task1_title.setText("");
                house1_task1_name.setText("");
                house1_due_date_title.setText("");
                house1_task1_due_date.setText("");
                house1_task2_title.setText("");
                house1_task2_name.setText("");
                house1_due_date_title2.setText("");
                house1_task2_due_date.setText("");
                house1_task3_title.setText("");
                house1_task3_name.setText("");
                house1_due_date_title3.setText("");
                house1_task3_due_date.setText("");*/
        //    }
        //});
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.navBar_home:
                            Intent newIntent = new Intent(HouseActivity.this, HomeActivity.class);
                            newIntent.putExtra("userInfo", new userInfo()); //userInfo.setSerialziable...??
                            startActivity(newIntent);
                            break;
                        case R.id.navBar_tasks:
                            startActivity(new Intent(HouseActivity.this, TaskActivity.class));
                            break;
                        case R.id.navBar_houses:
                            break;
                        case R.id.navBar_Settings:
                            startActivity(new Intent(HouseActivity.this, SettingsActivity.class));
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

    public void onLeaveHouseClick(View view) {
    }

}
