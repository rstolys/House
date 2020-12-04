package com.cmpt275.house.classDef.mappingClass;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class roleMappingTest {

    /////////////////////////////////////////////////////////
    //
    // Test map of string to inetger
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testMapOfStringToInt() {

        roleMapping roleMap = new roleMapping();

        String startValue = roleMap.ADMIN;

        String finalValue = roleMap.mapIntToString(roleMap.mapStringToInt(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    /////////////////////////////////////////////////////////
    //
    // Test map of integer to string
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testMapOfIntToString() {

        roleMapping roleMap = new roleMapping();

        Integer startValue = roleMap.MEMBER_NUM;

        Integer finalValue = roleMap.mapStringToInt(roleMap.mapIntToString(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    /////////////////////////////////////////////////////////
    //
    // Test map of list of integers to strings
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testListOfIntToStringAndBack() {

        roleMapping roleMap = new roleMapping();

        List<Integer> startValue = new ArrayList<Integer>();
        startValue.add(roleMap.ADMIN_NUM);
        startValue.add(roleMap.MEMBER_NUM);
        startValue.add(roleMap.REQUEST_NUM);

        List<Integer> finalValue = roleMap.mapList_StringToInt(roleMap.mapList_IntToString(startValue));

        Assert.assertEquals(startValue, finalValue);

    }


    /////////////////////////////////////////////////////////
    //
    // Test map of list of strings to inetgers
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testListOfStringToIntAndBack() {

        roleMapping roleMap = new roleMapping();

        List<String> startValue = new ArrayList<String>();
        startValue.add(roleMap.ADMIN);
        startValue.add(roleMap.MEMBER);
        startValue.add(roleMap.REQUEST);

        List<String> finalValue = roleMap.mapList_IntToString(roleMap.mapList_StringToInt(startValue));

        Assert.assertEquals(startValue, finalValue);
    }

}