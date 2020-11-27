package com.cmpt275.house;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.databaseObjects.houseMemberObj;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.google.android.gms.common.data.DataBufferObserver;

import java.util.Map;
import java.util.Observable;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseFrag extends Fragment {
    private houseInfo hInfo;
    private final int NUM_DISPLAYED_TASKS = 3;
    private houseClass hClass; // No context here to pass, this could end up being a bug

    public HouseFrag(houseInfo hInfo, houseClass houseClass) {
        this.hInfo = hInfo;
        this.hClass = houseClass;
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
        for (Map.Entry<String, houseMemberInfoObj> entry : hInfo.members.entrySet()){
            houseMemberInfoObj hMemberObj = entry.getValue();
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

        Button viewHouseButton = view.findViewById(R.id.view_house_button);
        viewHouseButton.setOnClickListener(v -> {
            hClass.viewHouse(this.hInfo.id);
        });

        Button leaveHouseButton = view.findViewById(R.id.house1_leave_house_button);
        leaveHouseButton.setOnClickListener(v -> leaveHouseButton.setText("Leaving house"));

        return view;
    }
}