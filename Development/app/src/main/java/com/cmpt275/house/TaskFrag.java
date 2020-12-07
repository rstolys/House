package com.cmpt275.house;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.taskClass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFrag extends Fragment {

    userInfo uInfo;
    private final taskInfo tInfo;
    taskClass myTaskClass;

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
        // Get userInfo from last activity
        Intent lastIntent = getActivity().getIntent();
        String serializedObject = lastIntent.getStringExtra("userInfo");

        if(serializedObject == ""){
            // If the serialized object is empty, error!
            Log.e("OnCreate Task", "userInfo not passed from last activity");
        } else {
            try {
                // Decode the string into a byte array
                byte b[] = Base64.decode( serializedObject, Base64.DEFAULT );

                // Convert byte array into userInfo object
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                uInfo = (userInfo) si.readObject();
                Log.d("TASK_ACTIVITY", "Userinfo.displayName passed: " + uInfo.displayName );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //populating with db data
        TextView title = view.findViewById(R.id.task_name);
        title.setText(tInfo.displayName);

        TextView house = view.findViewById(R.id.house_name);
        house.setText(tInfo.houseName);

        TextView description = view.findViewById(R.id.new_task_description);
        description.setText(tInfo.description);

        TextView dueDate = view.findViewById(R.id.new_task_due_date);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        dueDate.setText(dateFormat.format(tInfo.dueDate));


        //complete task button
        Button completeTaskButton = view.findViewById(R.id.complete_task_button);
        completeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTaskClass.completeTask(tInfo);
                Toast.makeText(getActivity().getApplicationContext(),"Task Completed!", Toast.LENGTH_LONG).show();
                completeTaskButton.setText("Task Completed");
                return;
            }
        });

        //view task button
        Button viewTask = view.findViewById(R.id.view_task_button);
        viewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First prepare the userInfo to pass to next activity
                String serializedUserInfo = getSerializedUserInfo();
                String serializedTaskInfo = getSerializedTaskInfo();

                Intent buttonIntent = new Intent(getActivity(), TaskViewActivity.class);
                buttonIntent.putExtra("userInfo", serializedUserInfo);
                buttonIntent.putExtra("taskInfo", serializedTaskInfo);
                startActivity( buttonIntent );
            }
        });

        return view;
    }

    private String getSerializedUserInfo() {

        String serializedUserInfo = "";
        try {
            // Convert object data to encoded string
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(uInfo);
            so.flush();
            final byte[] byteArray = bo.toByteArray();
            serializedUserInfo = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        catch (IOException e) {
            e.printStackTrace();
            serializedUserInfo = "";
        }

        return serializedUserInfo;
    }

    private String getSerializedTaskInfo() {

        String serializedTaskInfo = "";
        try {
            // Convert object data to encoded string
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(tInfo);
            so.flush();
            final byte[] byteArray = bo.toByteArray();
            serializedTaskInfo = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        catch (IOException e) {
            e.printStackTrace();
            serializedTaskInfo = "";
        }

        return serializedTaskInfo;
    }
}