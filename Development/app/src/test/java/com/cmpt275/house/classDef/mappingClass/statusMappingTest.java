package com.cmpt275.house.classDef.mappingClass;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class statusMappingTest {

    /////////////////////////////////////////////////////////
    //
    // Test map of string to integer
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testMapOfStringToInt() {

        statusMapping statusMap = new statusMapping();

        String startValue = statusMap.COMPLETED;

        String finalValue = statusMap.mapIntToString(statusMap.mapStringToInt(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    /////////////////////////////////////////////////////////
    //
    // Test map of integer to string
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testMapOfIntToString() {

        statusMapping statusMap = new statusMapping();

        Integer startValue = statusMap.DISPUTE_NUM;

        Integer finalValue = statusMap.mapStringToInt(statusMap.mapIntToString(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    /////////////////////////////////////////////////////////
    //
    // Test map of list of integers to strings
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testListOfIntToStringAndBack() {

        statusMapping statusMap = new statusMapping();

        List<Integer> startValue = new ArrayList<Integer>();
        startValue.add(statusMap.COMPLETED_NUM);
        startValue.add(statusMap.DISPUTE_NUM);
        startValue.add(statusMap.LATE_NUM);
        startValue.add(statusMap.NOT_COMPLETE_NUM);
        startValue.add(statusMap.NOT_SET_NUM);

        List<Integer> finalValue = statusMap.mapList_StringToInt(statusMap.mapList_IntToString(startValue));

        Assert.assertEquals(startValue, finalValue);

    }


    /////////////////////////////////////////////////////////
    //
    // Test map of list of strings to inetgers
    //
    /////////////////////////////////////////////////////////
    @Test
    public void testListOfStringToIntAndBack() {

        statusMapping statusMap = new statusMapping();

        List<String> startValue = new ArrayList<String>();
        startValue.add(statusMap.COMPLETED);
        startValue.add(statusMap.DISPUTE);
        startValue.add(statusMap.LATE);
        startValue.add(statusMap.NOT_COMPLETE);
        startValue.add(statusMap.NOT_SET);

        List<String> finalValue = statusMap.mapList_IntToString(statusMap.mapList_StringToInt(startValue));

        Assert.assertEquals(startValue, finalValue);
    }

}