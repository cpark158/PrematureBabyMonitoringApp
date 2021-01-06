package com.example.prematurebabymonitoringapp.exceptions;

/** This class throws an exception if gender input is not valid
 * Reference: https://www.tutorialspoint.com/how-can-we-create-a-custom-exception-in-java */
public class invalidGenderException extends Exception {
    String msg;

    public invalidGenderException(String str) {
        msg = str;
    }

    public String toString() {
        return ("Error Occurred : " + msg);
    }
}
