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
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.mappingClass.statusMapping;

import java.util.Observable;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseViewTaskFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseViewTaskFrag extends Fragment implements Observer {

    private String viewingMemberId;
    private houseClass hClass;
    private Button actionButton;
    private TextView title;
    private taskInfo tInfo;

    private statusMapping statusMap = new statusMapping();

    public HouseViewTaskFrag() {
        // Required empty public constructor
    }

    public HouseViewTaskFrag(taskInfo tInfo, String viewingMemberId ) {
        this.viewingMemberId = viewingMemberId;
        this.hClass = new houseClass(getActivity());
        this.tInfo = tInfo;
        hClass.addObserver(this);
    }

    public static HouseViewTaskFrag newInstance(){
        HouseViewTaskFrag fragment = new HouseViewTaskFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_house_task, container, false);

        this.title = view.findViewById(R.id.view_house_view_task_name);
        title.setText(tInfo.displayName);

        // Show appropriate action based on who is assigned the class
        this.actionButton = view.findViewById(R.id.view_house_view_task_action_button);
        if(!tInfo.assignedTo.containsKey(viewingMemberId)) {
            actionButton.setText("Dispute");
            if(tInfo.status.equals(statusMap.COMPLETED)) {
                actionButton.setEnabled(true);
                actionButton.setOnClickListener(v->{
                    hClass.disputeTask(tInfo);
                });
            }
            else {
                actionButton.setEnabled(false);
            }
        }
        else {
            if(tInfo.status.equals(statusMap.COMPLETED)) {
                actionButton.setText("Complete");
                actionButton.setEnabled(false);
            }
            else {
                actionButton.setText("Extend");
                actionButton.setEnabled(true);
                actionButton.setOnClickListener(v->{
                    hClass.requestExtension(tInfo);
                });
            }
        }


        return view;
    }

    @Override
    public void update(Observable o, Object arg) {

        if(arg == "disputeTask"){
            actionButton.setVisibility(View.GONE);
            title.setText("Task Dispute Started");
            // Should delete this fragment and show up in votes
        } else if( arg == "requestExtension" ){
            actionButton.setVisibility(View.GONE);
            title.setText("Request Extension Vote Started");
            // User wants to request an extension for this task
            // Should delete this fragment for the
        }

    }
}