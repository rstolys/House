package com.cmpt275.house.classDef.databaseObjects;

public class nameObj {

    //
    // Class Attributes
    //
    public String name;
    public boolean exists;

    //
    // Class Functions
    //

    /////////////////////////////////////////////////////////
    //
    // Firebase constructor
    //
    /////////////////////////////////////////////////////////
    public nameObj() {}

    public nameObj(String name, boolean exists) {
        this.name = name;
        this.exists = exists;
    }

    public String getName() {
        return name;
    }

    public boolean getExists() {
        return exists;
    }
}
