package com.cmpt275.house;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.cmpt275.house.classDef.displayMessage;
import com.cmpt275.house.classDef.signInClass;
import com.cmpt275.house.interfaceDef.signIn;


public class createAccountDialogFrag extends DialogFragment {

    private static final String TITLE = "Create Account";
    private static signIn auth;


    /////////////////////////////////////////////////
    //
    // Required Empty Constructor
    //
    /////////////////////////////////////////////////
    public createAccountDialogFrag() {
        // Required empty public constructor
    }


    /////////////////////////////////////////////////
    //
    // Factory Method to create new account dialog
    //
    /////////////////////////////////////////////////
    public static createAccountDialogFrag newInstance(Context mContext) {
        auth = new signInClass(null, mContext);

        createAccountDialogFrag fragment = new createAccountDialogFrag();
        Bundle args = new Bundle();
        return fragment;
    }

    /////////////////////////////////////////////////
    //
    // Will specify the layout for the dialog to use
    //
    /////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Show the layout for the defined fragment in the xml file
        return inflater.inflate(R.layout.fragment_create_account, container);
    }

    /////////////////////////////////////////////////
    //
    // Will create an account and close the dialog
    //
    /////////////////////////////////////////////////
    public void createAccount(View view) {

        //get input values
        try {
            EditText displayName_Val = getView().findViewById(R.id.create_displayName);
            EditText email_Val = getView().findViewById(R.id.create_email);
            EditText password_Val = getView().findViewById(R.id.create_password);

            String displayName = displayName_Val.getText().toString();
            String email = email_Val.getText().toString();
            String password = password_Val.getText().toString();

            Log.d("createAccount:", "Creating an account for: " + displayName);
            Log.d("createAccount:", "Parameters -- Email: " + email + " Password: " + password);

            //Create account
            auth.createAccount(displayName, email, password);
            //User notified from create account
        }
        catch(Exception e) {
            Log.d("createAccount:", "ERROR OCCURRED:  " + e.getMessage());
        }

        //Close the dialog
        this.dismiss();
    }


    /////////////////////////////////////////////////
    //
    // Will hide the keyboard on the call
    //
    /////////////////////////////////////////////////
    public void hideKeyboard(View view) {

        //Hide the keyboard from the user
        if (view != null) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

}