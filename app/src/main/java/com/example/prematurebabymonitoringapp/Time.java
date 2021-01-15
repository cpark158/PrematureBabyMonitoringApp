package com.example.prematurebabymonitoringapp;

import java.util.ArrayList;

public class Time {
    // The time axis will be displayed in time of day (days, hours, mins and secs) instead of an increasing time value
    private int day;
    private ArrayList<Integer> h = new ArrayList<>();
    private ArrayList<Integer> m = new ArrayList<>();
    private ArrayList<Double> s = new ArrayList<>();

    // Constructor
    public Time() {
    }

    // Getters and setters
    public void getTime() {
        System.out.println("Day: " + day + ", " + h + ":" + m + ":" + s);
    }

    public void setDay(int day) {
        this.day = day;
    }
    public int getDay() {return day;}

    public void setH(ArrayList<Integer> hour) {
        for (int i=0; i<hour.size(); i++){
            h.add(hour.get(i));
        }
    }
    public ArrayList<Integer> getHour() {return h;}

    public void setM(ArrayList<Integer> min) {
        for (int i=0; i<min.size(); i++){
            h.add(min.get(i));
        }
    }
    public ArrayList<Integer> getMin() {return m;}

    public void setS(ArrayList<Double> sec) {
        for (int i=0; i<sec.size(); i++){
            s.add(sec.get(i));
        }
    }
    public ArrayList<Double> getSec() {return s;}
}
