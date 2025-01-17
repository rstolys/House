package com.cmpt275.house.classDef.mappingClass;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class notificationMappingTest {

    ////////////////////////////////////////////////////////////
    // Automated Test 1
    ////////////////////////////////////////////////////////////
    @Test
    public void testMapOfStringToInt() {

        notificationMapping notify = new notificationMapping();

        String startValue = notify.WEEKLY;

        String finalValue = notify.mapIntToString(notify.mapStringToInt(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 2
    ////////////////////////////////////////////////////////////
    @Test
    public void testMapOfIntToString() {

        notificationMapping notify = new notificationMapping();

        Integer startValue = notify.MONTHLY_NUM;

        Integer finalValue = notify.mapStringToInt(notify.mapIntToString(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 3
    ////////////////////////////////////////////////////////////
    @Test
    public void testListOfStringToInt() {

        notificationMapping notify = new notificationMapping();

        List<String> startValue = new ArrayList<String>();
        startValue.add(notify.NONE);
        startValue.add(notify.WEEKLY);
        startValue.add(notify.MONTHLY);

        List<String> finalValue = notify.mapList_IntToString(notify.mapList_StringToInt(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 4
    ////////////////////////////////////////////////////////////
    @Test
    public void testListOfIntToString() {

        notificationMapping notify = new notificationMapping();

        List<Integer> startValue = new ArrayList<Integer>();
        startValue.add(notify.NONE_NUM);
        startValue.add(notify.WEEKLY_NUM);
        startValue.add(notify.MONTHLY_NUM);

        List<Integer> finalValue = notify.mapList_StringToInt(notify.mapList_IntToString(startValue));

        Assert.assertEquals(startValue, finalValue);

    }
}