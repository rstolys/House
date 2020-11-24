package com.cmpt275.house.classDef;

import android.content.Context;
import android.widget.Toast;

public class displayMessage {

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
    public void showMessage(Context mContext, String msg, int duration) {

        //If the call wants to use the default time
        if(duration == -1) {
            duration = LONG;
        }

        Toast toast = Toast.makeText(mContext, msg, duration);
        toast.show();
    }
}
