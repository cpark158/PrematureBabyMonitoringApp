package com.example.prematurebabymonitoringapp;

import java.util.ArrayList;

/** MonitoredParams is a class which contains the data to be plotted for each patient. It has two arraylists for
 * time and glucose concentration, which will be the x and y values for the graph.
 */
public class MonitoredParams {
    ArrayList<Glucose> gConc = new ArrayList<Glucose>();
    ArrayList<Time> time = new ArrayList<Time>();

    // Constructor
    public MonitoredParams() {}

    // Method to add glucose concentration values from parsed text file
    public void addConc(double c1,double c2) {
        Glucose gluc = new Glucose();
        gluc.setChannel1(c1);
        gluc.setChannel2(c2);
        gConc.add(gluc);
    }

    // Method to add time values from parsed text file
    public void addTime(int day,int h,int m,double s) {
        Time t = new Time();
        t.setDay(day);
        t.setH(h);
        t.setM(m);
        t.setS(s);
        time.add(t);
    }
}
