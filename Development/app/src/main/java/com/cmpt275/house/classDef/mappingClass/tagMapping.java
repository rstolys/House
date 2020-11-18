package com.cmpt275.house.classDef.mappingClass;

import com.cmpt275.house.interfaceDef.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tagMapping implements mapping {
    private Map<String, Integer> tagStringToInt;
    private Map<Integer, String> tagIntToString;

    //String Tags
    public final String NO_TAG = "No Tag";
    public final String CLEANING = "Cleaning";
    public final String KITCHEN = "Kitchen";
    public final String COOKING = "Cooking";
    public final String BATHROOM = "Bathroom";
    public final String GARBAGE = "Garbage";
    public final String GROCERIES = "Groceries";
    public final String SHOPPING = "Shopping";
    public final String IN_MAINTENANCE = "Indoor Maintenance";
    public final String OUT_MAINTENANCE = "Outdoor Maintenance";
    public final String GARAGE = "Garage";
    public final String OTHER = "Other";

    //Integer Tags
    public final Integer NO_TAG_NUM = -1;
    public final Integer CLEANING_NUM = 0;
    public final Integer KITCHEN_NUM = 1;
    public final Integer COOKING_NUM = 2;
    public final Integer BATHROOM_NUM = 3;
    public final Integer GARBAGE_NUM = 4;
    public final Integer GROCERIES_NUM = 5;
    public final Integer SHOPPING_NUM = 6;
    public final Integer IN_MAINTENANCE_NUM = 7;
    public final Integer OUT_MAINTENANCE_NUM = 8;
    public final Integer GARAGE_NUM = 9;
    public final Integer OTHER_NUM = 10;


    ////////////////////////////////////////////////////////////
    //
    // Constructor will initialize maps for easy lookups
    //
    ////////////////////////////////////////////////////////////
    public tagMapping() {
        //Setup the String to Int Map
        tagStringToInt = new HashMap<String, Integer>();
        tagStringToInt.put(NO_TAG, NO_TAG_NUM);
        tagStringToInt.put(CLEANING, CLEANING_NUM);
        tagStringToInt.put(KITCHEN, KITCHEN_NUM);
        tagStringToInt.put(COOKING, COOKING_NUM);
        tagStringToInt.put(BATHROOM, BATHROOM_NUM);
        tagStringToInt.put(GARBAGE, GARBAGE_NUM);
        tagStringToInt.put(GROCERIES, GROCERIES_NUM);
        tagStringToInt.put(SHOPPING, SHOPPING_NUM);
        tagStringToInt.put(IN_MAINTENANCE, IN_MAINTENANCE_NUM);
        tagStringToInt.put(OUT_MAINTENANCE, OUT_MAINTENANCE_NUM);
        tagStringToInt.put(GARAGE, GARAGE_NUM);
        tagStringToInt.put(OTHER, OTHER_NUM);

        //Setup the Int to String Map
        tagIntToString = new HashMap<Integer, String>();
        tagIntToString.put(NO_TAG_NUM, NO_TAG);
        tagIntToString.put(CLEANING_NUM, CLEANING);
        tagIntToString.put(KITCHEN_NUM, KITCHEN);
        tagIntToString.put(COOKING_NUM, COOKING);
        tagIntToString.put(BATHROOM_NUM, BATHROOM);
        tagIntToString.put(GARBAGE_NUM, GARBAGE);
        tagIntToString.put(GROCERIES_NUM, GROCERIES);
        tagIntToString.put(SHOPPING_NUM, SHOPPING);
        tagIntToString.put(IN_MAINTENANCE_NUM, IN_MAINTENANCE);
        tagIntToString.put(OUT_MAINTENANCE_NUM, OUT_MAINTENANCE);
        tagIntToString.put(GARAGE_NUM, GARAGE);
        tagIntToString.put(OTHER_NUM, OTHER);
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
            intTags.add(tagStringToInt.get(stringTags.get(i)));

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
            stringTags.add(tagIntToString.get(intTags.get(i)));
        }

        return stringTags;
    }

    ////////////////////////////////////////////////////////////
    //
    // Will map an single tag from String to Integer
    //
    ////////////////////////////////////////////////////////////
    public Integer mapStringToInt(String stringTag) {
        return tagStringToInt.get(stringTag);
    }

    ////////////////////////////////////////////////////////////
    //
    // Will map an single tag from Integer to String
    //
    ////////////////////////////////////////////////////////////
    public String mapIntToString(Integer intTag) {
        return tagIntToString.get(intTag);
    }
}
