package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFrag extends Fragment {

    userInfo uInfo;
    private final taskInfo tInfo;

    public TaskFrag(taskInfo taskInfo) {
        this.tInfo = taskInfo;
        this.uInfo = null;
    }


    public void TaskFrag(){
        // Empty constructor
        this.uInfo = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        TextView title = view.findViewById(R.id.task_name);
        title.setText(tInfo.displayName);

        //complete task button
        Button completeTaskButton = view.findViewById(R.id.complete_task_button);
        completeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeTaskButton.setText("Task Completed");
            }
        });

        //view task button
        Button viewTask = view.findViewById(R.id.view_task_button);
        viewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First prepare the userInfo to pass to next activity
                String serializedUserInfo = "";
                try {
                    // Convert object data to encoded string
                    ByteArrayOutputStream bo = new ByteArrayOutputStream();
                    ObjectOutputStream so = new ObjectOutputStream(bo);
                    so.writeObject(uInfo);
                    so.flush();
                    final byte[] byteArray = bo.toByteArray();
                    serializedUserInfo = Base64.encodeToString(byteArray, Base64.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent buttonIntent = new Intent(getActivity(), TaskViewActivity.class);
                buttonIntent.putExtra("userInfo", serializedUserInfo);
                startActivity( buttonIntent );
            }
        });

        return view;
    }
}