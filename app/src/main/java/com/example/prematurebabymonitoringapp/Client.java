package com.example.prematurebabymonitoringapp;

import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Client {
    public static ArrayList<Patient> requestPatientList() {
        ArrayList<Patient> patientList=new ArrayList<>();

        try {
            URL myURL = new URL("https://bhmdb2020.herokuapp.com/startup");
            HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Accept", "text/html");
            conn.setRequestProperty("charset", "utf-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(myURL.openStream()));
            String inputLine;
//             Read the body of the response
            while ((inputLine = in.readLine()) != null) {
                String delims = "[}]+";
                String[] json = inputLine.split(delims);
                for (int i = 0; i < json.length; i++) {
                    Gson gson = new Gson();
                    Patient p = gson.fromJson(json[i]+"}",Patient.class);
                    patientList.add(p);
                    System.out.println(p.getName());
                    System.out.println("ok");
                }
            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return patientList;
    }
}
