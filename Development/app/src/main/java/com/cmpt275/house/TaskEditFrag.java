package com.cmpt275.house;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cmpt275.house.classDef.databaseObjects.taskAssignObj;
import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.mappingClass.statusMapping;
import com.cmpt275.house.classDef.mappingClass.tagMapping;
import com.cmpt275.house.classDef.taskClass;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_edit, container, false);

        //Assign global elements values
        setupEditTaskPage(view);

        //Load the task information to the page
        showTaskInfo(view);

        //Set back button
        Button backButton = (Button) view.findViewById(R.id.returnToViewTask);
        backButton.setOnClickListener(v -> this.dismiss());

        //Setup save button
        Button saveButton = (Button) view.findViewById(R.id.edit_task_save_button);
        saveButton.setOnClickListener(this::saveEditedTask);
        
        return view;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will save the task info and return to view activity
    //
    ////////////////////////////////////////////////////////////
    private void saveEditedTask(View v) {
        //Save task
        taskInfo newInfo = collectInfo(v);

        //Set task id;
        newInfo.id = tInfo.id;

        //Sanitize inputs to ensure values are valid
        if (newInfo.displayName.length() < 1) {
            display.showToastMessage(mContext, "Your task must have a name", display.SHORT);
        } else if (newInfo.dueDate.before(new Date()) || newInfo.dueDate.equals(new Date())) {
            display.showToastMessage(mContext, "The due date has already passed. Choose one in the future", display.SHORT);
        } else {
            display.showToastMessage(mContext, "Saving Task...", display.SHORT);

            boolean reassigned = newInfo.assignedTo.equals(tInfo.assignedTo);
            String oldAssignee = "";

            if(reassigned) {
                for(String id : tInfo.assignedTo.keySet()) {
                    oldAssignee = id;       //This works because  there is only 1 assignee
                }
            }


            taskAction.editTask(newInfo, uInfo, reassigned, oldAssignee, (tInfoRet, success, err) -> {
                if (success) {
                    ((TaskViewActivity) getActivity()).showTaskInfo(tInfoRet);

                    //Close the edit task dialog
                    this.dismiss();
                }
            });
        }
    }


    /////////////////////////////////////////////////
    //
    // Setup global UI elements to be set later
    //
    /////////////////////////////////////////////////
    private void setupEditTaskPage(View view) {

        //Observe the instance of the houseClass
        houseAction.addObserver(this);

        dueDate = (DatePicker) view.findViewById(R.id.edit_datePicker);
        dueTime = (TimePicker) view.findViewById(R.id.edit_timePicker);

        //get the spinner from the xml.
        houseDropdown = view.findViewById(R.id.edit_houses_spinner);
        memberDropdown = view.findViewById(R.id.edit_assignee_spinner);
        notifDropdown = view.findViewById(R.id.edit_notifications_spinner);
        tagDropdown = view.findViewById(R.id.edit_tags_spinner);


        //Setup add field click listener
        Button addFieldBtn = (Button) view.findViewById(R.id.add_item_button);
        addFieldBtn.setOnClickListener(v -> {
            addField();
        });


        houseDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //create a list of items for the spinner.
                ArrayList<String> memOptions = new ArrayList<String>();

                if(!houseAction.hInfos.get(position).id.equals(tInfo.house_id)) {
                    StringBuilder membersListString = new StringBuilder(" ");
                    for (Map.Entry<String, houseMemberInfoObj> entry : houseAction.hInfos.get(position).members.entrySet()){
                        houseMemberInfoObj hMemberObj = entry.getValue();

                        membersListString.append(", ");
                        membersListString.append(hMemberObj.name);
                        memOptions.add(hMemberObj.getName());
                    }

                    ArrayAdapter<String> assigneeAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, memOptions);
                    //set the spinners adapter to the previously created one.
                    memberDropdown.setAdapter(assigneeAdapter);
                    if(houseAction.hInfos.get(position).members.size() > 0)
                        memberDropdown.setSelection(0);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
    }


    ////////////////////////////////////////////////////////////
    //
    // Will fill the view task window with new information
    //
    ////////////////////////////////////////////////////////////
    private void showTaskInfo(View view) {

        //displaying actual data
        TextView taskName = (TextView) view.findViewById(R.id.edit_task_name);
        taskName.setText(tInfo.displayName);

        TextView description = (TextView) view.findViewById(R.id.edit_task_description);
        description.setText(tInfo.description);

        //Set the date pickers
        setDate();

        //Begin actions to fill houses dropdown and members dropdown
        houseAction.viewYourHouses(uInfo);

        //create a list of items for the notifications spinner.
        setupNotificationSpinner(view);

        //Setup the tags available
        setUpTags();

        TextView cost =(TextView) view.findViewById(R.id.edit_task_associated_cost);
        cost.setText(Double.toString(tInfo.costAssociated));

        TextView penalty =(TextView) view.findViewById(R.id.edit_task_penalty);
        penalty.setText(Integer.toString(tInfo.difficultyScore));

        //Set the itemList
        setField();
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

            if(tInfo.assignedTo.containsKey(user_id))
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

        ArrayAdapter<String> notifyAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, nOptions);
        //set the spinners adapter to the previously created one.
        notifDropdown.setAdapter(notifyAdapter);

        //Determine time for notification
        final int MS_IN_DAY = 86400000;
        final int MS_IN_HOUR = 3600000;

        long differenceInDate;
        if(tInfo.notificationTime == null)
            differenceInDate = -1;
        else
            differenceInDate = tInfo.notificationTime.getTime() - tInfo.dueDate.getTime();

        if(differenceInDate >= MS_IN_DAY*8) {
            notifDropdown.setSelection(4);      //Notify 1 month before
        }
        else if(differenceInDate >= (MS_IN_DAY + 100)) {
            notifDropdown.setSelection(3);      //Notify 1 week before
        }
        else if(differenceInDate >= (MS_IN_DAY - 100)) {
            notifDropdown.setSelection(2);      //Notify 1 day before
        }
        else if(differenceInDate >= (MS_IN_HOUR - 100)) {
            notifDropdown.setSelection(1);      //Notify 1 hour before
        }
        else {
            notifDropdown.setSelection(0);      //No notification
        }
    }


    ////////////////////////////////////////////////////////////
    //
    // Will add lists to the page
    //
    ////////////////////////////////////////////////////////////
    private void addField() {
        FieldFrag field = new FieldFrag();
        fields.add(field);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.editTask_list, field);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    ////////////////////////////////////////////////////////////
    //
    // Will add list elements to the edit task page
    //
    ////////////////////////////////////////////////////////////
    private void setField() {
        for(int i = 0; i < tInfo.itemList.size(); i++) {
            FieldFrag field = new FieldFrag(tInfo.itemList.get(i));
            fields.add(field);

            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.add(R.id.editTask_list, field);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
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
                currentTag = tagNum + 1;        //plus 1 since its an array and we started at -1
        }

        ArrayAdapter<String> tagAdaptor = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, tagOptions);

        //Set the spinner adaptor and current selected
        tagDropdown.setAdapter(tagAdaptor);
        tagDropdown.setSelection(currentTag);
    }


    ////////////////////////////////////////////////////////////
    //
    // Will set the date in the date and time pickers
    //
    ////////////////////////////////////////////////////////////
    private void setDate() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(tInfo.dueDate);

        //Set day of year
        dueDate.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH), null);

        //Set time
        dueTime.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        dueTime.setCurrentMinute(cal.get(Calendar.MINUTE));
    }


    ////////////////////////////////////////////////////////////
    //
    // Will set the date in the date and time pickers
    //
    ////////////////////////////////////////////////////////////
    private taskInfo collectInfo(View view) {
        taskInfo newTaskInfo =  new taskInfo();

        // Scrape data off UI EditText
        EditText taskTitle = getView().findViewById(R.id.edit_task_name);
        newTaskInfo.displayName = taskTitle.getText().toString();

        EditText taskDescription = getView().findViewById(R.id.edit_task_description);
        newTaskInfo.description = taskDescription.getText().toString();

        EditText penalty = getView().findViewById(R.id.edit_task_penalty);
        newTaskInfo.difficultyScore = Integer.parseInt(penalty.getText().toString());

        EditText cost = getView().findViewById(R.id.edit_task_associated_cost);
        newTaskInfo.costAssociated = Double.parseDouble(cost.getText().toString());

        Date setDueDate =  new GregorianCalendar(dueDate.getYear(), dueDate.getMonth(),
                dueDate.getDayOfMonth(), dueTime.getCurrentHour(),dueTime.getCurrentMinute()).getTime();
        newTaskInfo.dueDate = setDueDate;


        newTaskInfo.tag.add(0,tagDropdown.getSelectedItem().toString());

        newTaskInfo.houseName = houseAction.hInfos.get(houseDropdown.getSelectedItemPosition()).displayName;
        newTaskInfo.house_id = houseAction.hInfos.get(houseDropdown.getSelectedItemPosition()).id;

        //create a list of items from hash map
        ArrayList<String> namesMem = new ArrayList<String>();
        ArrayList<String> idMem = new ArrayList<String>();

        StringBuilder membersListString = new StringBuilder(" ");
        for (Map.Entry<String, houseMemberInfoObj> entry :
                houseAction.hInfos.get(houseDropdown.getSelectedItemPosition()).members.entrySet()){
            houseMemberInfoObj hMemberObj = entry.getValue();

            membersListString.append(", ");
            membersListString.append(hMemberObj.name);
            namesMem.add(hMemberObj.getName());
            idMem.add(entry.getKey());
        }

        newTaskInfo.assignedTo.put(idMem.get(memberDropdown.getSelectedItemPosition()),
                new taskAssignObj(namesMem.get(memberDropdown.getSelectedItemPosition()), true, true));

        //Don't change original creator
        newTaskInfo.createdBy = tInfo.createdBy;
        newTaskInfo.createdBy_id = tInfo.createdBy_id;
        
        newTaskInfo.status = statusMap.NOT_COMPLETE;

        newTaskInfo.itemList = getItemList();

        Date notifDate = setDueDate;
        Calendar calendar = Calendar.getInstance();

        switch (notifDropdown.getSelectedItemPosition()) {
            case 0:
                //no notifications -- notify date will be null
                notifDate = null;
                break;
            case 1:
                calendar.setTime(notifDate);
                calendar.add(Calendar.HOUR_OF_DAY, -1);
                notifDate = calendar.getTime();
                break;
            case 2:
                calendar.setTime(notifDate);
                calendar.add(Calendar.DATE, -1);
                notifDate = calendar.getTime();
                break;
            case 3:
                calendar.setTime(notifDate);
                calendar.add(Calendar.DATE, -7);
                notifDate = calendar.getTime();
                break;
            case 4:
                calendar.setTime(notifDate);
                calendar.add(Calendar.MONTH, -1);
                notifDate = calendar.getTime();
                break;
        }
        newTaskInfo.notificationTime = notifDate;

        //Return updated task
        return newTaskInfo;
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

}