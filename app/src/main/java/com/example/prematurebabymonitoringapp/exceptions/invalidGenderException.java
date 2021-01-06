package com.example.prematurebabymonitoringapp.exceptions;

/** This class throws an exception if gender input is not valid
 * Reference: https://www.javatpoint.com/custom-exception */
public class invalidGenderException extends Exception {

    public invalidGenderException(String str) {
        super(str);
    }

}
