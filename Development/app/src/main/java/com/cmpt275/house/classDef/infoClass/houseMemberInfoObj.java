package com.cmpt275.house.classDef.infoClass;

public class houseMemberInfoObj {
    //
    // Class Attributes
    //
    public String name;
    public String role;


    //
    // Class Functions
    //

    /////////////////////////////////////////////////////////
    //
    // Firebase constructor
    //
    /////////////////////////////////////////////////////////
    public houseMemberInfoObj() {}

    public houseMemberInfoObj(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
