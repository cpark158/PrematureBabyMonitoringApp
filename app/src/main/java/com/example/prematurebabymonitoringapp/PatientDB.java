package com.example.prematurebabymonitoringapp;

import java.sql.Date;
import java.util.ArrayList;
//import java.util.Optional;

// PatientDB will have a list of all the patients in the unit

public class PatientDB {
    ArrayList<Patient> patients = new ArrayList<Patient>();
    private Patient newPat;     // stores last added patient's details

    // Constructor
    public PatientDB() {
    }

    // Method to add create a new com.example.prematurebabymonitoringapp.Patient object and add it to the database
    public void addPatient(String name, int hospID, String DOB, String gender) {
        newPat = new Patient(hospID);
        newPat.setName(name);

        /* DOB is entered as a string, then converted to a Date variable
        Reference: https://www.javatpoint.com/java-sql-date
         */
        Date date = Date.valueOf(DOB);
        newPat.setDOB(date);

        newPat.setGender(gender);
        patients.add(newPat);
    }

    public void addPatient(Patient newPat) {
        this.newPat = newPat;
        patients.add(this.newPat);
    }

    public void removePatient(Patient patientToRemove) {
        patients.remove(patientToRemove);
    }

    // methods to add more patient details (overloading because not all parameters are required)
    public void addName(Patient patient, String name) {
        int i = findPatientNo(patient.getHospID());
        patients.get(i).setName(name);
    }

    public void addTimeOfBirth(Patient patient, String time) {
        int i = this.findPatientNo(patient.getHospID());
        patients.get(i).setTimeOfBirth(time);
    }

    public void addWeight(Patient patient, double weight) {
        int i = this.findPatientNo(patient.getHospID());
        patients.get(i).setWeight(weight);
    }

    public void addContactNum(Patient patient, String contactNum) {
        int i = this.findPatientNo(patient.getHospID());
        patients.get(i).setContactNum(contactNum);
    }

    public void addMom(Patient patient, String mom) {
        int i = this.findPatientNo(patient.getHospID());
        patients.get(i).setMotherName(mom);
    }

    public void addDad(Patient patient, String dad) {
        int i = this.findPatientNo(patient.getHospID());
        patients.get(i).setFatherName(dad);
    }

    public void addHealthIdx(Patient patient, double healthIdx) {
        int i = this.findPatientNo(patient.getHospID());
        patients.get(i).setHealthIndex(healthIdx);
    }

    public void addDetails(Patient patient, String condition) {
        int i = this.findPatientNo(patient.getHospID());
        patients.get(i).setCondition(condition);
    }

    public Patient lastPatient() {
        return newPat;
    }

    // This method searches for a patient in the database
    /* Reference: https://www.baeldung.com/find-list-element-java */
    public Patient findPatient(Patient patient) {
        for (Patient p : patients) {
            if (p.getHospID() == patient.getHospID()) {
                return p;
            }
        }
        return null;
    }

    // Finds patient by index number
    public Patient findPatient(int i) {
        return patients.get(i);
    }

    // Finds the patient's number/position in database
    public int findPatientNo(int hospID) {
        int patientNo = -1;
        for (int i = 0; i < patients.size(); i++) {
            if (hospID == (patients.get(i).getHospID())) {
                patientNo = i;
            }
        }
        return patientNo;
    }

    // This method checks if patient already exists (i.e. checks for the same hospID
    public boolean patientExists(int hospID) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getHospID() == hospID)
                return true;
        }
        return false;
    }

    // method to return number of patients
    public int getDBSize() {
        return patients.size();
    }

}

