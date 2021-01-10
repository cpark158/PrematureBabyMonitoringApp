package com.example.prematurebabymonitoringapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/** This class tests the Patient class if it works the way we originally intend it to */
public class TestPatient {
    Patient p = new Patient(8);

    @Before
    public void setUp() {
        p.addComment("Comment 1");
        p.addComment("Comment 2");
    }

    @Test
    // test setting name
    public void testSetName() {
        p.setName("Calista");
        Assert.assertEquals(p.getName(),"Calista");
    }

    @Test
    // test setting date of birth (Date variable)
    public void testSetDOB() {
        p.setDOB(Date.valueOf("2020-06-15"));
        // convert date into string to compare
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Assert.assertEquals(dateFormat.format(p.getDOB()),"2020-06-15");
    }

    @Test
    // test if gender is always stored as lowercase string
    public void testSetGender() {
        p.setGender("Female");
        Assert.assertEquals(p.getGender(),"female");
    }

    @Test
    // test if correct number of comments is returned
    public void testGetNumberOfComment() {
        Assert.assertEquals(p.getNumberOfComment(),2);
    }

    @Test
    // test if comment is actually added to arraylist
    public void testAddComment() {
        p.addComment("New Comment");
        Assert.assertEquals(p.getNumberOfComment(),3);
    }

    @Test
    // test if comment is returned correctly using index number
    public void testGetCommentByIndex() {
        Assert.assertEquals(p.getCommentByIndex(1),"Comment 1");
    }

    @Test
    public void testSetTimeOfBirth() {
        p.setTimeOfBirth("13:45");
        Assert.assertEquals(p.getTimeOfBirth(),"13:45");
    }

    @Test
    public void testSetWeight() {
        p.setWeight(2.56);
        Assert.assertEquals(p.getWeight(),2.56,0);
    }

    @Test
    public void testSetFatherName() {
        p.setFatherName("Bob");
        Assert.assertEquals(p.getFatherName(),"Bob");
    }

    @Test
    public void testSetMotherName() {
        p.setMotherName("Mary");
        Assert.assertEquals(p.getMotherName(),"Mary");
    }

    @Test
    public void testSetContact() {
        p.setContactNum("0771812356");
        Assert.assertEquals(p.getContactNum(),"0771812356");
    }

    @Test
    public void testSetCondition() {
        p.setCondition("Beta Thalassemia Carrier");
        Assert.assertEquals(p.getCondition(),"Beta Thalassemia Carrier");
    }

    @Test
    public void testSetHealthIndex() {
        p.setHealthIndex(86.8);
        Assert.assertEquals(p.getHealthIndex(),86.8,0);
    }

}
