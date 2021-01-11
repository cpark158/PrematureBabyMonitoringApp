package com.example.prematurebabymonitoringapp;

import java.util.ArrayList;

/** MonitoredParams is a class which contains the data to be plotted for each patient. It has two arraylists for
 * time and glucose concentration, which will be the x and y values for the graph.
 */
public class MonitoredParams {
    ArrayList<Integer> glucose = new ArrayList<Integer>();
    ArrayList<Integer> lactate = new ArrayList<Integer>();
    ArrayList<Integer> time = new ArrayList<Integer>(); // ideally will change this to type Time

    // Constructor
    public MonitoredParams() {
    }

    // Method to add glucose concentration values
    public void addGlucose(int conc) {
        glucose.add(conc);
    }

    // Method to add lactate concentration values
    public void addLactate(int conc) {
        lactate.add(conc);
    }

    // Method to add time values
    public void addTime (int t) {
        time.add(t);
    }
    /* public void addTime(int day,int h,int m,double s) {
        Time t = new Time();
        t.setDay(day);
        t.setH(h);
        t.setM(m);
        t.setS(s);
        time.add(t);
    } */

    // Method to calibrate and add data from parsed text files
    /*public void addParsedData(TextFileProcessor data) {
        for (int i=0; i<data.getSize();i++) {
            // need to add calibration
            this.addGlucose(data.getTestVolt1().get(i));
            this.addLactate(data.getVolt2().get(i));
            this.addTime(data.getTestTimeValues().get(i));
            //this.addTime(data.getTimeValues().get(i));
        }
    }*/

    public ArrayList<Integer> getGlucose() {
        return glucose;
    }

    public ArrayList<Integer> getLactate() {
        return lactate;
    }

    public ArrayList<Integer> getTime() {
        return time;
    }

}
