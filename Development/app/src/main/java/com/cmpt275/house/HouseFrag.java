package com.cmpt275.house;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.databaseObjects.houseMemberObj;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseFrag extends Fragment {
    private final houseInfo hInfo;
    private final int NUM_DISPLAYED_TASKS = 3;

    public HouseFrag(houseInfo hInfo) {
        this.hInfo = hInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_house, container, false);

        TextView title = view.findViewById(R.id.house1_name);
        title.setText(hInfo.displayName);

        TextView description = view.findViewById(R.id.house1_description);
        description.setText(hInfo.description);

        StringBuilder membersListString = new StringBuilder(" ");
        boolean firstMember = true;
        for (Map.Entry<String, houseMemberObj> entry : hInfo.members.entrySet()){
            houseMemberObj hMemberObj = entry.getValue();
            if(firstMember){
                membersListString.append(hMemberObj.name);
                firstMember = false;
                continue;
            }

            membersListString.append(", ");
            membersListString.append(hMemberObj.name);
        }
        TextView membersList = view.findViewById(R.id.house1_members_list);
        membersList.setText(membersListString.toString());

        // Show preview of the first three tasks of the house
        /*int[] listOfTaskTitleIds = new int[]{R.id.house1_task1_title, R.id.house1_task2_title, R.id.house1_task3_title};
        for(int taskNum = 0; taskNum < NUM_DISPLAYED_TASKS; taskNum++){
            for(Map.Entry<String, String> entry : hInfo.tasks.entrySet()) {
                TextView taskTitle = view.findViewById( listOfTaskTitleIds[taskNum] );
                taskTitle.setText( entry.getValue() );
            }
        }*/

        Button leaveHouseButton = view.findViewById(R.id.house1_leave_house_button);
        leaveHouseButton.setOnClickListener(v -> leaveHouseButton.setText("Leaving house"));

        return view;
    }
}