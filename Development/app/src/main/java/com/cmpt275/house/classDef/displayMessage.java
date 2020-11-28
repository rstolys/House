package com.cmpt275.house.classDef;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.cmpt275.house.R;
import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;

public class displayMessage extends DialogFragment {

    //
    // Class Attributes
    //
    public final int SHORT = Toast.LENGTH_SHORT;
    public final int LONG = Toast.LENGTH_LONG;

    //
    // Class Functions
    //
    ////////////////////////////////////////////////////////////
    //
    // Constructor
    //
    ////////////////////////////////////////////////////////////
    public displayMessage() {}


    ////////////////////////////////////////////////////////////
    //
    // Will display an error message to the user
    //
    ////////////////////////////////////////////////////////////
    public void showToastMessage(Context mContext, String msg, int duration) {

        //If the call wants to use the default time
        if(duration == -1) {
            duration = LONG;
        }

        Toast toast = Toast.makeText(mContext, msg, duration);
        toast.show();
    }


    ////////////////////////////////////////////////////////////
    //
    // Will create a two button alert for the user
    //
    ////////////////////////////////////////////////////////////
    public void createTwoBtnAlert(Context mContext, String title, String msg, String positiveBtn, String negativeBtn, booleanCallback callback) {
        //Create the dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        // Set the message and title of the dialogue
        builder.setMessage(msg).setTitle(title);

        //Set the buttons of the dialogue
        builder.setPositiveButton(positiveBtn, (dialog, id) -> {
            callback.onReturn(true, "");
            dialog.cancel();
        });
        builder.setNegativeButton(negativeBtn, (dialog, id) -> {
            callback.onReturn(false, "");
            dialog.cancel();
        });

        //Create the dialogue
        AlertDialog dialog = builder.create();

        //Show to dialogue
        dialog.show();

        //Set button positioning
        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        layoutParams.setMargins(10, 0, 10, 0);
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }
}
