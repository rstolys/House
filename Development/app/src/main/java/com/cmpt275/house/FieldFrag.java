package com.cmpt275.house;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cmpt275.house.classDef.infoClass.taskInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.taskClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class FieldFrag extends Fragment {

    public String itemVar;


    /////////////////////////////////////////////////
    //
    // constructor
    //
    /////////////////////////////////////////////////

    public FieldFrag() {

    }



    ////////////////////////////////////////////////////////////
    //
    // Will specify the layout for the fragment
    //
    ////////////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.task_list_field, container, false);

       return view;
    }

    public String getItem(){
        EditText item = getView().findViewById(R.id.new_list_item);
        itemVar = item.getText().toString();
        return itemVar;
    }

}
