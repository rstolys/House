package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class roleMapping implements mapping {
    private Map<String, Integer> roleStringToInt;
    private Map<Integer, String> roleIntToString;

    //String Tags
    public final String REQUEST = "Request To Join";
    public final String MEMBER = "Member";
    public final String ADMIN = "Administrator";

    //Integer Tags
    public final Integer REQUEST_NUM = 0;
    public final Integer MEMBER_NUM = 1;
    public final Integer ADMIN_NUM = 2;


    ////////////////////////////////////////////////////////////
    //
    // Constructor will initialize maps for easy lookups
    //
    ////////////////////////////////////////////////////////////
    public roleMapping() {
        //Setup the String to Int Map
        roleStringToInt = new HashMap<String, Integer>();
        roleStringToInt.put(REQUEST, REQUEST_NUM);
        roleStringToInt.put(MEMBER, MEMBER_NUM);
        roleStringToInt.put(ADMIN, ADMIN_NUM);

        //Setup the Int to String Map
        roleIntToString = new HashMap<Integer, String>();
        roleIntToString.put(REQUEST_NUM, REQUEST);
        roleIntToString.put(MEMBER_NUM, MEMBER);
        roleIntToString.put(ADMIN_NUM, ADMIN);
    }


    ////////////////////////////////////////////////////////////
    //
    // Will map an entire list from String to Integer
    //
    ////////////////////////////////////////////////////////////
    public List<Integer> mapList_StringToInt(List<String> stringTags) {
        //Create an integer list
        List<Integer> intTags = new ArrayList<Integer>(stringTags.size());

        for(int i = 0; i < stringTags.size(); i++)
            intTags.add(roleStringToInt.get(stringTags.get(i)));

        return intTags;
    }


    ////////////////////////////////////////////////////////////
    //
    // Will map an entire list from Integer to String
    //
    ////////////////////////////////////////////////////////////
    public List<String> mapList_IntToString(List<Integer> intTags) {
        //Create an integer list
        List<String> stringTags = new ArrayList<String>(intTags.size());

        for(int i = 0; i < intTags.size(); i++) {
            stringTags.add(roleIntToString.get(intTags.get(i)));
        }

        return stringTags;
    }

    ////////////////////////////////////////////////////////////
    //
    // Will map an single value from String to Integer
    //
    ////////////////////////////////////////////////////////////
    public Integer mapStringToInt(String stringTag) {
        return roleStringToInt.get(stringTag);
    }

    ////////////////////////////////////////////////////////////
    //
    // Will map an single value from Integer to String
    //
    ////////////////////////////////////////////////////////////
    public String mapIntToString(Integer intTag) {
        return roleIntToString.get(intTag);
    }
}
