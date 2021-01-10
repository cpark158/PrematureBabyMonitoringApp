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
        assertEquals(db.lastPatient().getName(),p2.getName());
        assertEquals(db.lastPatient().getHospID(),p2.getHospID());
        assertEquals(db.lastPatient().getDOB(),p2.getDOB());
        assertEquals(db.lastPatient().getGender(),p2.getGender());
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
        // testing with addPatient by patient object
        db.addPatient(p1);
        db.addPatient(p2);
        assertEquals(db.findPatient(p2),p2);

        // testing with addPatient by patient details
        Patient p3 = new Patient(3);
        p3.setName("Bob");
        p3.setDOB(Date.valueOf("2020-12-12"));
        p3.setGender("male");
        db.addPatient("Bob",3,"2020-12-12","male");
        assertEquals(db.lastPatient().getName(),p3.getName());
        assertEquals(db.lastPatient().getHospID(),p3.getHospID());
        assertEquals(db.lastPatient().getDOB(),p3.getDOB());
        assertEquals(db.lastPatient().getGender(),p3.getGender());
    }

    @Test
    public void testFindPatientNo() {
        db.addPatient(p1);
        db.addPatient(p2);
        assertEquals(db.findPatientNo(p1.getHospID()),0);
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

    @Test
    public void testAddTimeOfBirth() {
        db.addPatient(p1);
        db.addTimeOfBirth(p1,"12.45");
        assertEquals(db.findPatient(p1).getTimeOfBirth(),"12.45");
    }

    @Test
    public void testAddWeight() {
        db.addPatient(p1);
        db.addWeight(p1,2.59);
        assertEquals(db.findPatient(p1).getWeight(),2.59,0);
    }

    public void addContactNum(Patient patient, String contactNum) {
        db.addPatient(p1);
        db.addContactNum(p1,"12345964");
        assertEquals(db.findPatient(p1).getContactNum(),"12345964");
    }

    public void addMom(Patient patient, String mom) {
        db.addPatient(p1);
        db.addMom(p1,"Mary Jane");
        assertEquals(db.findPatient(p1).getMotherName(),"Mary Jane");
    }

    public void addDad(Patient patient, String dad) {
        db.addPatient(p1);
        db.addDad(p1,"Peter Parker");
        assertEquals(db.findPatient(p1).getFatherName(),"Peter Parker");
    }

    public void addHealthIdx(Patient patient, double healthIdx) {
        db.addPatient(p1);
        db.addHealthIdx(p1,86.94);
        assertEquals(db.findPatient(p1).getHealthIndex(),86.94,0);
    }

    public void addDetails(Patient patient, String condition) {
        db.addPatient(p1);
        db.addDetails(p1,"Diabetes");
        assertEquals(db.findPatient(p1).getCondition(),"Diabetes");
    }
}
