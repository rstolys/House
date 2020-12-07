package com.cmpt275.house;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.houseInfo;
import com.cmpt275.house.classDef.infoClass.userInfo;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseJoinHouseFrag} factory method to
 * create an instance of this fragment.
 */
public class HouseJoinHouseFrag extends DialogFragment implements Observer {
    private houseClass hClass;
    private userInfo uInfo;
    ArrayList<houseInfo> housesNotIn = new ArrayList<>();
    private final displayMessage display;

    public Spinner houseSelectionSpinner;

    public HouseJoinHouseFrag(userInfo uInfo) {
        hClass = new houseClass(null);
        hClass.addObserver(this);
        this.uInfo = uInfo;
        display = new displayMessage();
    }

    public static HouseJoinHouseFrag newInstance(Context mContext, userInfo uInfo) {
        HouseJoinHouseFrag fragment = new HouseJoinHouseFrag(uInfo);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get what houses the user is already in
        hClass.viewYourHouses(this.uInfo);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join_house, container, false);
        houseSelectionSpinner = view.findViewById(R.id.join_house_spinner);

        // Set up the confirmation button
        Button joinHouseButton = view.findViewById(R.id.join_house_confirmation);
        joinHouseButton.setOnClickListener(v->{
            int indexChosen = houseSelectionSpinner.getSelectedItemPosition();
            hClass.joinHouse(this.housesNotIn.get(indexChosen), uInfo);
        });

        // Get all the houses
        hClass.viewAllHouses();
        return view;
    }

    @Override
    public void update(Observable o, Object returnString) {
        Log.d("JOIN HOUSE FRAG UPDATE:", "Updated with return value: " + returnString);

        if( returnString.equals("viewAllHouses") ){
            // If the hInfo list is not null, set up the spinner to hold all the houses names
            if( hClass.hInfosAll != null){
                // Create a list of items for the spinner.
                ArrayList<String> houseNames = new ArrayList<String>();

                // All hInfos have been returned, add them to the spinner if the user is not in the house already
                for(int i = 0; i < hClass.hInfosAll.size(); i++){
                    boolean isAlreadyInHouse = false;
                    if(hClass.hInfos != null){
                        for(int j = 0; j < hClass.hInfos.size(); j++){
                            if(hClass.hInfosAll.get(i).id.equals(hClass.hInfos.get(j).id)){
                                isAlreadyInHouse = true;
                                break;
                            }
                        }
                    }
                    if(!isAlreadyInHouse) {
                        // Add names to the spinner
                        houseNames.add(hClass.hInfosAll.get(i).displayName);

                        // Add the houses the user is not in to an array
                        housesNotIn.add(hClass.hInfosAll.get(i));
                    }
                }

                // All houses that the user is not in are added as options. Add them to spinner
                ArrayAdapter<String> joinHouseAdaptor = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, houseNames);
                joinHouseAdaptor.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                houseSelectionSpinner.setAdapter(joinHouseAdaptor);
            }
        } else if( returnString.equals( "joinHouseRequest" ) ){
            // Join house request succeeded, close fragment
            display.showToastMessage(getActivity(), "Requested to Join House", display.LONG);
            this.dismiss();
        }
    }
}