package com.example.prematurebabymonitoringapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/** This class tests the GraphPlotter class if it works the way we originally intend it to */
public class TestGraphPlotter {
    GraphPlotter graph = new GraphPlotter();


    @Test
    public void testSetXData() {
        ArrayList<Integer> xVal = new ArrayList<Integer>();
        xVal.add(1);
        xVal.add(2);
        xVal.add(3);
        xVal.add(4);
        xVal.add(5);
        graph.setXData(xVal);
        assertEquals(1, (long) graph.xDataValues.get(0));
    }

    @Test
    public void testSetYData() {
        ArrayList<Integer> yVal = new ArrayList<Integer>();
        yVal.add(1);
        yVal.add(2);
        yVal.add(3);
        yVal.add(4);
        yVal.add(5);
        graph.setYData(yVal);
        assertEquals(1,(long) graph.yDataValues.get(0));
    }
}
