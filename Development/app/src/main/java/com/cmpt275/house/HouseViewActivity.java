package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

public class HouseViewActivity extends AppCompatActivity implements Observer {

    private Intent newIntent;
    userInfo uInfo;
    houseInfo hInfo;
    houseClass hClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_view);

        // Get userInfo from last activity
        Intent lastIntent = getIntent();
        String serializedObject = lastIntent.getStringExtra("userInfo");
        String houseId = lastIntent.getStringExtra("houseInfo");
        Log.d("HOUSE_VIEW_ACTIVITY", "Started view with serialized object: " + serializedObject );
//        Log.d("HOUSE_VIEW_ACTIVITY", "Started view with serialized house object: " + serializedHouseObject );

        if(serializedObject == ""){
            // If the serialized object is empty, error!
            Log.e("OnCreate Task View", "userInfo not passed from last activity");
        } else {
            try {
                // Decode the userInfo string into a byte array
                byte b[] = Base64.decode( serializedObject, Base64.DEFAULT );

                // Convert byte array into userInfo object
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                uInfo = (userInfo) si.readObject();
                Log.d("HOUSE_VIEW_ACTIVITY", "Userinfo.displayName passed: " + uInfo.displayName );

//                // Decode the houseInfo string into a byte array
//                byte b2 [] = Base64.decode( serializedHouseObject, Base64.DEFAULT );
//
//                // Convert second byte array into userInfo object
//                ByteArrayInputStream bi2 = new ByteArrayInputStream(b2);
//                ObjectInputStream si2 = new ObjectInputStream(bi2);
//                hInfo = (houseInfo) si2.readObject();
//
//                Log.d("HOUSE_VIEW_ACTIVITY", "Started view with house ID: " + hInfo.id );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        hClass = new houseClass( this );
        hClass.addObserver(this);
        hClass.viewHouse(houseId);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener); //so we can implement it outside onCreate

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
                            newIntent = new Intent(HouseViewActivity.this, HomeActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_tasks:
                            newIntent = new Intent(HouseViewActivity.this, TaskActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_houses:
                            newIntent = new Intent(HouseViewActivity.this, HouseActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_Settings:
                            newIntent = new Intent(HouseViewActivity.this, SettingsActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                    }

                    return true;
                }
            };


    @Override
    public void update(Observable o, Object obj) {
        // Get house information from house class, now put it to screen
        this.hInfo = (houseInfo) obj;
        TextView houseTitle =  findViewById(R.id.view_hosue_house_name);
        houseTitle.setText(this.hInfo.displayName);
    }
}
