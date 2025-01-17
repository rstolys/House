package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
                uInfo = (userInfo) si.readObject();
                Log.d("HOUSE_ACTIVITY", "Userinfo.displayName passed: " + uInfo.displayName );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        myHouseClass.updateUserInfo(uInfo.id, (success)->{
            if(success){
                Log.d("HOUSE", "UserInfo updated");
            }
        });

        // Observe the instance of the houseClass
        myHouseClass.addObserver(this);

        // Update the tasks in the houseClass from the database
        Log.d("OnCreate House Activity", "Before call to view your houses" );
        myHouseClass.viewYourHouses(uInfo);
        Log.d("OnCreate House Activity", "After call to view your houses" );

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navBar_houses);
        navView.setOnNavigationItemSelectedListener(navListener); // So we can implement it outside onCreate

        Button newHouseButton = findViewById(R.id.new_house_button);
        newHouseButton.setOnClickListener(v -> {
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
                HouseFrag houseFrag = new HouseFrag(myHouseClass.hInfos.get(i), myHouseClass, uInfo);
                fragmentTransaction.add(R.id.my_houses_list, houseFrag);
                fragmentTransaction.addToBackStack(null);
            }

            fragmentTransaction.commit();
        });

        Button joinHouseButton = findViewById(R.id.my_houses_join_house_button);
        joinHouseButton.setOnClickListener(v->{
            fragmentTransaction = fm.beginTransaction();

            HouseJoinHouseFrag joinHouseFrag = HouseJoinHouseFrag.newInstance(this, uInfo);
            joinHouseFrag.show(fm, "fragment_house_join");
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
                            newIntent = new Intent(HouseActivity.this, HouseActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity(newIntent);
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
    public void update(Observable o, Object obj) {
        // Observer pattern to update houses or start a new activity

        Log.d("UPDATE", "Object is: " + obj);
        String passedParam = String.valueOf(obj);
        if(passedParam == "createHouses" || passedParam == "viewHouses"){ // View houses after adding one
            Log.d("UPDATE", "In update to update houses");
            this.updateHouses();
        } else if(passedParam == "viewHouse"){
            // Or start a new activity after clicking on "View a house"
            String serializedUserInfo = serializeUserInfo();

            newIntent = new Intent(HouseActivity.this, HouseViewActivity.class);
            newIntent.putExtra("userInfo", serializedUserInfo);
            newIntent.putExtra("houseId", myHouseClass.hInfo.id );
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
            // Add all the houses to the screen
            HouseFrag houseFrag = new HouseFrag(myHouseClass.hInfos.get(i), myHouseClass, uInfo);
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
