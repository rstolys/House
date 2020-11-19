package com.cmpt275.house;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cmpt275.house.classDef.taskClass;
import com.cmpt275.house.classDef.taskInfo;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link newTask#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTaskFrag extends Fragment {

    public taskInfo newTaskInfo = new taskInfo();
    taskClass theTaskClass;

    public NewTaskFrag(taskClass myTaskClass) {
        // Attain the house class for this running of the houses activity
        theTaskClass = myTaskClass;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_task, container, false);

        Button saveButton = view.findViewById(R.id.new_task_save_button);
        saveButton.setOnClickListener(v -> {
            EditText taskTitle = view.findViewById(R.id.new_task_name);
            newTaskInfo.displayName = String.valueOf(taskTitle.getText());

            EditText taskDescription = view.findViewById(R.id.new_task_description);
            newTaskInfo.description = String.valueOf(taskDescription);
        });

        return view;
    }
}