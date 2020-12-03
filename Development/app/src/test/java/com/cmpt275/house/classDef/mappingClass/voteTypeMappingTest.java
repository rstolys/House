package com.cmpt275.house.classDef.mappingClass;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class voteTypeMappingTest {

    /////////////////////////////////////////////////////////
    //
    // Test map of string to integer
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testMapOfStringToInt() {

        voteTypeMapping voteMap = new voteTypeMapping();

        String startValue = voteMap.DISPUTE_COMPLETION;

        String finalValue = voteMap.mapIntToString(voteMap.mapStringToInt(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    /////////////////////////////////////////////////////////
    //
    // Test map of integer to string
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testMapOfIntToString() {

        voteTypeMapping voteMap = new voteTypeMapping();

        Integer startValue = voteMap.DEADLINE_EXTENSION_NUM;

        Integer finalValue = voteMap.mapStringToInt(voteMap.mapIntToString(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    /////////////////////////////////////////////////////////
    //
    // Test map of list of integers to strings
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testListOfIntToStringAndBack() {

        voteTypeMapping voteMap = new voteTypeMapping();

        List<Integer> startValue = new ArrayList<Integer>();
        startValue.add(voteMap.DISPUTE_COMPLETION_NUM);
        startValue.add(voteMap.DEADLINE_EXTENSION_NUM);

        List<Integer> finalValue = voteMap.mapList_StringToInt(voteMap.mapList_IntToString(startValue));

        Assert.assertEquals(startValue, finalValue);

    }


    /////////////////////////////////////////////////////////
    //
    // Test map of list of strings to integers
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testListOfStringToIntAndBack() {

        voteTypeMapping voteMap = new voteTypeMapping();

        List<String> startValue = new ArrayList<String>();
        startValue.add(voteMap.DISPUTE_COMPLETION);
        startValue.add(voteMap.DEADLINE_EXTENSION);

        List<String> finalValue = voteMap.mapList_IntToString(voteMap.mapList_StringToInt(startValue));

        Assert.assertEquals(startValue, finalValue);
    }
}