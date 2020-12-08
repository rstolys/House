package com.cmpt275.house;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.classDef.mappingClass.statusMapping;
import com.cmpt275.house.classDef.mappingClass.tagMapping;
import com.cmpt275.house.classDef.mappingClass.voteTypeMapping;
import com.cmpt275.house.classDef.taskClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


public class TaskEditFrag extends DialogFragment implements Observer {
    
    private Context mContext; 
    
    private taskInfo tInfo;
    private userInfo uInfo;
    private taskClass taskAction;
    private houseClass houseAction;
    
    private displayMessage display;
    private statusMapping statusMap;
    private tagMapping tagMap;

    private DatePicker dueDate;
    private TimePicker dueTime;
    private Spinner houseDropdown;
    private Spinner memberDropdown;
    private Spinner notifDropdown;
    private Spinner tagDropdown;
    private ArrayList<FieldFrag> fields = new ArrayList<FieldFrag>();

    ////////////////////////////////////////////////////////////
    //
    // Required empty public constructor
    //
    ////////////////////////////////////////////////////////////
    public TaskEditFrag() {}


    ////////////////////////////////////////////////////////////
    //
    // Constructor to set class values to allow frag to be updated
    //
    ////////////////////////////////////////////////////////////
    public TaskEditFrag(Context mContext, taskInfo tInfo, userInfo uInfo, taskClass taskAction) {
        this.mContext = mContext;

        this.tInfo = tInfo;
        this.uInfo = uInfo;
        
        this.taskAction = taskAction;
        this.houseAction = new houseClass(mContext);

        display = new displayMessage();
        statusMap = new statusMapping();
        tagMap = new tagMapping();
    }


    ////////////////////////////////////////////////////////////
    //
    // Factory Method to create new account dialog
    //
    ////////////////////////////////////////////////////////////
    public static TaskEditFrag newInstance(Context mContext, taskInfo tInfo, userInfo uInfo, taskClass taskAction) {
        TaskEditFrag fragment = new TaskEditFrag(mContext, tInfo, uInfo, taskAction);
        Bundle args = new Bundle();
        return fragment;
    }
    

    ////////////////////////////////////////////////////////////
    //
    // Will specify the layout for the dialog to use, set values
    //
    ////////////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_task_edit, container, false);

        //Assign global elements values
        setupEditTaskPage(view);

        //Load the task information to the page
        showTaskInfo(view);

        //Set back button
        Button backButton = (Button) view.findViewById(R.id.returnToViewTask);
        backButton.setOnClickListener(v -> {
            //Close the dialog
            this.dismiss();
        });
        
        return view;
    }

    /////////////////////////////////////////////////
    //
    // Setup all the input options for creating a new task
    //
    /////////////////////////////////////////////////
   // @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupEditTaskPage(View view) {

        //Observe the instance of the houseClass
        houseAction.addObserver(this);


        dueDate = (DatePicker) view.findViewById(R.id.datePicker1);
        dueDate.init(2011,7,17, null);
        dueTime = (TimePicker) view.findViewById(R.id.timePicker1);
        //dueTime.setHour(7);
        //dueTime.setMinute(15);

        //get the spinner from the xml.
        houseDropdown = view.findViewById(R.id.houses_spinner);
        memberDropdown = view.findViewById(R.id.assignee_spinner);
        notifDropdown = view.findViewById(R.id.notifications_spinner);
        tagDropdown = view.findViewById(R.id.tags_spinner);


        //Setup add field click listener
        Button addFieldBtn = (Button) view.findViewById(R.id.add_item_button);
        addFieldBtn.setOnClickListener(v -> {
            addField();
        });

        //Setup delete field listener as well
        /*
        Button removeFieldBtn = (Button) view.findViewById(R.id.add_item_button);
        addFieldBtn.setOnClickListener(v -> {
            addField();
        });
         */




        houseDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //create a list of items for the spinner.
                ArrayList<String> memOptions = new ArrayList<String>();

                Log.d("UPDATE MEMBER DROPDOWN", "I am putting members in dropdown");

                StringBuilder membersListString = new StringBuilder(" ");
                for (Map.Entry<String, houseMemberInfoObj> entry : houseAction.hInfos.get(position).members.entrySet()){
                    houseMemberInfoObj hMemberObj = entry.getValue();

                    membersListString.append(", ");
                    membersListString.append(hMemberObj.name);
                    memOptions.add(hMemberObj.getName());
                }

                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, memOptions);
                //set the spinners adapter to the previously created one.
                memberDropdown.setAdapter(adapter1);
                if(houseAction.hInfos.get(position).members.size()>0)
                    memberDropdown.setSelection(0);
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
    }


    /////////////////////////////////////////////////
    //
    // Observer update call
    //
    /////////////////////////////////////////////////
    @Override
    public void update(Observable o, Object arg) {
        this.updateHouses();
    }


    /////////////////////////////////////////////////
    //
    // Will add the houses to the houses dropdown
    //
    /////////////////////////////////////////////////
    private void updateHouses() {

        //Update the houses options and show the current house
        int houseIndex = 0;
        //create a list of items for the spinner.
        ArrayList<String> hOptions = new ArrayList<String>();

        //create an adapter to describe how the items are displayed
        for(int i = 0; i < houseAction.hInfos.size(); i++) {
            hOptions.add(houseAction.hInfos.get(i).displayName);

            if(tInfo.house_id.equals(houseAction.hInfos.get(i).id)) {
                houseIndex = i;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, hOptions);

        //set the spinners adapter to the previously created one.
        houseDropdown.setAdapter(adapter);
        houseDropdown.setSelection(houseIndex);



        //Show the members of that house
        int memberCount = 0;
        int memberArraySelected = 0;
        ArrayList<String> memOptions = new ArrayList<String>();
        for(String user_id : houseAction.hInfos.get(houseIndex).members.keySet()) {
            memOptions.add(houseAction.hInfos.get(houseIndex).members.get(user_id).name);

            String member_id = houseAction.hInfos.get(houseIndex).members.get(user_id).name;
            if(tInfo.assignedTo.containsKey(member_id))
                memberArraySelected = memberCount;

            memberCount++;
        }

        ArrayAdapter<String> memberAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, memOptions);
        //set the spinners adapter to the previously created one.
        memberDropdown.setAdapter(memberAdapter);
        memberDropdown.setSelection(memberArraySelected);
    }

    
    ////////////////////////////////////////////////////////////
    //
    // Will fill the view task window with new information
    //
    ////////////////////////////////////////////////////////////
    private void showTaskInfo(View view) {

        //displaying actual data
        TextView taskName = (TextView) view.findViewById(R.id.new_task_name);
        taskName.setText(tInfo.displayName);

        TextView description = (TextView) view.findViewById(R.id.new_task_description);
        description.setText(tInfo.description);

        //dueDate
        //dueTime

        //Begin actions to fill houses dropdown and memebers dropdown
        houseAction.viewYourHouses(uInfo);

        //create a list of items for the notifications spinner.
        setupNotificationSpinner(view);

        //Setup the tags available
        setUpTags();

        TextView cost =(TextView) view.findViewById(R.id.new_task_associated_cost);
        cost.setText(Double.toString(tInfo.costAssociated));

        TextView penalty =(TextView) view.findViewById(R.id.new_task_penalty);
        penalty.setText(Integer.toString(tInfo.difficultyScore));

        //Set the itemList
        setField(this.getView());
    }


    ////////////////////////////////////////////////////////////
    //
    // Will set the notification spinner values
    //
    ////////////////////////////////////////////////////////////
    private void setupNotificationSpinner(View view) {

        //create a list of items for the notifications spinner.
        ArrayList<String> nOptions =new ArrayList<String>();

        nOptions.add("None");
        nOptions.add("1 hour before due date");
        nOptions.add("1 day before due date");
        nOptions.add("1 week before due date");
        nOptions.add("1 month before due date");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, nOptions);
        //set the spinners adapter to the previously created one.
        notifDropdown.setAdapter(adapter2);

        //Need to get time of notifications relative to due date.
        notifDropdown.setSelection(0);
    }


    ////////////////////////////////////////////////////////////
    //
    // Will add lists to the page
    //
    ////////////////////////////////////////////////////////////
    private void addField() {
        FieldFrag field = new FieldFrag();
        fields.add(field);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.newTask_list, field);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    ////////////////////////////////////////////////////////////
    //
    // Will add list elements to the edit task page
    //
    ////////////////////////////////////////////////////////////
    private void setField(View view) {
        for(int i = 0; i < tInfo.itemList.size(); i++) {
            FieldFrag field = new FieldFrag();
            fields.add(field);

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.add(R.id.newTask_list, field);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

            //Set field item
            field.setItem(view, tInfo.itemList.get(i));
        }
    }

    ////////////////////////////////////////////////////////////
    //
    // Will add the tags to the page
    //
    ////////////////////////////////////////////////////////////
    private void setUpTags() {

        int currentTag = -1;

        //create a list of items for the notifications spinner.
        ArrayList<String> tagOptions = new ArrayList<String>();
        for(int tagNum = -1; tagNum <= 10; tagNum++) {
            String tagToAdd = tagMap.mapIntToString(tagNum);
            tagOptions.add(tagToAdd);

            if(tInfo.tag.contains(tagToAdd))
                currentTag = tagNum;
        }

        ArrayAdapter<String> tagAdaptor = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, tagOptions);

        //Set the spinner adaptor and current selected
        tagDropdown.setAdapter(tagAdaptor);
        tagDropdown.setSelection(currentTag);
    }

}