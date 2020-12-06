package com.cmpt275.house;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.houseClass;
import com.cmpt275.house.classDef.infoClass.userInfo;
import com.cmpt275.house.classDef.infoClass.votingInfo;
import com.cmpt275.house.classDef.mappingClass.voteTypeMapping;
import com.cmpt275.house.classDef.signInClass;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseViewVotingFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseViewVotingFrag extends Fragment {

    private Context mContext;

    private votingInfo vInfo;
    private int voteIndex = -1;
    private userInfo uInfo;
    private String buttonValue = "";

    private voteTypeMapping voteMap;
    private displayMessage display;
    private houseClass houseAction;

    private final String VIEW = "View";
    private final String VOTE = "Vote";


    ////////////////////////////////////////////////////////////
    //
    // Required empty public constructor
    //
    ////////////////////////////////////////////////////////////
    public HouseViewVotingFrag() {}

    ////////////////////////////////////////////////////////////
    //
    // Constructor to set class values to allow frag to be updated
    //
    ////////////////////////////////////////////////////////////
    public HouseViewVotingFrag(Context mContext, votingInfo vInfo, int voteIndex, userInfo uInfo, houseClass houseAction) {
        this.mContext = mContext;
        this.voteIndex = voteIndex;
        this.vInfo = vInfo;
        this.uInfo = uInfo;

        voteMap = new voteTypeMapping();
        display = new displayMessage();
        this.houseAction = houseAction;
    }


    ////////////////////////////////////////////////////////////
    //
    // Factory Method to create new account dialog
    //
    ////////////////////////////////////////////////////////////
    public static HouseViewVotingFrag newInstance() {
        HouseViewVotingFrag fragment = new HouseViewVotingFrag();
        Bundle args = new Bundle();
        return fragment;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will specify the layout for the dialog to use, set values
    //
    ////////////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_house_vote, container, false);

        //Set the title of the vote
        TextView voteType = view.findViewById(R.id.view_house_voteType);
        voteType.setText(vInfo.taskName + ": " + vInfo.type);

        //Set the button action and display text
        Button voteAction = view.findViewById(R.id.view_house_voteButton);
        if(vInfo.voters.containsKey(uInfo.id)) {
            buttonValue = VIEW;
        }
        else {
            buttonValue = VOTE;
        }
        voteAction.setText(buttonValue);

        //Set click listener when button is clicked
        voteAction.setOnClickListener(this::voteButtonClicked);


        return view;
    }


    ////////////////////////////////////////////////////////////
    //
    // On button click, show alert dialog
    //
    ////////////////////////////////////////////////////////////
    public void voteButtonClicked(View view) {
        //Depending on the button value, show user alert dialog
        String title = "";
        String message = "";
        String positiveBtn = "";
        String negativeBtn = "";

        boolean error = false;

        if(buttonValue.equals(VIEW)) {
            title = "View Votes";
            message = "Yes Votes: " + vInfo.yesVotes;
            message += "\nNo Votes: " + vInfo.noVotes;
            positiveBtn = "Close";
            negativeBtn = "Close";
        }
        else if(buttonValue.equals(VOTE)) {
            title = "Submit Vote";

            message = "Do you agree that task " + vInfo.taskName + " should be";
            if(vInfo.type.equals(voteMap.DEADLINE_EXTENSION)) {
                message += " granted a deadline extension?";
            }
            else {
                message += " completed again?";
            }

            positiveBtn = "Yes";
            negativeBtn = "No";
        }
        else {
            error = true;
        }

        //Show alert dialog for user or display error message
        if(error) {
            display.showToastMessage(mContext, "Looks like something went wrong there. Sorry!", display.LONG);
        }
        else {
            display.createTwoBtnAlert(mContext, title, message, positiveBtn, negativeBtn, (result, errorMessage) -> {

                if(buttonValue.equals(VOTE)) {
                    //Submit the user vote
                    houseAction.submitVote(vInfo, uInfo, result, voteIndex, (vInfoRet, success, err) -> {
                        if(success) {
                            //Update the vInfo
                            this.vInfo = vInfoRet;

                            //Change the button value to view
                            Button voteAction = view.findViewById(R.id.view_house_voteButton);
                            buttonValue = VIEW;
                            voteAction.setText(buttonValue);
                        }
                    });
                }
            });
        }
    }
}