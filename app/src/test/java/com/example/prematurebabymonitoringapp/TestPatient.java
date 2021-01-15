package com.example.prematurebabymonitoringapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;

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
        assertEquals("Calista",p.getName());
    }

    @Test
    // test setting date of birth (Date variable)
    public void testSetDOB() {
        p.setDOB(Date.valueOf("2020-06-15"));
        assertEquals(Date.valueOf("2020-06-15"),p.getDOB());
    }

    @Test
    // test if gender is always stored as lowercase string
    public void testSetGender() {
        p.setGender("Female");
        assertEquals("female",p.getGender());
    }

    @Test
    // test if correct number of comments is returned
    public void testGetNumberOfComment() {
        assertEquals(2,p.getNumberOfComment());
    }

    @Test
    // test if comment is actually added to arraylist
    public void testAddComment() {
        p.addComment("New Comment");
        assertEquals(3,p.getNumberOfComment());
    }

    @Test
    // test if comment is returned correctly using index number
    public void testGetCommentByIndex() {
        assertEquals("Comment 2",p.getCommentByIndex(1));
    }

    @Test
    public void testSetTimeOfBirth() {
        p.setTimeOfBirth("13:45");
        assertEquals("13:45",p.getTimeOfBirth());
    }

    @Test
    public void testSetWeight() {
        p.setWeight(2.56);
        assertEquals(2.56, p.getWeight(),0);
    }

    @Test
    public void testSetFatherName() {
        p.setFatherName("Bob");
        assertEquals("Bob",p.getFatherName());
    }

    @Test
    public void testSetMotherName() {
        p.setMotherName("Mary");
        assertEquals("Mary",p.getMotherName());
    }

    @Test
    public void testSetContact() {
        p.setContactNum("0771812356");
        assertEquals("0771812356",p.getContactNum());
    }

    @Test
    public void testSetCondition() {
        p.setCondition("Beta Thalassemia Carrier");
        assertEquals("Beta Thalassemia Carrier",p.getCondition());
    }

    @Test
    public void testSetHealthIndex() {
        p.setHealthIndex(86.8);
        assertEquals(86.8,p.getHealthIndex(),0);
    }

}
