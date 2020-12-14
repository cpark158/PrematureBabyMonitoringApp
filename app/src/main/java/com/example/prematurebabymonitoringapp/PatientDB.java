package com.example.prematurebabymonitoringapp;

import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
//import java.util.Optional;

// PatientDB will have a list of all the patients in the unit

public class PatientDB {
    ArrayList<Patient> patients = new ArrayList<Patient>();

    // Constructor
    public PatientDB() {
    }

    // Method to add create a new com.example.prematurebabymonitoringapp.Patient object and add it to the database, with
    public void addPatient(String name, String DOB, String gender) {
        Patient baby = new Patient(name);
        baby.setDOB(DOB);
        baby.setGender(gender);
        patients.add(baby);
    }

    // method to add more patient details (overloading because not all parameters are required)
    public void addHospID(Patient patient,String hospID) {
        patient.setHospID(hospID);
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

    // This method searches for a patient in the database using patient's name
    public Patient findPatient(String name) {
        int patientNo = 0;
        for(int i=0; i < patients.size(); i++) {
            if (patients.get(i).getName() == name) patientNo = i+1;
        }
        if (patientNo == 0) System.out.println("Not a registered patient");
        return patients.get(patientNo-1);   // exception if patientNo = 0, need to find a way around this
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
