package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cmpt275.house.classDef.databaseObjects.taskAssignObj;
import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.mappingClass.statusMapping;
import com.cmpt275.house.classDef.mappingClass.tagMapping;
import com.cmpt275.house.classDef.taskClass;
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

public class NewTaskActivity extends AppCompatActivity implements Observer {

    private final taskInfo newTaskInfo = new taskInfo();
    private userInfo uInfo;

    private final houseClass myHouseClass = new houseClass(this);
    private final taskClass theTaskClass = new taskClass(this);
    private final displayMessage display = new displayMessage();
    private final tagMapping tagMap = new tagMapping();
    private final statusMapping statusMap = new statusMapping();

    private Intent newIntent;
    private FragmentTransaction fragmentTransaction;


    private DatePicker datePicker;
    private TimePicker timePicker;
    private Spinner houseDropdown;
    private Spinner memberDropdown;
    private Spinner notifDropdown;
    private Spinner tagDropdown;

    private ArrayList<FieldFrag> fields = new ArrayList<FieldFrag>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtask);


        Intent lastIntent = getIntent();
        String serializedObject = lastIntent.getStringExtra("userInfo");

        if(serializedObject.equals("")){
            // If the serialized object is empty, error!
            Log.e("OnCreate New Task", "userInfo not passed from last activity");
        }
        else {
            try {
                // Decode the string into a byte array
                byte[] b = Base64.decode( serializedObject, Base64.DEFAULT );

                // Convert byte array into userInfo object
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                uInfo = (userInfo) si.readObject();
                Log.d("NEW_TASK_ACTIVITY", "userInfo.displayName passed: " + uInfo.displayName );
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            setupNewTaskPage();
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navBar_tasks);
        navView.setOnNavigationItemSelectedListener(navListener); //so we can implement it outside onCreate


        Button saveButton = findViewById(R.id.edit_task_save_button);
        saveButton.setOnClickListener(this::createNewTask);

        Button backButton = findViewById(R.id.returnToTaskPage);
        backButton.setOnClickListener(this::returnToTaskPage);
    }


    /////////////////////////////////////////////////
    //
    // Will go back to task landing page
    //
    /////////////////////////////////////////////////
    private void returnToTaskPage(View view) {
        newIntent = new Intent(NewTaskActivity.this, TaskActivity.class);
        newIntent.putExtra("userInfo", getSerializedUserInfo());
        startActivity( newIntent );
    }


    /////////////////////////////////////////////////
    //
    // Setup all the input options for creating a new task
    //
    /////////////////////////////////////////////////
    private void setupNewTaskPage() {

        FragmentManager fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        // Observe the instance of the houseClass
        myHouseClass.addObserver(this);

        // Update the houses in the houseClass from the database
        myHouseClass.viewYourHouses(uInfo);

        datePicker = (DatePicker)findViewById(R.id.edit_datePicker);
        timePicker = (TimePicker)findViewById(R.id.edit_timePicker);

        //get the spinner from the xml.
        houseDropdown = findViewById(R.id.edit_houses_spinner);

        memberDropdown = findViewById(R.id.edit_assignee_spinner);

        notifDropdown = findViewById(R.id.edit_notifications_spinner);

        tagDropdown = findViewById(R.id.edit_tags_spinner);

        addField();

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


        //create a list of items for the tags spinner.
        ArrayList<String> tagOptions = new ArrayList<String>();
        for(int tag = -1; tag <= 10; tag++) {
            tagOptions.add(tagMap.mapIntToString(tag));
        }

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tagOptions);
//set the spinners adapter to the previously created one.
        tagDropdown.setAdapter(adapter3);
        tagDropdown.setSelection(0);

        //Setup add field click listener
        Button addFieldBtn = (Button) findViewById(R.id.add_item_button);
        addFieldBtn.setOnClickListener(v -> {
            addField();
        });


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
                //Do we need to do anything here...?
            }
        });
    }


    /////////////////////////////////////////////////
    //
    // Define navigation bar behaviour
    //
    /////////////////////////////////////////////////
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



    /////////////////////////////////////////////////
    //
    // Observer update call
    //
    /////////////////////////////////////////////////
    @Override
    public void update(Observable o, Object arg) {
        Log.d("UPDATE", "In update to update tasks");
        this.updateHouses();
    }

    /////////////////////////////////////////////////
    //
    // Will create a new task based on user input
    //
    /////////////////////////////////////////////////
    private void createNewTask(View v){
        // Scrape data off UI
        EditText taskTitle = findViewById(R.id.edit_task_name);
        newTaskInfo.displayName = taskTitle.getText().toString();

        EditText taskDescription = findViewById(R.id.edit_task_description);
        newTaskInfo.description = taskDescription.getText().toString();

        EditText penalty = findViewById(R.id.edit_task_penalty);
        if(penalty.getText().toString().equals(""))
            newTaskInfo.difficultyScore = 0;
        else
            newTaskInfo.difficultyScore = Integer.parseInt(penalty.getText().toString());

        EditText cost = findViewById(R.id.edit_task_associated_cost);
        if(cost.getText().toString().equals(""))
            newTaskInfo.costAssociated = 0;
        else
            newTaskInfo.costAssociated = Double.parseDouble(cost.getText().toString());

        Date dueDate =  new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth(), timePicker.getCurrentHour(),timePicker.getCurrentMinute()).getTime();
        newTaskInfo.dueDate = dueDate;


        newTaskInfo.tag.add(0,tagDropdown.getSelectedItem().toString());

        newTaskInfo.houseName = myHouseClass.hInfos.get(houseDropdown.getSelectedItemPosition()).displayName;
        newTaskInfo.house_id = myHouseClass.hInfos.get(houseDropdown.getSelectedItemPosition()).id;

        //create a list of items from hash map
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

        newTaskInfo.createdBy = uInfo.displayName;
        newTaskInfo.createdBy_id = uInfo.id;

        newTaskInfo.status = statusMap.NOT_COMPLETE;

        newTaskInfo.itemList = getItemList();

        Date notifDate = null;
        Calendar calendar = Calendar.getInstance();

        switch (notifDropdown.getSelectedItemPosition()){
            case 0:
                //no notifications
                break;
            case 1:
                notifDate=  dueDate;
                calendar.setTime(notifDate);
                calendar.add(Calendar.HOUR_OF_DAY, -1);
                notifDate= calendar.getTime();
                break;
            case 2:
                notifDate=  dueDate;
                calendar.setTime(notifDate);
                calendar.add(Calendar.DATE, -1);
                notifDate= calendar.getTime();
                break;
            case 3:
                notifDate=  dueDate;
                calendar.setTime(notifDate);
                calendar.add(Calendar.DATE, -7);
                notifDate= calendar.getTime();
                break;
            case 4:
                notifDate=  dueDate;
                calendar.setTime(notifDate);
                calendar.add(Calendar.MONTH, -1);
                notifDate= calendar.getTime();
                break;
        }
        newTaskInfo.notificationTime= notifDate;

        //sanitizing input
        if(TextUtils.isEmpty(taskTitle.getText().toString()))
            display.showToastMessage(this, "Your task doesn't have a name. It needs one", display.LONG);
        else if(newTaskInfo.dueDate.before(new Date())  ||newTaskInfo.dueDate.equals(new Date() ))
            display.showToastMessage(this, "The due date has already passed. Choose one in the future", display.LONG);
        else {
            Log.d("createTaskButton", "Creating task in db with name: " + newTaskInfo.displayName);

            display.showToastMessage(this, "Creating Task...", display.SHORT);

            theTaskClass.createTask(newTaskInfo, result -> {
                if(result) {
                    // Prepare the userInfo to pass to next activity
                    String serializedUserInfo = getSerializedUserInfo();

                    //Send user back to task activity
                    newIntent = new Intent(NewTaskActivity.this, TaskActivity.class);
                    newIntent.putExtra("userInfo", serializedUserInfo);
                    startActivity( newIntent );
                }
                //else -- error message already displayed
            });
        }
    }


    /////////////////////////////////////////////////
    //
    // Will add the houses to the houses dropdown
    //
    /////////////////////////////////////////////////
    private void updateHouses() {

        //create a list of items for the spinner.
        ArrayList<String> hOptions = new ArrayList<String>();

        //create an adapter to describe how the items are displayed
       for(int i = 0; i < myHouseClass.hInfos.size(); i++) {
            hOptions.add(myHouseClass.hInfos.get(i).displayName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hOptions);

        //set the spinners adapter to the previously created one.
        houseDropdown.setAdapter(adapter);
        if(myHouseClass.hInfos.size() > 0)
            houseDropdown.setSelection(0);

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


    ////////////////////////////////////////////////////////////
    //
    // Will add list item to the UI
    //
    ////////////////////////////////////////////////////////////
    private void addField(){
        FieldFrag field = new FieldFrag();
        fields.add(field);
        
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        ft.add(R.id.editTask_list, field);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }


    ////////////////////////////////////////////////////////////
    //
    // Will collect items list
    //
    ////////////////////////////////////////////////////////////
    private ArrayList<String> getItemList(){

        ArrayList<String> itemList = new ArrayList<String>();

        if(fields.isEmpty()) {
            return itemList;
        }

        for(int i = 0; i < fields.size(); i++) {
            if(!TextUtils.isEmpty(fields.get(i).getItem()))
                itemList.add(fields.get(i).getItem());
        }

        return itemList;
    }


    /////////////////////////////////////////////////
    //
    // Will hide the keyboard on the call
    //
    /////////////////////////////////////////////////
    public void hideKeyboard(View view) {

        //Hide  the keyboard from the user
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}