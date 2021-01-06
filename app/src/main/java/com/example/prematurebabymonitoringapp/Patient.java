package com.example.prematurebabymonitoringapp;//import java.time.LocalDate;
//import java.time.LocalTime;

import java.util.ArrayList;

/** Patient is a class which contains all the data for one patient */

public class Patient {
    // The following fields list patient's profile information
    private String name;
    private String hospID;
    private String DOB; // String for now, will figure out how to set it as a date
    private String timeOfBirth;
    private double weight;
    private String gender;
    private String motherName;
    private String fatherName;
    private String contactNum;
    private String condition;   // stores additional details on pre-existing conditions/diseases
    private double healthIndex;
    private MonitoredParams param;
    private GraphPlotter graph1;    // graph of conc1 against time
    private GraphPlotter graph2;    // graph of conc2 against time
    private ArrayList<String> comments = new ArrayList<String>();

    // constructor
    public Patient(String name) {
        this.name = name;
        param = new MonitoredParams();
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public String getDOB() {
        return DOB;
    }
    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHospID() {
        return hospID;
    }
    public void setHospID(String hospID) {
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

    public void addComment(String comment){
        comments.add(comment);
    }

    public int getNumberOfComment(){
        return comments.size();
    }

    public String getCommentByIndex(int index){
        if(index < comments.size()){
            return comments.get(index);
        }
        else return "";
    }



    // Main functions

    /* Set monitoredParams manually
    public void manualInput(ArrayList<Integers> conc1,ArrayList<Integers> conc2,ArrayList<Time> time) {
        for (int i=0; i<conc1.getSize();i++) {
            this.addConc(conc1.get(i),conc2.get(i));
            //this.addTime(time.get(i));
        }
     */

    // Method to plot glucose concentration against time (ideally against time of day)
    public void plotGlucose() {
        graph1 = new GraphPlotter(param.getTime(), param.getGlucose());
    }

    // Method to plot lactate concentration against time (ideally against time of day)
    public void plotLactate() {
        graph2 = new GraphPlotter(param.getTime(), param.getLactate());
    }

}
