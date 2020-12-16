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

    public void parseFile(){
        for (int i=0;i<10;i++){
            voltage1.add(i);
            time.add(i);
        }
    }

    public ArrayList<Integer> getVoltageValues(){
        return voltage1;
    }

    public ArrayList<Integer> getTimeValues(){
        return time;
    }

}

