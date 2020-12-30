package com.example.prematurebabymonitoringapp;

import android.widget.LinearLayout;
import android.widget.TextView;

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
    public void addPatient(String name, int hospID, Date DOB, String gender) {
        newPat = new Patient(hospID);
        newPat.setName(name);
        newPat.setDOB(DOB);
        newPat.setGender(gender);
        patients.add(newPat);
    }

    public void addPatient(Patient newPat) {
        this.newPat=newPat;
        patients.add(this.newPat);
    }

    // method to add more patient details (overloading because not all parameters are required)
    public void addName(Patient patient,String name) {
        patient.setName(name);
    }
    public void addTimeOfBirth(Patient patient,String time) {
        patient.setTimeOfBirth(time);
    }
    public void addWeight(Patient patient,double weight) {
        patient.setWeight(weight);
    }
    public void addContactNum(Patient patient,String contactNum) {
        patient.setContactNum(contactNum);
    }
    public void addMom(Patient patient,String mom) {
        patient.setMotherName(mom);
    }
    public void addDad(Patient patient,String dad) {
        patient.setFatherName(dad);
    }
    public void addHealthIdx(Patient patient,double healthIdx) {
        patient.setHealthIndex(healthIdx);
    }
    public void addDetails(Patient patient,String condition) {
        patient.setCondition(condition);
    }

    public Patient lastPatient() {
        return newPat;
    }

    // This method searches for a patient in the database using patient's name
    public Patient findPatient(String name) {
        int patientNo = 0;
        for(int i=0; i < patients.size(); i++) {
            if (patients.get(i).getName() == name)
                patientNo = i+1;
        }

        if (patientNo == 0) {
            System.out.println("Not a registered patient");
            return patients.get(0);
        }
        else {
        return patients.get(patientNo-1); }  // exception if patientNo = 0, need to find a way around this
    }

    public Patient findPatientByIndex(int i){
        return patients.get(i);
    }

    // This method returns the patient using index number
    public Patient findPatIdx(int index) {
        return patients.get(index-1);
    }

    /* method to display all patients in the database to android app
     public void patientMenu() {
        // Reference: https://stackoverflow.com/questions/6525916/dynamically-display-string-text-on-android-screen
        TextView myText = new TextView();
        for (Patient p:patients) {
            myText.setText("Name: "+p.getName()+" Hospital ID: "+p.getHospID());
        }
     } */

     // method to return number of patients
    public int getDBSize() {
        return patients.size();
    }

}
