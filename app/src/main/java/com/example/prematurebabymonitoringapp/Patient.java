package com.example.prematurebabymonitoringapp;//import java.time.LocalDate;
//import java.time.LocalTime;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

/** Patient is a class which contains all the data for one patient */

public class Patient {
    // The following fields list patient's profile
    @SerializedName("name")
    private String name;
    @SerializedName("hospID")
    private int hospID;
    @SerializedName("DOB")
    private Date DOB; // String for now, will figure out how to set it as a date
    @SerializedName("timeOfBirth")
    private String timeOfBirth;
    @SerializedName("weight")
    private double weight;
    @SerializedName("gender")
    private String gender;
    @SerializedName("motherName")
    private String motherName;
    @SerializedName("fatherName")
    private String fatherName;
    @SerializedName("contactNum")
    private String contactNum;
    @SerializedName("condition")
    private String condition;   // stores additional details on pre-existing conditions/diseases
    @SerializedName("healthIndex")
    private double healthIndex;
    //private MonitoredParams param;

    // constructor
    public Patient(String name) {
        this.name = name;
        //param = new MonitoredParams();
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public Date getDOB() {
        return DOB;
    }
    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHospID() {
        return hospID;
    }
    public void setHospID(int hospID) {
        this.hospID = hospID;
    }

    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getMotherName() {
        return motherName;
    }
    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getContactNum() {
        return contactNum;
    }
    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getTimeOfBirth() {
        return timeOfBirth;
    }
    public void setTimeOfBirth(String timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }

    public void setHealthIndex(double perc) {
        // code to calculate and store health index
        healthIndex = perc;
    }
    public double getHealthIndex() {
        return healthIndex;
    }

    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }

    // Main functions
    // set monitoredParams from data in text files
    // public void graphPlotter()
}
