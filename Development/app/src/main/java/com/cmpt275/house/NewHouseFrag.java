package com.cmpt275.house;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmpt275.house.classDef.databaseObjects.houseMemberObj;
import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.mappingClass.notificationMapping;
import com.cmpt275.house.classDef.mappingClass.roleMapping;
import com.google.firebase.auth.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newHouse#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewHouseFrag extends Fragment {

    public houseInfo newHouseInfo = new houseInfo();
    houseClass theHouseClass;
    String houseCreatorName;
    userInfo uInfo;

    public NewHouseFrag(houseClass myHouseClass, userInfo uInfo) {
        // Attain the house class for this running of the houses activity
         theHouseClass = myHouseClass;
         this.uInfo = uInfo;
         this.houseCreatorName = uInfo.displayName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_house, container, false);

        TextView membersList =  view.findViewById(R.id.new_house_members_list);
        membersList.setText(" " + houseCreatorName);

        Button saveButton = view.findViewById(R.id.new_house_save_button);
        saveButton.setOnClickListener(v -> {

            // Scrape data off UI
            EditText houseTitle = view.findViewById(R.id.new_house_name);
            newHouseInfo.displayName = String.valueOf(houseTitle.getText());

            EditText houseDescription = view.findViewById(R.id.new_house_description);
            newHouseInfo.description = String.valueOf(houseDescription.getText());

            final roleMapping roleMap = new roleMapping();
            newHouseInfo.members.put(uInfo.id, new houseMemberObj(uInfo.displayName, true, roleMap.ADMIN_NUM));

            EditText punishMult;

            punishMult = view.findViewById(R.id.new_house_punish_mult);
            String newString = String.valueOf(punishMult.getText());
            newHouseInfo.punishmentMultiplier = (int) Double.parseDouble(newString);

            EditText notifSchedInput = view.findViewById((R.id.new_house_notification_sched));
            String notifSched = String.valueOf(notifSchedInput);
            final notificationMapping notificationMap = new notificationMapping();
            if( notifSched.equals("Weekly") || notifSched.equals("weekly") ){
                newHouseInfo.houseNotifications = notificationMap.WEEKLY;
            } else if( notifSched.equals("Monthly") || notifSched.equals("monthly") ){
                newHouseInfo.houseNotifications = notificationMap.MONTHLY;
            } else {
                newHouseInfo.houseNotifications = notificationMap.NONE;
            }

            saveButton.setText("Creating House");

            Log.d("createHouseButton", "Creating house in db with name: " + newHouseInfo.displayName);

            theHouseClass.createHouse(newHouseInfo);
        });

        return view;
    }
}