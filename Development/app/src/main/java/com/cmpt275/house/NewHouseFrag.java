package com.cmpt275.house;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.mappingClass.notificationMapping;
import com.cmpt275.house.classDef.mappingClass.roleMapping;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link //newHouse#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewHouseFrag extends Fragment {

    public houseInfo newHouseInfo = new houseInfo();
    houseClass theHouseClass;
    String houseCreatorName;
    userInfo uInfo;
    displayMessage display;

    public NewHouseFrag(houseClass myHouseClass, userInfo uInfo) {
        // Attain the house class for this running of the houses activity
         theHouseClass = myHouseClass;
         this.uInfo = uInfo;
         this.houseCreatorName = uInfo.displayName;
         display = new displayMessage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_house, container, false);

        TextView membersList =  view.findViewById(R.id.new_house_members_list);
        membersList.setText(" " + houseCreatorName);

        Spinner notifSchedInput = view.findViewById((R.id.new_house_notification_sched));

        ArrayList<String> notifOptions = new ArrayList<String>();
        notifOptions.add("None");
        notifOptions.add("Weekly");
        notifOptions.add("Monthly");

        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, notifOptions);
        // Set the spinner's options
        notifSchedInput.setAdapter(adapter);
        notifSchedInput.setSelection(0);

        Button saveButton = view.findViewById(R.id.new_house_save_button);
        saveButton.setOnClickListener(v -> {
            boolean goodData = true;

            // Scrape data off UI
            EditText houseTitle = view.findViewById(R.id.new_house_name);
            if(houseTitle.getText().toString() != ""){
                newHouseInfo.displayName = String.valueOf(houseTitle.getText());
            } else {
                goodData = false;
            }

            // Get the house description
            EditText houseDescription = view.findViewById(R.id.new_house_description);
            if(houseDescription.getText().toString() != ""){
                newHouseInfo.description = String.valueOf(houseDescription.getText());
            } else{
                goodData = false;
            }

            final roleMapping roleMap = new roleMapping();
            newHouseInfo.members.put(uInfo.id, new houseMemberInfoObj(uInfo.displayName, roleMap.ADMIN));

            // Get the punishment multiplier for the house
            EditText punishMult;
            punishMult = view.findViewById(R.id.new_house_punish_mult);
            if(!punishMult.getText().toString().equals("")){
                String newString = String.valueOf(punishMult.getText());
                newHouseInfo.punishmentMultiplier = (int) Double.parseDouble(newString);
            } else{
                goodData = false;
            }

            // Get notification selection
            final notificationMapping notificationMap = new notificationMapping();
            String notifSched = notifSchedInput.getSelectedItem().toString();

            if (notifSched.equals("Weekly") || notifSched.equals("weekly")) {
                newHouseInfo.houseNotifications = notificationMap.WEEKLY;
            } else if (notifSched.equals("Monthly") || notifSched.equals("monthly")) {
                newHouseInfo.houseNotifications = notificationMap.MONTHLY;
            } else {
                newHouseInfo.houseNotifications = notificationMap.NONE;
            }

            // Check that the inputs were correct by the user
            if(goodData) {
                saveButton.setText("Creating House");

                Log.d("createHouseButton", "Creating house in db with name: " + newHouseInfo.displayName);
                display.showToastMessage(getActivity(), "Creating House", display.LONG);

                theHouseClass.createHouse(newHouseInfo);
            } else {
                display.showToastMessage(getActivity(), "Oops. Looks like you are missing some data. Enter that before submitting", display.LONG);
            }

        });

        return view;
    }
}