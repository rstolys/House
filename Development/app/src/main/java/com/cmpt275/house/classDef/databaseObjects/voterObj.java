package com.cmpt275.house.classDef.databaseObjects;

public class voterObj {

    //
    // Class Attributes
    //
    public String name;
    public boolean exists;
    public boolean yesVote;


    //
    // Class Functions
    //

    /////////////////////////////////////////////////////////
    //
    // Firebase constructor
    //
    /////////////////////////////////////////////////////////
    public voterObj() {}

    public voterObj(String name, boolean exists, boolean yesVote) {
        this.name = name;
        this.exists = exists;
        this.yesVote = yesVote;
    }

    public String getName() {
        return name;
    }

    public boolean getExists() {
        return exists;
    }

    public boolean getYesVote() {
        return exists;
    }
}
