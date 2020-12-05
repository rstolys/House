package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Integer.parseInt;

public class HouseEditActivity extends AppCompatActivity implements Observer {

    private Intent newIntent;
    userInfo uInfo;
    houseInfo hInfo;
    houseClass hClass;
    votingInfo[] vInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_edit);

        // Get userInfo from last activity
        Intent lastIntent = getIntent();
        String serializedObject = lastIntent.getStringExtra("userInfo");
        String houseId = lastIntent.getStringExtra("houseId");

        if (serializedObject == "") {
            // If the serialized object is empty, error!
            Log.e("OnCreate Task View", "userInfo not passed from last activity");
        } else {
            try {
                // Decode the userInfo string into a byte array
                byte b[] = Base64.decode(serializedObject, Base64.DEFAULT);

                // Convert byte array into userInfo object
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                uInfo = (userInfo) si.readObject();
                Log.d("HOUSE_VIEW_ACTIVITY", "Userinfo.displayName passed: " + uInfo.displayName);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Start asyncronous call to get the entire hInfo
        hClass = new houseClass(this);
        hClass.addObserver(this);
        hClass.viewHouse(houseId);

        Button backButton = findViewById(R.id.edit_house_back_button);
        backButton.setOnClickListener(v -> {
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

            newIntent = new Intent(HouseEditActivity.this, HouseViewActivity.class);
            newIntent.putExtra("userInfo", serializedUserInfo);
            newIntent.putExtra("houseId", hClass.hInfo.id);
            startActivity(newIntent);
        });

        // Get references to where the data is
        EditText houseTitle = findViewById(R.id.edit_house_house_name);
        EditText houseMaxMembers = findViewById(R.id.edit_house_max_members_int);
        EditText notificationSched = findViewById(R.id.edit_house_notification_schedule);
        EditText punishMult = findViewById(R.id.edit_houes_punishment_mult);
        EditText houseDescrp = findViewById(R.id.edit_house_description);

        Button saveButton = findViewById(R.id.edit_house_save_button);
        saveButton.setOnClickListener(v->{
            // Scrape data off UI
            this.hInfo.displayName = String.valueOf(houseTitle.getText());
            this.hInfo.maxMembers  = parseInt(String.valueOf(houseMaxMembers.getText()));
            this.hInfo.houseNotifications = String.valueOf(notificationSched.getText());
            this.hInfo.punishmentMultiplier = parseInt(String.valueOf(punishMult.getText()));
            this.hInfo.description = String.valueOf(houseDescrp.getText());

            // Update data in backend and exit activity
            hClass.editSettings(this.hInfo);
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
                            newIntent = new Intent(HouseEditActivity.this, HomeActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_tasks:
                            newIntent = new Intent(HouseEditActivity.this, TaskActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_houses:
                            newIntent = new Intent(HouseEditActivity.this, HouseActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_settings:
                            newIntent = new Intent(HouseEditActivity.this, SettingsActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                    }

                    return true;
                }
            };

    @Override
    public void update(Observable o, Object arg) {
        // Update should return a house
        if(arg == "viewHouse"){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            // Correctly returned object with hInfo attached
            this.hInfo = this.hClass.hInfo;

            // Set hInfo data to screen
            EditText houseTitle = findViewById(R.id.edit_house_house_name);
            houseTitle.setText(hInfo.displayName);

            EditText houseMaxMembers = findViewById(R.id.edit_house_max_members_int);
            houseMaxMembers.setText(String.valueOf(hInfo.maxMembers));

            EditText notificationSched = findViewById(R.id.edit_house_notification_schedule);
            notificationSched.setText(hInfo.houseNotifications);

            EditText punishMult = findViewById(R.id.edit_houes_punishment_mult);
            punishMult.setText(String.valueOf(hInfo.punishmentMultiplier));

            EditText houseDescrp = findViewById(R.id.edit_house_description);
            houseDescrp.setText(String.valueOf(hInfo.description));

            for(houseMemberInfoObj houseMember : hInfo.members.values()) {
                HouseViewMemberFrag hvmf = new HouseViewMemberFrag("editHouse", houseMember.name, houseMember.role);

                // Determine if this user is authorized to make changes to house settings
                if( houseMember.name.equals(uInfo.displayName) ){
                    Button saveButton = findViewById(R.id.edit_house_save_button);
                    saveButton.setEnabled(houseMember.role == "Administrator");
                }

                ft.add(R.id.edit_house_member_list, hvmf);
                ft.addToBackStack(null);
            }

            ft.commit();
        } else if( arg == "editSettings" ) {
            // Call to edit settings has passed and back end updated

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

            newIntent = new Intent(HouseEditActivity.this, HouseViewActivity.class);
            newIntent.putExtra("userInfo", serializedUserInfo);
            newIntent.putExtra("houseId", hClass.hInfo.id);
            startActivity(newIntent);
        }
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
