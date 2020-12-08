package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.classDef.userFirebaseClass;
import com.cmpt275.house.interfaceDef.Callbacks.updateCallback;
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
    votingInfo[] vInfos;

    private userFirebaseClass firebaseUserTask;
    public displayMessage display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_view);

        // Get userInfo and houseId from last activity
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
        hClass.getVotes(houseId);

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

        Button editButton = findViewById(R.id.view_house_edit_button);
        editButton.setOnClickListener(v->{
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

            newIntent = new Intent(HouseViewActivity.this, HouseEditActivity.class);
            newIntent.putExtra("userInfo", serializedUserInfo);
            newIntent.putExtra("houseId", houseId );
            startActivity( newIntent );
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navBar_houses);
        navView.setOnNavigationItemSelectedListener(navListener); //so we can implement it outside onCreate

        //Start with keyboard hidden
        hideKeyboard(findViewById(android.R.id.content).getRootView());
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
                        case R.id.navBar_settings:
                            newIntent = new Intent(HouseViewActivity.this, SettingsActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                    }

                    return true;
                }
            };

    @Override
    protected void onStop() {
        super.onStop();
        //Our activity is no longer visible to the user
        //Next callback will be onRestart() or onDestroy()
    }

    @Override
    public void update(Observable o, Object obj) {
        // Called on displaying a users house information to the screen
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Log.d("UPDATE HOUSE_VIEW_ACT:", "Object passed: " + obj);
        if(String.valueOf(obj).equals("viewVoting")){
            this.vInfos = this.hClass.vInfos;

            for(int voteIndex = 0; voteIndex < vInfos.length; voteIndex++) {
                //HouseViewVotingFrag(Context mContext, votingInfo vInfo, int voteIndex, userInfo uInfo, houseClass houseAction)
                HouseViewVotingFrag votingFrag = new HouseViewVotingFrag(this, vInfos[voteIndex], voteIndex, uInfo, hClass);
                ft.add(R.id.view_house_votes, votingFrag);
                ft.addToBackStack(null);
            }
        }
        else if(String.valueOf(obj).equals("viewHouse")){
            // Load house data that is not on a callback (title, members, description)
            this.hInfo = this.hClass.hInfo;
            TextView houseTitle = findViewById(R.id.view_house_house_name);
            houseTitle.setText(this.hInfo.displayName);

            // Find the information for the caller of this view house
            houseMemberInfoObj viewingMember = hInfo.members.get(uInfo.id);

            // Go through each member of the house and dynamically populate a fragment with their information
            for(houseMemberInfoObj houseMember : hInfo.members.values()) {
                HouseViewMemberFrag hvmf = new HouseViewMemberFrag("viewHouse", houseMember, viewingMember );
                ft.add(R.id.view_house_members, hvmf);
                ft.addToBackStack(null);
            }

            // Now get the tasks for the updated hInfo
            hClass.getTasks(hClass.hInfo);

            TextView houseDescription = findViewById(R.id.view_house_description);
            houseDescription.setText(this.hInfo.description);
        } else if(obj == "getTasks"){

            // Go through each task and populate a fragment with its parameters
            for(taskInfo tInfo : hClass.tInfos){
                HouseViewTaskFrag hvtf = new HouseViewTaskFrag( tInfo, uInfo.id );
                ft.add(R.id.view_house_tasks, hvtf);
            }

        }

        ft.commit();
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
