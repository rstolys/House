package com.cmpt275.house;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.houseInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newHouse#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewHouseFrag extends Fragment {

    public houseInfo newHouseInfo = new houseInfo();

    public NewHouseFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_house, container, false);

        Button saveButton = view.findViewById(R.id.new_house_save_button);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               EditText houseTitle = view.findViewById(R.id.new_house_name);
               newHouseInfo.name = String.valueOf(houseTitle.getText());

               EditText houseDescription = view.findViewById(R.id.new_house_description);
               newHouseInfo.description = String.valueOf(houseDescription);
            }
        });

        return view;
    }
}