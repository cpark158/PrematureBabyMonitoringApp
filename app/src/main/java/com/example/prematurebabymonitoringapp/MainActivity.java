package com.example.prematurebabymonitoringapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;

public class MainActivity extends AppCompatActivity {
    LineChart mpLineChart;
    TextFileProcessor txtFileProcessor = new TextFileProcessor();
    GraphPlotter graphPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       txtFileProcessor.parseFile();
        graphPlot = new GraphPlotter(txtFileProcessor.getTimeValues(),txtFileProcessor.getVoltageValues());

        mpLineChart=findViewById(R.id.line_chart);
        mpLineChart.setData(graphPlot.getData());
        mpLineChart.invalidate();

    }


    }
