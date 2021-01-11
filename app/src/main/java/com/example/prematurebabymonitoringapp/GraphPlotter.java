package com.example.prematurebabymonitoringapp;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

/** The GraphPlotter class is used to plot concentrations of glucose and lactose against time */
public class GraphPlotter {
    LineDataSet lineDataSet;
    ArrayList<Integer> xDataValues = new ArrayList<Integer>();
    ArrayList<Integer> yDataValues = new ArrayList<Integer>();
    ArrayList<Entry> dataValues = new ArrayList<Entry>();
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    LineData data;



    public GraphPlotter(ArrayList<Integer> xValues, ArrayList<Integer> yValues){
        xDataValues = xValues;
        yDataValues = yValues;
        for (int i=0; i<xDataValues.size();i++){
            dataValues.add(new Entry(xDataValues.get(i),yDataValues.get(i)));
        }

        lineDataSet = new LineDataSet(dataValues,"Test Data Set");
        dataSets.add(lineDataSet);
        data = new LineData(dataSets);


    }

    public LineData getData(){
        return this.data;
    }
}
