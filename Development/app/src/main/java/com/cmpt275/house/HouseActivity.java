package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firestore.v1.Value;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class HouseActivity extends AppCompatActivity implements Observer {

    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;
    private houseClass myHouseClass = new houseClass(this);
    public Intent newIntent;
    private userInfo uInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_houses);

        // Get userInfo from last activity
        Intent lastIntent = getIntent();
        String serializedObject = lastIntent.getStringExtra("userInfo");

        if(serializedObject == ""){
            // If the serialized object is empty, error!
            Log.e("OnCreate Home", "userInfo not passed from last activity");
        } else {
            try {
                // Decode the string into a byte array
                byte b[] = Base64.decode( serializedObject, Base64.DEFAULT );

                // Convert byte array into userInfo object
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                uInfo = (userInfo) si.readObject();
                Log.d("HOUSE_ACTIVITY", "Userinfo.displayName passed: " + uInfo.displayName );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Observe the instance of the houseClass
        myHouseClass.addObserver(this);

        // Update the tasks in the houseClass from the database
        Log.d("OnCreate House Activity", "Before call to view your houses" );
        myHouseClass.viewYourHouses(uInfo);
        Log.d("OnCreate House Activity", "After call to view your houses" );

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navBar_houses);
        navView.setOnNavigationItemSelectedListener(navListener); //so we can implement it outside onCreate

        Button addHouseButton = findViewById(R.id.add_house_button);
        addHouseButton.setOnClickListener(v -> {
            // Remove all current houses there
            try {
                int numBackStack = fm.getBackStackEntryCount();
                for(; numBackStack>0; numBackStack--) {
                    fm.popBackStack();
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            // Add new house first
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            NewHouseFrag newHouseFrag = new NewHouseFrag(myHouseClass, uInfo);
            fragmentTransaction.add(R.id.my_houses_list, newHouseFrag);
            fragmentTransaction.addToBackStack(null);

            // Put back all old houses
            for(int i = 0; i < myHouseClass.hInfos.size(); i++ ){
                HouseFrag houseFrag = new HouseFrag(myHouseClass.hInfos.get(i), myHouseClass);
                fragmentTransaction.add(R.id.my_houses_list, houseFrag);
                fragmentTransaction.addToBackStack(null);
            }

            fragmentTransaction.commit();
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // First prepare the userInfo to pass to next activity
                    String serializedUserInfo = serializeUserInfo();

                    // Now go to which activity was requested
                    switch (item.getItemId()){
                        case R.id.navBar_home:
                            newIntent = new Intent(HouseActivity.this, HomeActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity(newIntent);
                            break;
                        case R.id.navBar_tasks:
                            newIntent = new Intent(HouseActivity.this, TaskActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity(newIntent);
                            break;
                        case R.id.navBar_houses:
                            break;
                        case R.id.navBar_settings:
                            newIntent = new Intent(HouseActivity.this, SettingsActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity(newIntent);
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
        //Should restore the state of the activity
        //Next callback will always be onStart()
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //System invokes this before the app is destroyed
        //Usually ensures all the activities resources are released
    }

    @Override
    public void update(Observable o, Object obj) {
        // Observer pattern to update houses or start a new activity
        // Update screen with new houses
        Log.d("UPDATE", "Object is: " + obj);
        String passedParam = String.valueOf(obj);
        if(passedParam == ""){
            Log.d("UPDATE", "In update to update houses");
            this.updateHouses();
        } else {
            houseInfo hInfo = (houseInfo) obj;
            String serializedUserInfo = serializeUserInfo();

            newIntent = new Intent(HouseActivity.this, HouseViewActivity.class);
            newIntent.putExtra("userInfo", serializedUserInfo);
            newIntent.putExtra("houseId", hInfo.id );
            startActivity(newIntent);
        }
    }

    public void updateHouses(){
        // First check if there are any houses on the screen
        Log.d("UPDATE_HOUSES", "I am removing old houses from the screen");

        // Remove them if there are
        try {
            Log.d("UPDATE_HOUSES", "There are " + fm.getBackStackEntryCount() + " backEntry");
            int numBackStack = fm.getBackStackEntryCount();
            for(; numBackStack>0; numBackStack--) {
                fm.popBackStack();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        fragmentTransaction = fm.beginTransaction();

        // Now add back the updated houses
        Log.d("UPDATE_HOUSES", "I am putting my houses to screen");
        for(int i = 0; i < myHouseClass.hInfos.size(); i++ ){
            HouseFrag houseFrag = new HouseFrag(myHouseClass.hInfos.get(i), myHouseClass);
            fragmentTransaction.add(R.id.my_houses_list, houseFrag);
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    private String serializeUserInfo(){

        String serializedUserInfo = "";
        try {
            // Convert object data to encoded string
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(uInfo);
            so.flush();
            final byte[] byteArray = bo.toByteArray();
            serializedUserInfo = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e){
        }

        return serializedUserInfo;
    }
}
