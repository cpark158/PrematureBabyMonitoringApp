package com.example.prematurebabymonitoringapp;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class TextFileProcessor {

    File file;
    Scanner s;
    ArrayList<String> timeStrings;
    ArrayList<Integer> voltage1;
    ArrayList<Integer> voltage2;
    ArrayList<Integer> hour;
    ArrayList<Integer> min;
    ArrayList<Double> sec;

    public TextFileProcessor(File fileName) {
        this.timeStrings = new ArrayList<>();
        this.voltage1 = new ArrayList<>();
        this.voltage2 = new ArrayList<>();
        this.hour = new ArrayList<>();
        this.min = new ArrayList<>();
        this.sec = new ArrayList<>();

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
            int dataPoint = Integer.parseInt(s.next()); //retrieves first string (data point)
            String timeString = s.next(); //retrieves time as a string
            timeStrings.add(timeString);
            int voltage1Val = Integer.parseInt(s.next()); //retrieves voltage1 value as a string and converts it to an integer to store in voltage1 array
            voltage1.add(voltage1Val);
            int voltage2Val = Integer.parseInt(s.next());
            voltage2.add(voltage2Val);
        }

        System.out.println(voltage1.get(3558)); //correctly prints value at index 3559
        //currently time is stored in a string in the format hour:min:sec - this code splits each time string into separate hour, min, sec components and converts them to integers for storage in respective arrays
        for (int i = 0; i < timeStrings.size(); i++) {
            String[] splitString = timeStrings.get(i).split(":"); //splitString is a string array containing 3 strings - 1 for hour, 1 for min, 1 for sec for each i
            hour.add(Integer.parseInt(splitString[0]));
            min.add(Integer.parseInt(splitString[1]));
            sec.add(Double.parseDouble(splitString[2]));

        }


    }

    public String testValString() {
        return timeStrings.get(9);
    }
}





















