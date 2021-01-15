package com.example.prematurebabymonitoringapp;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

//This class will convert the XAxis to plot in time of day instead of integer values
//Currently not complete - future development tasks include filtering down the glucose and lactate data sets
//so that graph will plot in time of day vs glucose concentration

public class TimeXAxisValueFormatter extends ValueFormatter {
    Time t;

    public TimeXAxisValueFormatter(Time time){
        this.t = time;
    }

   /* @Override
    public String getAxisLabel(float value, AxisBase axis){

        return

    }*/
}
