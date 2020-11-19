package com.cmpt275.house.classDef.mappingClass;

import com.cmpt275.house.interfaceDef.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class statusMapping implements mapping {
    private Map<String, Integer> statusStringToInt;
    private Map<Integer, String> statusIntToString;

    //String Tags
    public final String NOT_SET = "Not Set";
    public final String NOT_COMPLETE = "Not Complete";
    public final String COMPLETED = "Completed";
    public final String LATE = "Late";
    public final String DISPUTE = "Disputed";
    public final String REASSIGNMENT_APPROVAL = "Reassignment Being Approved";
    public final String SWAP_APPROVAL = "Swap Being Approved";

    //Integer Tags
    public final Integer NOT_SET_NUM = -1;
    public final Integer NOT_COMPLETE_NUM = 1;
    public final Integer COMPLETED_NUM = 2;
    public final Integer LATE_NUM = 3;
    public final Integer DISPUTE_NUM = 4;
    public final Integer REASSIGNMENT_APPROVAL_NUM = 5;
    public final Integer SWAP_APPROVAL_NUM = 6;


    ////////////////////////////////////////////////////////////
    //
    // Constructor will initialize maps for easy lookups
    //
    ////////////////////////////////////////////////////////////
    public statusMapping() {
        //Setup the String to Int Map
        statusStringToInt = new HashMap<String, Integer>();
        statusStringToInt.put(NOT_SET, NOT_SET_NUM);
        statusStringToInt.put(NOT_COMPLETE, NOT_COMPLETE_NUM);
        statusStringToInt.put(COMPLETED, COMPLETED_NUM);
        statusStringToInt.put(LATE, LATE_NUM);
        statusStringToInt.put(DISPUTE, DISPUTE_NUM);
        statusStringToInt.put(REASSIGNMENT_APPROVAL, REASSIGNMENT_APPROVAL_NUM);
        statusStringToInt.put(SWAP_APPROVAL, SWAP_APPROVAL_NUM);

        //Setup the Int to String Map
        statusIntToString = new HashMap<Integer, String>();
        statusIntToString.put(NOT_SET_NUM, NOT_SET);
        statusIntToString.put(NOT_COMPLETE_NUM, NOT_COMPLETE);
        statusIntToString.put(COMPLETED_NUM, COMPLETED);
        statusIntToString.put(LATE_NUM, LATE);
        statusIntToString.put(DISPUTE_NUM, DISPUTE);
        statusIntToString.put(REASSIGNMENT_APPROVAL_NUM, REASSIGNMENT_APPROVAL);
        statusIntToString.put(SWAP_APPROVAL_NUM, SWAP_APPROVAL);
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
            intTags.add(statusStringToInt.get(stringTags.get(i)));

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
            stringTags.add(statusIntToString.get(intTags.get(i)));
        }

        return stringTags;
    }

    ////////////////////////////////////////////////////////////
    //
    // Will map an single tag from String to Integer
    //
    ////////////////////////////////////////////////////////////
    public Integer mapStringToInt(String stringTag) {
        return statusStringToInt.get(stringTag);
    }

    ////////////////////////////////////////////////////////////
    //
    // Will map an single tag from Integer to String
    //
    ////////////////////////////////////////////////////////////
    public String mapIntToString(Integer intTag) {
        return statusIntToString.get(intTag);
    }
}
