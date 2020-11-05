package com.cmpt275.house.classDef;

import java.io.Serializable;

public class feedbackInfo implements Serializable {
    public String id;
    public int user_id;
    public String feedback;
    public int date;                //Date in ms since epoch -- can use java Date
}
