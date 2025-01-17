package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpt275.house.classDef.homeClass;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {

    private final homeClass home = new homeClass(this);
    private Intent newIntent;


    /////////////////////////////////////////////////
    //
    // Called when the activity is first created
    //
    /////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get userInfo from last activity
        Intent lastIntent = getIntent();
        String serializedObject = lastIntent.getStringExtra("userInfo");

        if(serializedObject.equals("")){
            // If the serialized object is empty, error!
            Log.e("OnCreate Home", "userInfo not passed from last activity");
        }
        else {
            try {
                // Decode the string into a byte array
                byte[] b = Base64.decode( serializedObject, Base64.DEFAULT );

                // Convert byte array into userInfo object
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                home.uInfo = (userInfo) si.readObject();
                Log.d("HOME_ACTIVITY", "UserInfo.displayName passed: " + home.uInfo.displayName );
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }


        //Get the updated userInfo and load the info to the pages
        home.updateUserInfo(home.uInfo.id, result -> {
            if(result) {
                //Update the title
                TextView homeTitle = findViewById(R.id.home_title);
                homeTitle.setText("Welcome Home " + home.uInfo.displayName + "!");


                //Get reference to recycler view in activity layout
                RecyclerView rvTaskList = (RecyclerView) findViewById(R.id.taskList_home);
                RecyclerView rvHouseList = (RecyclerView) findViewById(R.id.houseList_home);

                //Create adapter by passing in data
                recyclerListAdaptor taskList = new recyclerListAdaptor(home.uInfo.tasks);
                recyclerListAdaptor houseList = new recyclerListAdaptor(home.uInfo.houses);

                //Attach the adapter to the recyclerview to populate items
                rvTaskList.setAdapter(taskList);
                rvHouseList.setAdapter(houseList);

                //Set layout manager to position the items -- do we need this?
                rvTaskList.setLayoutManager(new LinearLayoutManager(this));
                rvHouseList.setLayoutManager(new LinearLayoutManager(this));
            }
        });


        //Set the navigation bar
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navBar_home);
        navView.setOnNavigationItemSelectedListener(navListener); //so we can implement it outside onCreate
    }


    /////////////////////////////////////////////////
    //
    // Defines the navigation bar behaviour
    //
    /////////////////////////////////////////////////
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
            // First prepare the userInfo to pass to next activity
            String serializedUserInfo = getSerializedUserInfo();

            // Now start appropriate activity
            switch (item.getItemId()){
                case R.id.navBar_home:
                    break;
                case R.id.navBar_tasks:
                    Intent tasksIntent = new Intent(HomeActivity.this, TaskActivity.class);
                    tasksIntent.putExtra("userInfo", serializedUserInfo);
                    startActivity(tasksIntent);
                    break;
                case R.id.navBar_houses:
                    Intent houseIntent = new Intent(HomeActivity.this, HouseActivity.class);
                    houseIntent.putExtra("userInfo", serializedUserInfo);
                    startActivity(houseIntent);
                    break;
                case R.id.navBar_settings:
                    Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                    settingsIntent.putExtra("userInfo", serializedUserInfo);
                    startActivity(settingsIntent);
                    break;
            }

            return true;
        };


    /////////////////////////////////////////////////
    //
    // Will log user out of app
    //
    /////////////////////////////////////////////////
    public void logoutOfApp(View view) {
        //Logout of app
        home.logout( result -> {
            //Send user to the main activity
            newIntent =  new Intent(HomeActivity.this, MainActivity.class);
            startActivity(newIntent);
        });
    }


    /////////////////////////////////////////////////
    //
    // Go to settings activity
    //
    /////////////////////////////////////////////////
    public void beginSettingsActivity(View view) {

        newIntent = new Intent(HomeActivity.this, SettingsActivity.class);
        newIntent.putExtra("userInfo", getSerializedUserInfo());
        startActivity(newIntent);
    }


    /////////////////////////////////////////////////
    //
    // Begin Tasks Activity
    //
    /////////////////////////////////////////////////
    public void beginTasksActivity(View view) {

        newIntent = new Intent(HomeActivity.this, TaskActivity.class);
        newIntent.putExtra("userInfo", getSerializedUserInfo());
        startActivity(newIntent);
    }


    /////////////////////////////////////////////////
    //
    // Begin Houses Activity
    //
    /////////////////////////////////////////////////
    public void beginHousesActivity(View view) {

        newIntent = new Intent(HomeActivity.this, HouseActivity.class);
        newIntent.putExtra("userInfo", getSerializedUserInfo());
        startActivity(newIntent);
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
            so.writeObject(home.uInfo);
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



    /////////////////////////////////////////////////
    //
    // Will fill recycler views
    //
    /////////////////////////////////////////////////
    public static class recyclerListAdaptor extends RecyclerView.Adapter<recyclerListAdaptor.ViewHolder> {

        private final String[] localData;


        /////////////////////////////////////////////////
        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        /////////////////////////////////////////////////
        public static class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView textView;        //Will hold the value to be set

            public ViewHolder(View view) {
                super(view);

                textView = (TextView) view.findViewById(R.id.recyclerView_item);
            }

            public TextView getTextView() {
                return textView;
            }
        }


        /////////////////////////////////////////////////
        // Constructor for creating recycler views
        /////////////////////////////////////////////////
        public recyclerListAdaptor(Map<String, String> dataSet) {

            if(dataSet == null) {
                localData = new String [1];
                localData[0] = "Nothing to display";
            }
            else {
                localData = new String [dataSet.size()];

                //Fill the localData set from the hashMap
                int index = 0;
                for(String key : dataSet.keySet()) {
                    localData[index] = (index + 1) + ". " + dataSet.get(key);
                    index++;
                }
            }
        }


        /////////////////////////////////////////////////
        // Create new views (invoked by the layout manager)
        /////////////////////////////////////////////////
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            //Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item, viewGroup, false);

            return new ViewHolder(view);
        }


        /////////////////////////////////////////////////
        // Replace the contents of a view (invoked by the layout manager)
        /////////////////////////////////////////////////
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            //Fill the item in the recycler view
            viewHolder.getTextView().setText(localData[position]);
        }


        /////////////////////////////////////////////////
        // Return the size of your dataset (invoked by the layout manager)
        /////////////////////////////////////////////////
        @Override
        public int getItemCount() {
            return localData.length;
        }
    }

}
