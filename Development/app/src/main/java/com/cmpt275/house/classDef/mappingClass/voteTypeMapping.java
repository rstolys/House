package com.cmpt275.house.classDef.mappingClass;

import com.cmpt275.house.interfaceDef.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class voteTypeMapping implements mapping {
    private Map<String, Integer> voteTypeStringToInt;
    private Map<Integer, String> voteTypeIntToString;

    //String Tags
    public final String DISPUTE_COMPLETION = "Disputing Task Completion";
    public final String DEADLINE_EXTENSION = "Requesting Deadline Extension";

    //Integer Tags
    public final Integer DISPUTE_COMPLETION_NUM = 0;
    public final Integer DEADLINE_EXTENSION_NUM = 1;


    ////////////////////////////////////////////////////////////
    //
    // Constructor will initialize maps for easy lookups
    //
    ////////////////////////////////////////////////////////////
    public voteTypeMapping() {
        //Setup the String to Int Map
        voteTypeStringToInt = new HashMap<String, Integer>();
        voteTypeStringToInt.put(DISPUTE_COMPLETION, DISPUTE_COMPLETION_NUM);
        voteTypeStringToInt.put(DEADLINE_EXTENSION, DEADLINE_EXTENSION_NUM);

        //Setup the Int to String Map
        voteTypeIntToString = new HashMap<Integer, String>();
        voteTypeIntToString.put(DISPUTE_COMPLETION_NUM, DISPUTE_COMPLETION);
        voteTypeIntToString.put(DEADLINE_EXTENSION_NUM, DEADLINE_EXTENSION);
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
            intTags.add(voteTypeStringToInt.get(stringTags.get(i)));

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
            stringTags.add(voteTypeIntToString.get(intTags.get(i)));
        }

        return stringTags;
    }

    ////////////////////////////////////////////////////////////
    //
    // Will map an single value from String to Integer
    //
    ////////////////////////////////////////////////////////////
    public Integer mapStringToInt(String stringTag) {
        return voteTypeStringToInt.get(stringTag);
    }

    ////////////////////////////////////////////////////////////
    //
    // Will map an single value from Integer to String
    //
    ////////////////////////////////////////////////////////////
    public String mapIntToString(Integer intTag) {
        return voteTypeIntToString.get(intTag);
    }
}
