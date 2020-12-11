package com.cmpt275.house;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.databaseObjects.houseMemberObj;
import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.mappingClass.roleMapping;
import com.google.android.gms.common.data.DataBufferObserver;

import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseFrag extends Fragment implements Observer {
    private houseInfo hInfo;
    private houseClass hClass; // No context here to pass, this could end up being a bug
    private userInfo uInfo;

    private roleMapping roleMap;

    public HouseFrag(houseInfo hInfo, houseClass houseClass, userInfo uInfo) {
        this.hInfo = hInfo;
        this.hClass = houseClass;
        this.uInfo = uInfo;

        roleMap = new roleMapping();

        hClass.addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_house, container, false);

        TextView title = view.findViewById(R.id.house1_name);
        if(Objects.requireNonNull(hInfo.members.get(uInfo.id)).role.equals(roleMap.REQUEST))
            title.setText(hInfo.displayName + " (Requesting)");
        else
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

        // Get task info positions
        TextView TaskName1 = view.findViewById(R.id.house1_task1_name);
        TextView TaskName2 = view.findViewById(R.id.house1_task2_name);
        TextView TaskName3 = view.findViewById(R.id.house1_task3_name);

        TableRow tr5 = view.findViewById(R.id.house_row5);
        tr5.setVisibility(View.GONE);
        TableRow tr6 = view.findViewById(R.id.house_row6);
        tr6.setVisibility(View.GONE);

        // Get all tasks for the user
        // For all users tasks, compare Id's, show the user their first three tasks
        int i = 1;
        if(this.hInfo.tasks != null){
            for( String houseTaskKey : this.hInfo.tasks.keySet() ){
                for( String userTaskKey : uInfo.tasks.keySet() ){
                    if(userTaskKey.equals(houseTaskKey)){
                        if(i == 1){
                            TaskName1.setText(" " + this.hInfo.tasks.get(houseTaskKey));
                        } else if (i==2){
                            TaskName2.setText(" " + this.hInfo.tasks.get(houseTaskKey));
                            tr5.setVisibility(View.VISIBLE);
                        } else if (i==3) {
                            TaskName3.setText(" " + this.hInfo.tasks.get(houseTaskKey));
                            tr6.setVisibility(View.VISIBLE);
                        }
                        i++;
                    }
                }
            }
        }

        // If no tasks in the house, display that
        if(i == 1){
            TaskName1.setText("You have no tasks in this house right now!");
            TaskName1.setGravity(View.TEXT_ALIGNMENT_CENTER);
            view.findViewById(R.id.house1_task1_title).setVisibility(View.GONE);
        }

        TextView membersList = view.findViewById(R.id.house1_members_list);
        membersList.setText(membersListString.toString());

        Button viewHouseButton = view.findViewById(R.id.view_house_button);
        if(Objects.requireNonNull(hInfo.members.get(uInfo.id)).role.equals(roleMap.REQUEST)) {
            viewHouseButton.setEnabled(false);
        }
        else {
            viewHouseButton.setOnClickListener(v -> {
                hClass.viewHouse(this.hInfo.id);
            });
        }


        Button leaveHouseButton = view.findViewById(R.id.house1_leave_house_button);
        leaveHouseButton.setOnClickListener(v -> {
            leaveHouseButton.setText("Leaving house");
            hClass.removeMember(hInfo, uInfo.id, uInfo.id);
        });

        return view;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg == "deleteHouse" || arg == "removeMember"){
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            hClass.updateUserInfo(uInfo.id, (success)->{
                Log.d("LEAVING HOUSE", "Updated userInfo on house leave");
            });
        }
    }
}