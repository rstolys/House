package com.cmpt275.house.classDef.databaseObjects;

import java.io.Serializable;

public class taskAssignObj implements Serializable {

    //
    // Class Attributes
    //
    public String name;
    public boolean exists;
    public boolean approved;


    //
    // Class Functions
    //

    /////////////////////////////////////////////////////////
    //
    // Firebase constructor
    //
    /////////////////////////////////////////////////////////
    public taskAssignObj() {}

    public taskAssignObj(String name, boolean exists, boolean approved) {
        this.name = name;
        this.exists = exists;
        this.approved = approved;
    }

    public String getName() {
        return name;
    }

    public boolean getExists() {
        return exists;
    }

    public boolean getApproved() {
        return approved;
    }
}
