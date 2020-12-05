package com.cmpt275.house.classDef.mappingClass;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class roleMappingTest {


    ////////////////////////////////////////////////////////////
    // Automated Test 5
    ////////////////////////////////////////////////////////////
    @Test
    public void testMapOfStringToInt() {

        roleMapping roleMap = new roleMapping();

        String startValue = roleMap.ADMIN;

        String finalValue = roleMap.mapIntToString(roleMap.mapStringToInt(startValue));

        Assert.assertEquals(startValue, finalValue);
    }

    ////////////////////////////////////////////////////////////
    // Automated Test 6
    ////////////////////////////////////////////////////////////
    @Test
    public void testMapOfIntToString() {

        roleMapping roleMap = new roleMapping();

        Integer startValue = roleMap.MEMBER_NUM;

        Integer finalValue = roleMap.mapStringToInt(roleMap.mapIntToString(startValue));

        Assert.assertEquals(startValue, finalValue);
    }


    ////////////////////////////////////////////////////////////
    // Automated Test 7
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
    // Automated Test 8
    ////////////////////////////////////////////////////////////
    @Test
    public void testListOfIntToString() {

        roleMapping roleMap = new roleMapping();

        List<Integer> startValue = new ArrayList<Integer>();
        startValue.add(roleMap.ADMIN_NUM);
        startValue.add(roleMap.MEMBER_NUM);
        startValue.add(roleMap.REQUEST_NUM);

        List<Integer> finalValue = roleMap.mapList_StringToInt(roleMap.mapList_IntToString(startValue));

        Assert.assertEquals(startValue, finalValue);
    }
}