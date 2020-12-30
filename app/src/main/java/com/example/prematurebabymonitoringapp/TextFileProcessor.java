package com.example.prematurebabymonitoringapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class TextFileProcessor{
    ArrayList<Integer> time = new ArrayList<Integer>();
    ArrayList<Integer> voltage1 = new ArrayList<Integer>();
    ArrayList<Integer> voltage2 = new ArrayList<Integer>();

    // ideally we want to parse data such that we get time values in day,h,min,sec and we get two separate voltages
    public void parseFile(){
        for (int i=0;i<10;i++){
            voltage1.add(i);
            time.add(i);
        }
    }

    public ArrayList<Integer> getVolt1(){
        return voltage1;
    }

    public ArrayList<Integer> getVolt2() {return voltage2;}

    public ArrayList<Integer> getTimeValues(){
        return time;
    }

    public int getSize() {
        return time.size();
    }
}

