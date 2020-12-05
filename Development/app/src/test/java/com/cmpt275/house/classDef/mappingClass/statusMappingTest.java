package com.cmpt275.house.classDef.mappingClass;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class statusMappingTest {

    ////////////////////////////////////////////////////////////
    // Automated Test 9
    ////////////////////////////////////////////////////////////
    @Test
    public void testMapOfStringToInt() {

        statusMapping statusMap = new statusMapping();

        String startValue = statusMap.COMPLETED;

        String finalValue = statusMap.mapIntToString(statusMap.mapStringToInt(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 10
    ////////////////////////////////////////////////////////////
    @Test
    public void testMapOfIntToString() {

        statusMapping statusMap = new statusMapping();

        Integer startValue = statusMap.DISPUTE_NUM;

        Integer finalValue = statusMap.mapStringToInt(statusMap.mapIntToString(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 11
    ////////////////////////////////////////////////////////////
    @Test
    public void testListOfStringToInt() {

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


    ////////////////////////////////////////////////////////////
    // Automated Test 12
    ////////////////////////////////////////////////////////////
    @Test
    public void testListOfIntToString() {

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
}