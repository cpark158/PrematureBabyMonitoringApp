package com.example.prematurebabymonitoringapp.exceptions;

/* This class checks if a date (entered as a string) is in the correct format and if the date exists
* In other words, month must be between 1 and 12. Date must be between 1 and 31.
* Reference: https://mkyong.com/java/how-to-check-if-date-is-valid-in-java/ */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DateValidator {
    public static boolean isValid(final String date) {  // checks if date string can be parsed and is valid

        boolean valid = false;

        try {

            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("uuuu-M-d")
                            .withResolverStyle(ResolverStyle.STRICT)
            );

            valid = true;

        } catch (DateTimeParseException e) {    // if date is not valid, an exception is thrown and caught
            e.printStackTrace();
            System.out.println("Invalid date input!");
            valid = false;
        }

        return valid;
    }
}

