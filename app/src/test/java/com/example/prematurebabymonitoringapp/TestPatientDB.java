package com.example.prematurebabymonitoringapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;

/** This class tests the methods in PatientDB */
public class TestPatientDB {
    private PatientDB db;
    private Patient p1;
    private Patient p2;

    @Before
    public void setDB() {
        db = new PatientDB();
        p1 = new Patient(1);
        p2 = new Patient(2);
    }

    @Test
    public void testAddPatient() {
        p1 = new Patient(1);
        db.addPatient(p1);
        Assert.assertEquals(db.lastPatient(),p1);
    }

    @Test
    public void testAddPatientDetails() {
        p2.setName("Bob");
        p2.setDOB(Date.valueOf("2020-12-12"));
        p2.setGender("male");
        db.addPatient("Bob",2,"2020-12-12","male");
        Assert.assertEquals(db.lastPatient(),p2);
    }

    @Test
    public void testGetDBSize() {
        db.addPatient(p1);
        db.addPatient(p2);
        Assert.assertEquals(db.getDBSize(),2);
    }

}
