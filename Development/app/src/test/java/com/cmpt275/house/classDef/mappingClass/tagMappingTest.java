package com.cmpt275.house.classDef.mappingClass;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class tagMappingTest {

    /////////////////////////////////////////////////////////
    //
    // Test map of string to integer
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testMapOfStringToInt() {

        tagMapping tagMap = new tagMapping();

        String startValue = tagMap.CLEANING;

        String finalValue = tagMap.mapIntToString(tagMap.mapStringToInt(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    /////////////////////////////////////////////////////////
    //
    // Test map of integer to string
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testMapOfIntToString() {

        tagMapping tagMap = new tagMapping();

        Integer startValue = tagMap.CLEANING_NUM;

        Integer finalValue = tagMap.mapStringToInt(tagMap.mapIntToString(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    /////////////////////////////////////////////////////////
    //
    // Test map of list of integers to strings
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testListOfIntToStringAndBack() {

        tagMapping tagMap = new tagMapping();

        List<Integer> startValue = new ArrayList<Integer>();
        startValue.add(tagMap.CLEANING_NUM);
        startValue.add(tagMap.BATHROOM_NUM);
        startValue.add(tagMap.COOKING_NUM);
        startValue.add(tagMap.GARAGE_NUM);
        startValue.add(tagMap.GROCERIES_NUM);

        List<Integer> finalValue = tagMap.mapList_StringToInt(tagMap.mapList_IntToString(startValue));

        Assert.assertEquals(startValue, finalValue);

    }


    /////////////////////////////////////////////////////////
    //
    // Test map of list of strings to integers
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testListOfStringToIntAndBack() {

        tagMapping tagMap = new tagMapping();

        List<String> startValue = new ArrayList<String>();
        startValue.add(tagMap.CLEANING);
        startValue.add(tagMap.BATHROOM);
        startValue.add(tagMap.COOKING);
        startValue.add(tagMap.GARAGE);
        startValue.add(tagMap.GROCERIES);

        List<String> finalValue = tagMap.mapList_IntToString(tagMap.mapList_StringToInt(startValue));

        Assert.assertEquals(startValue, finalValue);
    }

}