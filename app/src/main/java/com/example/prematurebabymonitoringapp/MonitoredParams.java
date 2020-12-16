package com.example.prematurebabymonitoringapp;

import java.util.ArrayList;

/** MonitoredParams is a class which contains the data to be plotted for each patient. It has two arraylists for
 * time and glucose concentration, which will be the x and y values for the graph.
 */
public class MonitoredParams {
    ArrayList<Integer> glucose = new ArrayList<Integer>();
    ArrayList<Integer> lactate = new ArrayList<Integer>();
    ArrayList<Time> time = new ArrayList<Time>();

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
    public void addTime(int day,int h,int m,double s) {
        Time t = new Time();
        t.setDay(day);
        t.setH(h);
        t.setM(m);
        t.setS(s);
        time.add(t);
    }

    // Method to add data from parsed text files
    public void addParsedData(TextFileProcessor data) {
        for (int i=0; i<data.getSize();i++) {
            this.addGlucose(data.getVolt1().get(i));
            this.addGlucose(data.getVolt2().get(i));
            //this.addTime(data.getTimeValues().get(i));
        }
    }
}
