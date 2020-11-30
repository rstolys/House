package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;
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
        String houseId = lastIntent.getStringExtra("houseId");

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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Start asyncronous call to get the entire hInfo
        hClass = new houseClass( this );
        hClass.addObserver(this);
        hClass.viewHouse(houseId);

        Button backButton = findViewById(R.id.view_house_back_button);
        backButton.setOnClickListener(v->{
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

            newIntent = new Intent(HouseViewActivity.this, HouseActivity.class);
            newIntent.putExtra("userInfo", serializedUserInfo);
            startActivity( newIntent );
        });

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
        // Called on displaying a users house information to the screen
        this.hInfo = (houseInfo) obj;
        TextView houseTitle =  findViewById(R.id.view_hosue_house_name);
        houseTitle.setText(this.hInfo.displayName);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        for(int i = 0; i < hInfo.members.size(); i++){
            HouseViewMemberFrag hvmf = new HouseViewMemberFrag("HARDCODED Ryan Stolys", "0");
            ft.add(R.id.view_house_members, hvmf);
            ft.addToBackStack(null);
        }

        ft.commit();
    }
}
