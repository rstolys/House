package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt275.house.classDef.databaseObjects.taskAssignObj;
import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.mappingClass.statusMapping;
import com.cmpt275.house.classDef.taskClass;
import com.cmpt275.house.interfaceDef.mapping;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class NewTaskActivity extends AppCompatActivity implements Observer {

    public Intent newIntent;
    public taskInfo newTaskInfo = new taskInfo();
    private houseClass myHouseClass = new houseClass(this);
    private taskClass theTaskClass = new taskClass(this);

    userInfo uInfo;

    DatePicker datePicker;
    TimePicker timePicker;
    Spinner houseDropdown;
    Spinner memberDropdown;
    Spinner notifDropdown;
    Spinner tagDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtask);


        Intent lastIntent = getIntent();
        String serializedObject = lastIntent.getStringExtra("userInfo");

        if(serializedObject == ""){
            // If the serialized object is empty, error!
            Log.e("OnCreate New Task", "userInfo not passed from last activity");
        } else {
            try {
                // Decode the string into a byte array
                byte b[] = Base64.decode( serializedObject, Base64.DEFAULT );

                // Convert byte array into userInfo object
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                uInfo = (userInfo) si.readObject();
                Log.d("NEW_TASK_ACTIVITY", "Userinfo.displayName passed: " + uInfo.displayName );
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Observe the instance of the houseClass
            myHouseClass.addObserver(this);
            // Update the tasks in the houseClass from the database
            myHouseClass.viewYourHouses(uInfo);

            datePicker = (DatePicker)findViewById(R.id.datePicker1);
            timePicker = (TimePicker)findViewById(R.id.timePicker1);

            //get the spinner from the xml.
            houseDropdown = findViewById(R.id.houses_spinner);

            memberDropdown = findViewById(R.id.assignee_spinner);

            notifDropdown = findViewById(R.id.notifications_spinner);

            tagDropdown = findViewById(R.id.tags_spinner);


            //create a list of items for the notifications spinner.
            ArrayList<String> nOptions =new ArrayList<String>();

            nOptions.add("None");
            nOptions.add("1 hour before due date");
            nOptions.add("1 day before due date");
            nOptions.add("1 week before due date");
            nOptions.add("1 month before due date");

            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nOptions);
            //set the spinners adapter to the previously created one.
            notifDropdown.setAdapter(adapter2);
            notifDropdown.setSelection(0);


            //create a list of items for the notifications spinner.
            /*ArrayList<String> tagOptions =new ArrayList<String>();

            tagOptions.add("1 month before due date");
            Map<Integer, String> tagIntToString;

            tagOptions.add(ta);
            for()
            tagOptions.add("1 month before due date");*/




            houseDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    //create a list of items for the spinner.
                    ArrayList<String> memOptions = new ArrayList<String>();

                    Log.d("UPDATE MEMBER DROPDOWN", "I am putting members in dropdown");

                    StringBuilder membersListString = new StringBuilder(" ");
                    for (Map.Entry<String, houseMemberInfoObj> entry : myHouseClass.hInfos.get(position).members.entrySet()){
                        houseMemberInfoObj hMemberObj = entry.getValue();

                        membersListString.append(", ");
                        membersListString.append(hMemberObj.name);
                        memOptions.add(hMemberObj.getName());
                    }

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, memOptions);
//set the spinners adapter to the previously created one.
                    memberDropdown.setAdapter(adapter1);
                    if(myHouseClass.hInfos.get(position).members.size()>0)
                        memberDropdown.setSelection(0);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    //
                }
            });

        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navBar_tasks);
        navView.setOnNavigationItemSelectedListener(navListener); //so we can implement it outside onCreate


        Button saveButton = findViewById(R.id.new_task_save_button);
        saveButton.setOnClickListener(v -> {

            // Scrape data off UI
            EditText taskTitle = findViewById(R.id.new_task_name);
            newTaskInfo.displayName = String.valueOf(taskTitle.getText());

            EditText taskDescription = findViewById(R.id.new_task_description);
            newTaskInfo.description = String.valueOf(taskDescription.getText());

            Date dueDate =  new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(),
                    datePicker.getDayOfMonth(), timePicker.getCurrentHour(),timePicker.getCurrentMinute()).getTime();
            newTaskInfo.dueDate = dueDate;

            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(dueDate);
            Log.d("NEW_TASK_ACTIVITY", "new task date " + strDate );

            newTaskInfo.houseName = myHouseClass.hInfos.get(houseDropdown.getSelectedItemPosition()).displayName;
            newTaskInfo.house_id = myHouseClass.hInfos.get(houseDropdown.getSelectedItemPosition()).id;

                    //create a list of items from hashmap
                    ArrayList<String> namesMem = new ArrayList<String>();
                    ArrayList<String> idMem = new ArrayList<String>();

                    StringBuilder membersListString = new StringBuilder(" ");
                    for (Map.Entry<String, houseMemberInfoObj> entry :
                            myHouseClass.hInfos.get(houseDropdown.getSelectedItemPosition()).members.entrySet()){
                        houseMemberInfoObj hMemberObj = entry.getValue();

                        membersListString.append(", ");
                        membersListString.append(hMemberObj.name);
                        namesMem.add(hMemberObj.getName());
                        idMem.add(entry.getKey());
                    }

            newTaskInfo.assignedTo.put(idMem.get(memberDropdown.getSelectedItemPosition()),
                    new taskAssignObj(namesMem.get(memberDropdown.getSelectedItemPosition()), true, true));
            Log.d("NEW TASK", "onCreate: ASSIGNED ID "+ idMem.get(memberDropdown.getSelectedItemPosition()));
            Log.d("NEW TASK", "onCreate: ASSIGNED NAME "+ namesMem.get(memberDropdown.getSelectedItemPosition()));

            newTaskInfo.createdBy = uInfo.displayName;
            newTaskInfo.createdBy_id = uInfo.id;

            mapping statusMap = new statusMapping();
            newTaskInfo.status = statusMap.mapIntToString(1);

            newTaskInfo.costAssociated = 0.0;
            newTaskInfo.difficultyScore  = 1;

            Date notifDate = new Date();

            switch (notifDropdown.getSelectedItemPosition()){
                case 0:
                    //no notifications
                    break;
                case 1:
                    notifDate=  new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(),
                        datePicker.getDayOfMonth(), timePicker.getCurrentHour()-1,
                            timePicker.getCurrentMinute()).getTime();
                    break;
                case 2:
                    notifDate=  new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(),
                            datePicker.getDayOfMonth()-1, timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute()).getTime();
                    break;
                case 3:
                    notifDate=  new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(),
                            datePicker.getDayOfMonth()-7, timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute()).getTime();
                    break;
                case 4:
                    notifDate=  new GregorianCalendar(datePicker.getYear(), datePicker.getMonth()-1,
                            datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute()).getTime();
                    break;
            }
            newTaskInfo.notificationTime= notifDate;

            Log.d("createTaskButton", "Creating task in db with name: " + newTaskInfo.displayName);
            theTaskClass.createTask(newTaskInfo);

            // First prepare the userInfo to pass to next activity
            String serializedUserInfo = getSerializedUserInfo();
            newIntent = new Intent(NewTaskActivity.this, TaskActivity.class);
            newIntent.putExtra("userInfo", serializedUserInfo);
            startActivity( newIntent );
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    // First prepare the userInfo to pass to next activity
                    String serializedUserInfo = getSerializedUserInfo();

                    switch (item.getItemId()){
                        case R.id.navBar_home:
                            newIntent = new Intent(NewTaskActivity.this, HomeActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_tasks:
                            // Do we really want to go back to tasks page here?
                            newIntent = new Intent(NewTaskActivity.this, TaskActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_houses:
                            newIntent = new Intent(NewTaskActivity.this, HouseActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
                            break;
                        case R.id.navBar_settings:
                            newIntent = new Intent(NewTaskActivity.this, SettingsActivity.class);
                            newIntent.putExtra("userInfo", serializedUserInfo);
                            startActivity( newIntent );
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

    @Override
    public void update(Observable o, Object arg) {
        Log.d("UPDATE", "In update to update tasks");
        this.updateHouses();
    }

    private void updateHouses() {

//create a list of items for the spinner.
        ArrayList<String> hOptions =new ArrayList<String>();
//create an adapter to describe how the items are displayed

       Log.d("UPDATE HOUSE DROPDOWN", "I am putting houses in dropdown");
        for(int i = 0; i < myHouseClass.hInfos.size(); i++ ){
            hOptions.add(myHouseClass.hInfos.get(i).displayName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hOptions);
//set the spinners adapter to the previously created one.
        houseDropdown.setAdapter(adapter);
        if(myHouseClass.hInfos.size()>0)
        houseDropdown.setSelection(0);

    }

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