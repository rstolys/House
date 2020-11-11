package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class notificationMapping implements mapping {
    private Map<String, Integer> notificationStringToInt;
    private Map<Integer, String> notificationIntToString;

    //String Tags
    private final String NONE = "Request To Join";
    private final String WEEKLY = "Member";
    private final String MONTHLY = "Administrator";

    //Integer Tags
    private final Integer NONE_NUM = 0;
    private final Integer WEEKLY_NUM = 1;
    private final Integer MONTHLY_NUM = 2;


    ////////////////////////////////////////////////////////////
    //
    // Constructor will initialize maps for easy lookups
    //
    ////////////////////////////////////////////////////////////
    public notificationMapping() {
        //Setup the String to Int Map
        notificationStringToInt = new HashMap<String, Integer>();
        notificationStringToInt.put(NONE, NONE_NUM);
        notificationStringToInt.put(WEEKLY, WEEKLY_NUM);
        notificationStringToInt.put(MONTHLY, MONTHLY_NUM);

        //Setup the Int to String Map
        notificationIntToString = new HashMap<Integer, String>();
        notificationIntToString.put(NONE_NUM, NONE);
        notificationIntToString.put(WEEKLY_NUM, WEEKLY);
        notificationIntToString.put(MONTHLY_NUM, MONTHLY);
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
            intTags.add(notificationStringToInt.get(stringTags.get(i)));

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
            stringTags.add(notificationIntToString.get(intTags.get(i)));
        }

        return stringTags;
    }

    ////////////////////////////////////////////////////////////
    //
    // Will map an single value from String to Integer
    //
    ////////////////////////////////////////////////////////////
    public Integer mapStringToInt(String stringTag) {
        return notificationStringToInt.get(stringTag);
    }

    ////////////////////////////////////////////////////////////
    //
    // Will map an single value from Integer to String
    //
    ////////////////////////////////////////////////////////////
    public String mapIntToString(Integer intTag) {
        return notificationIntToString.get(intTag);
    }
}
