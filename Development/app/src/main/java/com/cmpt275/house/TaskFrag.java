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

import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.mappingClass.statusMapping;
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

    private userInfo uInfo;
    private taskInfo tInfo;
    private int taskIndex = -1;

    private taskClass myTaskClass;

    private displayMessage display = new displayMessage();
    private statusMapping statusMap = new statusMapping();


    /////////////////////////////////////////////////
    //
    // Task fragment constructor
    //
    /////////////////////////////////////////////////
    public TaskFrag(taskInfo taskInfo, userInfo uInfo, int taskIndex, taskClass myTaskClass) {
        this.tInfo = taskInfo;
        this.uInfo = uInfo;
        this.taskIndex = taskIndex;
        this.myTaskClass = myTaskClass;
    }


    /////////////////////////////////////////////////
    //
    // Required empty constructor
    //
    /////////////////////////////////////////////////
    public void TaskFrag() {}


    ////////////////////////////////////////////////////////////
    //
    // Will specify the layout for the fragment
    //
    ////////////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        /*
        // Get userInfo from last activity
        Intent lastIntent = getActivity().getIntent();
        String serializedObject = lastIntent.getStringExtra("userInfo");

        if(serializedObject.equals("")){
            // If the serialized object is empty, error!
            Log.e("OnCreate Task", "userInfo not passed from last activity");
        }
        else {
            try {
                // Decode the string into a byte array
                byte[] b = Base64.decode( serializedObject, Base64.DEFAULT );

                // Convert byte array into userInfo object
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                uInfo = (userInfo) si.readObject();
                Log.d("TASK_ACTIVITY", "Userinfo.displayName passed: " + uInfo.displayName );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         */

        //populating screen with task information
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
        if(tInfo.status.equals(statusMap.COMPLETED)) {
            completeTaskButton.setText("Completed");
        }
        else {
            completeTaskButton.setOnClickListener(v -> {
                display.showToastMessage(getActivity().getApplicationContext(), "Completing Task...", display.LONG);

                myTaskClass.completeTask(tInfo, taskIndex, result -> {
                    completeTaskButton.setText("Completed");
                    ((TaskActivity)getActivity()).updateTasks();
                });
            });
        }



        //view task button
        Button viewTask = view.findViewById(R.id.view_task_button);
        viewTask.setOnClickListener(v -> {

            // First prepare the userInfo to pass to next activity
            String serializedUserInfo = getSerializedUserInfo();
            String serializedTaskInfo = getSerializedTaskInfo();

            Intent buttonIntent = new Intent(getActivity(), TaskViewActivity.class);
            buttonIntent.putExtra("userInfo", serializedUserInfo);
            buttonIntent.putExtra("taskInfo", serializedTaskInfo);
            startActivity( buttonIntent );
        });

        return view;
    }


    /////////////////////////////////////////////////
    //
    // Get the serialized user info to pass between activities
    //
    /////////////////////////////////////////////////
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


    /////////////////////////////////////////////////
    //
    // Get the serialized task info to pass between activities
    //
    /////////////////////////////////////////////////
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