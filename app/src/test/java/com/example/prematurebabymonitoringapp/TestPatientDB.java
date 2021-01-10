package com.example.prematurebabymonitoringapp;

import org.junit.Before;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;

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
        db.addPatient(p1);
        assertEquals(db.lastPatient(),p1);
    }

    @Test
    public void testAddPatientDetails() {
        p2.setName("Bob");
        p2.setDOB(Date.valueOf("2020-12-12"));
        p2.setGender("male");
        db.addPatient("Bob",2,"2020-12-12","male");
        assertEquals(db.lastPatient(),p2);
    }

    @Test
    public void testGetDBSize() {
        db.addPatient(p1);
        db.addPatient(p2);
        assertEquals(db.getDBSize(),2);
    }

    @Test
    public void testRemovePatient() {
        db.addPatient(p1);
        db.addPatient(p2);
        db.removePatient(p1);
        assertEquals(db.getDBSize(),1);
    }

    @Test
    public void testFindPatientByIndex() {
        db.addPatient(p1);
        db.addPatient(p2);
        assertEquals(db.findPatient(1),p2);
    }

    @Test
    public void testFindPatient() {
        db.addPatient(p1);
        db.addPatient(p2);
        assertEquals(db.findPatient(p2),p2);
    }

    @Test
    public void testFindPatientNo() {
        db.addPatient(p1);
        db.addPatient(p2);
        assertEquals(db.findPatientNo(p1),1);
    }

    @Test
    public void testPatientExists() {
        db.addPatient(p1);
        db.addPatient(p2);
        assertTrue(db.patientExists(2));
        assertFalse(db.patientExists(3));
    }

    @Test
    public void testAddName() {
        db.addPatient(p1);
        db.addName(p1,"Bob");
        assertEquals(db.findPatient(p1).getName(),"Bob");
    }

}
