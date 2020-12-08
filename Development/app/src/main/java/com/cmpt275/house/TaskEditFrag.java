package com.cmpt275.house;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
import com.cmpt275.house.classDef.mappingClass.voteTypeMapping;
import com.cmpt275.house.classDef.taskClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


public class TaskEditFrag extends Fragment implements Observer {
    
    private Context mContext; 
    
    private taskInfo tInfo;
    private userInfo uInfo;
    private taskClass taskAction;
    private houseClass houseAction;
    
    private displayMessage display;
    private statusMapping statusMap;

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
    }


    ////////////////////////////////////////////////////////////
    //
    // Factory Method to create new account dialog
    //
    ////////////////////////////////////////////////////////////
    public static TaskEditFrag newInstance() {
        TaskEditFrag fragment = new TaskEditFrag();
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
        View view = inflater.inflate(R.layout.fragment_task_edit, container, false);

        //Assign global elements values
        setupEditTaskPage(view);

        //Load the task information to the page
        showTaskInfo(view);
        
        
        return view;
    }

    /////////////////////////////////////////////////
    //
    // Setup all the input options for creating a new task
    //
    /////////////////////////////////////////////////
    private void setupEditTaskPage(View view) {


        // Observe the instance of the houseClass
        houseAction.addObserver(this);

        // Update the houses in the houseClass from the database
        houseAction.viewYourHouses(uInfo);

        dueDate = (DatePicker) view.findViewById(R.id.datePicker1);
        dueTime = (TimePicker) view.findViewById(R.id.timePicker1);

        //get the spinner from the xml.
        houseDropdown = view.findViewById(R.id.houses_spinner);
        memberDropdown = view.findViewById(R.id.assignee_spinner);
        notifDropdown = view.findViewById(R.id.notifications_spinner);
        tagDropdown = view.findViewById(R.id.tags_spinner);

        //Set the itemList
        setField(view);

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

        //create a list of items for the notifications spinner.
        setupNotificationSpinner(view);




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

        //create a list of items for the spinner.
        ArrayList<String> hOptions = new ArrayList<String>();

        //create an adapter to describe how the items are displayed
        for(int i = 0; i < houseAction.hInfos.size(); i++) {
            hOptions.add(houseAction.hInfos.get(i).displayName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, hOptions);

        //set the spinners adapter to the previously created one.
        houseDropdown.setAdapter(adapter);
        if(houseAction.hInfos.size() > 0)
            houseDropdown.setSelection(0);

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

        //DatePicker dueDate = (DatePicker) view.findViewById(R.id.datePicker1);
        //TimePicker dueTime = (TimePicker) view.findViewById(R.id.timePicker1);

        TextView house = (TextView) view.findViewById(R.id.house_name);
        house.setText(tInfo.houseName);

        TextView assignee =  (TextView) view.findViewById(R.id.assignee_name);
        assignee.setText(uInfo.displayName);

        setupNotificationSpinner(view);

        TextView assignedBy =(TextView) view.findViewById(R.id.assigned_by_name);
        assignedBy.setText(tInfo.createdBy);

        TextView cost =(TextView) view.findViewById(R.id.task_cost);
        cost.setText(Double.toString(tInfo.costAssociated));

        TextView penalty =(TextView) view.findViewById(R.id.task_penalty);
        penalty.setText(Integer.toString(tInfo.difficultyScore));

        TextView list =(TextView) view.findViewById(R.id.task_list);

        String listString = "";
        for(int i=0 ; i< tInfo.itemList.size(); i++){
            listString += tInfo.itemList.get(i);
            if(tInfo.itemList.size()>1 && i!=tInfo.itemList.size()-1)
                listString+= ", ";
        }


        list.setText(listString);
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

            //Set field item
            field.setItem(view, tInfo.itemList.get(i));

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.add(R.id.newTask_list, field);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }

}