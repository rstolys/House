package com.cmpt275.house;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFrag extends Fragment {


    public void TaskFrag(){
        // Empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        Button completeTaskButton = view.findViewById(R.id.new_task_save_button);
        completeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeTaskButton.setText("Task Completed");
            }
        });

        return view;
    }
}