package com.cmpt275.house;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cmpt275.house.classDef.infoClass.houseMemberInfoObj;
import com.cmpt275.house.classDef.mappingClass.roleMapping;

public class HouseViewMemberFrag extends Fragment {
    private String context; // Context we are viewing this from
    private houseMemberInfoObj member; // Member info to be displayed
    private roleMapping rm = new roleMapping(); // Get a role mapping
    private houseMemberInfoObj viewer; // User who is viewing the fragment

    public HouseViewMemberFrag(String viewContext, houseMemberInfoObj houseMember, houseMemberInfoObj hmio) {
        this.context = viewContext;
        this.member = houseMember;
        this.viewer = hmio;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_house_member, container, false);

        if(this.context == "viewHouse") {
            TextView name = view.findViewById(R.id.view_house_voteType);
            name.setText(this.member.name);

            Button changeMemberRoleButton = view.findViewById(R.id.view_house_member_button);
            changeMemberRoleButton.setVisibility(View.GONE);

            TextView bonusInfo = view.findViewById(R.id.view_house_member_amount_owed);
            // TODO: Get user $$$ amount added to houseMemberInfoObj
            bonusInfo.setText("0");
        } else if(this.context == "editHouse"){
            TextView name = view.findViewById(R.id.view_house_voteType);
            name.setText(this.member.name);

            TextView bonusInfo = view.findViewById(R.id.view_house_member_amount_owed);
            bonusInfo.setText(member.role);

            Button changeMemberRoleButton = view.findViewById(R.id.view_house_member_button);
            // Only admins can change the status of people in the house
            if(viewer.role == rm.ADMIN ) {
                if (member.role == rm.REQUEST) { // Admins can let in people who request to join at any time
                    changeMemberRoleButton.setText("Let Join");
                } else if( member.name == viewer.name ){ // Viewer can leave a house at any time
                    changeMemberRoleButton.setText("Leave");
                } else if (member.role == rm.ADMIN) { // All other admins can be made members
                    changeMemberRoleButton.setText("Make Member");
                } else { // Any member can be made admin
                    changeMemberRoleButton.setText("Make Admin");
                }
            } else { // Else we set change member role invisible if it is not for themselves.
                if(member.name != viewer.name){
                    changeMemberRoleButton.setVisibility(View.GONE);
                } else {
                    changeMemberRoleButton.setText("Leave House");
                }
            }

            // Set the onclick listener for the member role button
            changeMemberRoleButton.setOnClickListener(v -> {
                // If button is clicked change the text on the button and the bonus info. This will only be saved on "save button" click
                String action = changeMemberRoleButton.getText().toString();

                if (action == "Let Join" || action == "Make Member"){
                    bonusInfo.setText(rm.MEMBER);
                    changeMemberRoleButton.setText("Make Admin");
                    member.role = rm.MEMBER;

                } else if( action == "Make Admin" ){
                    bonusInfo.setText(rm.ADMIN);
                    changeMemberRoleButton.setText("Remove");
                    member.role = rm.ADMIN;

                } else if( action == "Leave" || action == "Remove"){
                    changeMemberRoleButton.setText("Make Member");
                    bonusInfo.setText("Removed");
                    member.role = rm.REMOVE;
                }
            });
        }

        return view;
    }
}
