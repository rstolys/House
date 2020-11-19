package com.cmpt275.house.classDef.databaseObjects;

public class houseMemberObj {
    //
    // Class Attributes
    //
    public String name;
    public boolean exists;
    public Integer role;


    //
    // Class Functions
    //

    /////////////////////////////////////////////////////////
    //
    // Firebase constructor
    //
    /////////////////////////////////////////////////////////
    public houseMemberObj() {}

    public houseMemberObj(String name, boolean exists, int role) {
        this.name = name;
        this.exists = exists;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public boolean getExists() {
        return exists;
    }

    public Integer getRole() {
        return role;
    }
}
