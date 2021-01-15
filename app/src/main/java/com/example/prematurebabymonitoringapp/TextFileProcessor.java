package com.example.prematurebabymonitoringapp;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class TextFileProcessor {

    File file;
    Scanner s;

    //raw data
    ArrayList<String> timeStrings;
    ArrayList<Integer> voltage1;
    ArrayList<Integer> voltage2;
    ArrayList<Integer> hour;
    ArrayList<Integer> min;
    ArrayList<Double> sec;

    //calibrated data
    ArrayList<Integer> glucConc; //glucose concentration
    ArrayList<Integer> lactConc; //lactate concentration

    //calibration parameters to convert from voltage to concentration to be entered by user
    int gradient;
    int offset;


    public TextFileProcessor(File fileName) {
        this.timeStrings = new ArrayList<>();
        this.voltage1 = new ArrayList<>();
        this.voltage2 = new ArrayList<>();
        this.hour = new ArrayList<>();
        this.min = new ArrayList<>();
        this.sec = new ArrayList<>();
        this.glucConc = new ArrayList<>();
        this.lactConc = new ArrayList<>();

        this.file = fileName;
        try {
            this.s = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void parseFile() {
        //scanner object parses text file. s.next() retrieves each string on the line
        while (s.hasNext()) {
            int dataPoint = Integer.parseInt(s.next());
            String timeString = s.next(); //retrieve time as a string
            timeStrings.add(timeString);
            int voltage1Val = Integer.parseInt(s.next()); //retrieve voltage1 value as string and convert to int
            voltage1.add(voltage1Val);
            int voltage2Val = Integer.parseInt(s.next());
            voltage2.add(voltage2Val);
        }

        /*currently time stored in string in format hour:min:sec
        split each time string into separate hour, min, sec components and convert to int*/
        for (int i = 0; i < timeStrings.size(); i++) {
            String[] splitString = timeStrings.get(i).split(":"); //splitString is a string array containing 3 strings - 1 for hour, 1 for min, 1 for sec
            hour.add(Integer.parseInt(splitString[0]));
            min.add(Integer.parseInt(splitString[1]));
            sec.add(Double.parseDouble(splitString[2]));

        }


    }

    //set calibration parameters
    public void setOffset(int c){
        offset = c;
    }

    public void setGradient(int m){
        gradient = m;
    }

    //calibrate data
    public void calibrateGlucose(){
        for (int i=0; i<voltage1.size(); i++){
            int glucoseConcentration = (voltage1.get(i)*gradient) + offset;
            glucConc.add(glucoseConcentration);
        }
    }

    public void calibrateLactate(){
        for (int i=0; i<voltage2.size(); i++){
            int lactateConcentration = (voltage2.get(i)*gradient) + offset;
            lactConc.add(lactateConcentration);
        }
    }

    //getters
    public ArrayList<Integer> getGlucConc(){
        return this.glucConc;
    }

    public ArrayList<Integer> getLactConc(){
        return this.lactConc;
    }

    public ArrayList<Integer> getHour(){
        return this.hour;
    }

    public ArrayList<Integer> getMin(){
        return this.min;
    }

    public ArrayList<Double> getSec(){
        return this.sec;
    }



    //this returns a test value for comparison against the downloaded data file to check data has been correctly taken in
    public String testValString() {
        return timeStrings.get(9);
    }
}





















