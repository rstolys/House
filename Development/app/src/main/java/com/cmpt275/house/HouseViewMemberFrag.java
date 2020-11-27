package com.cmpt275.house;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.VerifiedInputEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class HouseViewMemberFrag extends Fragment {
    private String memberName;
    private String amountOwing;

    public HouseViewMemberFrag(String memberName, String amountOwing) {
        this.memberName = memberName;
        this.amountOwing = amountOwing;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_house_member, container, false);

        TextView name = view.findViewById(R.id.view_house_member_name);
        name.setText(this.memberName);

        TextView owing = view.findViewById(R.id.view_house_member_amount_owed);
        owing.setText(amountOwing);

        return view;
    }
}
